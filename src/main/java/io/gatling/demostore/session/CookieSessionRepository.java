package io.gatling.demostore.session;

import org.springframework.session.MapSession;
import org.springframework.session.SessionRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Component
public class CookieSessionRepository implements SessionRepository<MapSession> {
    private final CookieSession cookieSession;

    public CookieSessionRepository(CookieSession cookieSession) {
        this.cookieSession = cookieSession;
    }

    public static Optional<HttpServletRequest> getCurrentHttpRequest() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getRequest);
    }

    public static Optional<HttpServletResponse> getCurrentHttpResponse() {
        return Optional.ofNullable(RequestContextHolder.getRequestAttributes())
                .filter(ServletRequestAttributes.class::isInstance)
                .map(ServletRequestAttributes.class::cast)
                .map(ServletRequestAttributes::getResponse);
    }

    @Override
    public MapSession createSession() {
        return new MapSession("id");
    }

    @Override
    public void save(MapSession mapSession) {
        getCurrentHttpResponse().ifPresent(response -> response.addCookie(cookieSession.fromMapSession(mapSession)));
    }

    @Override
    public MapSession findById(String s) {
        return getCurrentHttpRequest()
                .map(request -> cookieSession.fromCookies(request.getCookies()))
                .orElseGet(this::createSession);    }

    @Override
    public void deleteById(String s) {
        getCurrentHttpResponse().ifPresent(response -> response.addCookie(cookieSession.deleteCookie()));
    }
}
