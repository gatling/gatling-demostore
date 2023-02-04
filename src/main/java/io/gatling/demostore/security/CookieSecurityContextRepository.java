package io.gatling.demostore.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.context.HttpRequestResponseHolder;
import org.springframework.security.web.context.SaveContextOnUpdateOrErrorResponseWrapper;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;
import java.util.stream.Stream;

@Component
public class CookieSecurityContextRepository implements SecurityContextRepository {
    private static final Logger LOG = LoggerFactory.getLogger(CookieSecurityContextRepository.class);

    private static final String EMPTY_CREDENTIALS = "";
    private static final String ANONYMOUS_USER = "anonymousUser";

    private final String cookieHmacKey;
    private final UserDetailsService userDetailsService;

    public CookieSecurityContextRepository(@Value("${auth.cookie.hmac-key}") String cookieHmacKey, UserDetailsService userDetailsService) {
        this.cookieHmacKey = cookieHmacKey;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public SecurityContext loadContext(HttpRequestResponseHolder requestResponseHolder) {
        HttpServletRequest request = requestResponseHolder.getRequest();
        HttpServletResponse response = requestResponseHolder.getResponse();
        requestResponseHolder.setResponse(new SaveToCookieResponseWrapper(request, response));

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        readUserInfoFromCookie(request).ifPresent(userInfo ->
                context.setAuthentication(new UsernamePasswordAuthenticationToken(userInfo, EMPTY_CREDENTIALS, userInfo.getAuthorities())));

        return context;
    }

    @Override
    public void saveContext(SecurityContext context, HttpServletRequest request, HttpServletResponse response) {
        SaveToCookieResponseWrapper responseWrapper = (SaveToCookieResponseWrapper) response;
        if (!responseWrapper.isContextSaved()) {
            responseWrapper.saveContext(context);
        }
    }

    @Override
    public boolean containsContext(HttpServletRequest request) {
        return readUserInfoFromCookie(request).isPresent();
    }

    private Optional<UserDetails> readUserInfoFromCookie(HttpServletRequest request) {
        return readCookieFromRequest(request)
                .map(this::createUserInfo);
    }

    private Optional<Cookie> readCookieFromRequest(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return Optional.empty();
        }

        return Stream.of(request.getCookies())
                .filter(c -> SignedUserInfoCookie.NAME.equals(c.getName()))
                .findFirst();
    }

    private UserDetails createUserInfo(Cookie cookie) {
        return new SignedUserInfoCookie(cookie, cookieHmacKey).getUserDetails(userDetailsService);
    }

    private class SaveToCookieResponseWrapper extends SaveContextOnUpdateOrErrorResponseWrapper {
        private final HttpServletRequest request;

        SaveToCookieResponseWrapper(HttpServletRequest request, HttpServletResponse response) {
            super(response, true);
            this.request = request;
        }

        @Override
        protected void saveContext(SecurityContext securityContext) {
            HttpServletResponse response = (HttpServletResponse) getResponse();
            Authentication authentication = securityContext.getAuthentication();

            if (authentication == null) {
                LOG.debug("No securityContext.authentication, skip saveContext");
                return;
            }

            if (ANONYMOUS_USER.equals(authentication.getPrincipal())) {
                LOG.debug("Anonymous User SecurityContext, skip saveContext");
                return;
            }

            if (!(authentication.getPrincipal() instanceof UserDetails)) {
                LOG.warn("securityContext.authentication.principal of unexpected type {}, skip saveContext", authentication.getPrincipal().getClass().getCanonicalName());
                return;
            }

            UserDetails userInfo = (UserDetails) authentication.getPrincipal();
            SignedUserInfoCookie cookie = new SignedUserInfoCookie(userInfo.getUsername(), cookieHmacKey);
            cookie.setSecure(request.isSecure());
            response.addCookie(cookie);
        }
    }
}
