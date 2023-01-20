package io.gatling.demostore.website.controllers;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.demostore.models.CartEntry;
import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
@SuppressWarnings("unchecked")
public class CartController {

    public static final class Carts {

        public static final String COOKIE_NAME = "cart";

        private static ObjectMapper JACKSON = new ObjectMapper();

        private static TypeReference<Map<Integer, CartEntry>> CART_TYPE = new TypeReference<Map<Integer, CartEntry>>() {};

        public static Map<Integer, CartEntry> fromCookieValue(String cookieValue) throws IOException {
            if (cookieValue == null) {
                return null;
            }

            return JACKSON.readValue(Base64.getDecoder().decode(cookieValue), CART_TYPE);
        }

        public static String toCookieValue(Map<Integer, CartEntry> cart) throws IOException {
            if (cart == null) {
                return null;
            }

            return Base64.getEncoder().encodeToString(JACKSON.writeValueAsString(cart).getBytes(StandardCharsets.UTF_8));
        }
    }


    @Autowired
    private ProductRepository productRepo;

    @GetMapping("/add/{id}")
    public String add(
            @PathVariable int id,
            Model model,
            @RequestParam(value = "cartPage", required = false) String cartPage,
            @CookieValue(value = Carts.COOKIE_NAME, required = false) String cartCookie,
            HttpServletResponse response
    ) throws IOException {
        Product product = productRepo.getOne(id);

        Map<Integer, CartEntry> cart;
        if (cartCookie == null) {
            cart = new HashMap<>();
            cart.put(id, new CartEntry(id, product.getName(), product.getPrice(), 1, product.getImage()));

        } else {
            cart = Carts.fromCookieValue(cartCookie);
            if (cart.containsKey(id)) {
                int qty = cart.get(id).getQuantity();
                cart.put(id, new CartEntry(id, product.getName(), product.getPrice(), ++qty, product.getImage()));
            } else {
                cart.put(id, new CartEntry(id, product.getName(), product.getPrice(), 1, product.getImage()));
            }
        }
        updateCartCookie(response, cart);

        int size = 0;
        double total = 0;

        for (CartEntry value : cart.values()) {
            size += value.getQuantity();
            total += value.getQuantity() * Double.parseDouble(value.getPrice());
        }

        model.addAttribute("size", size);
        model.addAttribute("total", total);

        if (cartPage != null) {
            return "redirect:/cart/view";
        }

        return "cart_view";
    }

    @GetMapping("/subtract/{id}")
    public String subtract(
            @PathVariable int id,
            @CookieValue(value = Carts.COOKIE_NAME, required = false) String cartCookie,
            HttpServletResponse response,
            HttpServletRequest httpServletRequest
    ) throws IOException {
        Product product = productRepo.getOne(id);

        Map<Integer, CartEntry> cart = Carts.fromCookieValue(cartCookie);

        int qty = cart.get(id).getQuantity();

        if (qty == 1) {
            cart.remove(id);
        } else {
            cart.put(id, new CartEntry(id, product.getName(), product.getPrice(), --qty, product.getImage()));
        }

        if (cart.isEmpty()) {
            removeCartCookie(response);
        } else {
            updateCartCookie(response, cart);
        }

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }

    @GetMapping("/remove/{id}")
    public String remove(
            @PathVariable int id,
            @CookieValue(value = Carts.COOKIE_NAME, required = false) String cartCookie,
            HttpServletRequest httpServletRequest,
            HttpServletResponse response
    ) throws IOException {
        Map<Integer, CartEntry> cart = Carts.fromCookieValue(cartCookie);
        cart.remove(id);

        if (cart.isEmpty()) {
            removeCartCookie(response);
        } else {
            updateCartCookie(response, cart);
        }

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }

    @GetMapping("/clear")
    public String clear(
      @CookieValue(value = Carts.COOKIE_NAME, required = false) String cartCookie,
      HttpServletRequest httpServletRequest,
      HttpServletResponse response
    ) {
        if (cartCookie != null) {
            removeCartCookie(response);
        }

        String refererLink = httpServletRequest.getHeader("referer");

        return "redirect:" + refererLink;
    }

    @GetMapping("/view")
    public String view(
      Model model,
      @CookieValue(value = Carts.COOKIE_NAME, required = false) String cartCookie
    ) throws IOException  {
        if (cartCookie == null) {
            return "redirect:/";
        }

        Map<Integer, CartEntry> cart = Carts.fromCookieValue(cartCookie);

        model.addAttribute("cart", cart);
        model.addAttribute("notCartViewPage", true);

        return "cart";
    }

    @GetMapping("/checkout")
    public String checkout(
      @CookieValue(value = Carts.COOKIE_NAME, required = false) String cartCookie,
      HttpServletResponse response
    ) {
        if (cartCookie == null) {
            return "redirect:/";
        }

        removeCartCookie(response);

        return "redirect:/cart/checkoutConfirmation";
    }

    @GetMapping("/checkoutConfirmation")
    public String checkoutConfirmation() {
        return "checkout";
    }

    private static void updateCartCookie(HttpServletResponse response, Map<Integer, CartEntry> cart) throws IOException {
        Cookie cookie = new Cookie(Carts.COOKIE_NAME, Carts.toCookieValue(cart));
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private static void removeCartCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie(Carts.COOKIE_NAME, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
    }
}
