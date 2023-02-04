package io.gatling.demostore.session;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.demostore.models.Cart;
import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.data.Product;
import io.gatling.demostore.security.CookieSecurityContextRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.session.MapSession;
import org.springframework.stereotype.Service;

import javax.servlet.http.Cookie;
import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CookieSession {
    private static final Logger LOG = LoggerFactory.getLogger(CookieSecurityContextRepository.class);
    public static final String COOKIE_NAME = "cookie_session";

    private static final ObjectMapper JACKSON = new ObjectMapper();
    private static final TypeReference<Map<Integer, Integer>> SessionDataType = new TypeReference<>() {
    };

    public CookieSession(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    private final ProductRepository productRepository;

    public Cookie fromMapSession(MapSession session) {
        final Map<Integer, Integer> sessionData;
        Map<Integer, Cart> cart = session.getAttribute("cart");
        if (cart != null) {
            sessionData = cart.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().getQuantity()));
        } else {
            sessionData = Collections.emptyMap();
        }

        String cookieValue = "";
        try {
            cookieValue = Base64.getEncoder().encodeToString(JACKSON.writeValueAsBytes(sessionData));
        } catch (IOException e) {
            LOG.warn("Cannot encode cart in cookie", e);
        }

        Cookie cookie = new Cookie(COOKIE_NAME, cookieValue);
        cookie.setPath("/");
        return cookie;
    }

    @Transactional
    public MapSession fromCookies(Cookie[] cookies) {
        String cookieValue = Arrays.stream(cookies)
                .filter(cookie -> CookieSession.COOKIE_NAME.equals(cookie.getName()))
                .map(Cookie::getValue)
                .collect(Collectors.joining());
        MapSession session = new MapSession();
        try {
            Map<Integer, Integer> sessionData = JACKSON.readValue(Base64.getDecoder().decode(cookieValue), SessionDataType);
            if (sessionData != null) {
                Map<Integer, Cart> cartAttribute = toCart(sessionData);
                if(!cartAttribute.isEmpty()) {
                    session.setAttribute("cart", cartAttribute);
                }
            }
        } catch (IOException e) {
            LOG.warn("Cannot read cart from cookie", e);
        }
        return session;
    }

    private Map<Integer, Cart> toCart(Map<Integer, Integer> cartSession) {
        return cartSession.entrySet().stream().collect(Collectors.toMap(
                Map.Entry::getKey, entry -> {
                    Product product = productRepository.getById(entry.getKey());
                    return new Cart(entry.getKey(), product.getName(), product.getPrice(), entry.getValue(), product.getImage());
                }));
    }

    public Cookie deleteCookie() {
        Cookie cookie = new Cookie(CookieSession.COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        return cookie;
    }
}
