package io.gatling.demostore;

import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import io.gatling.demostore.models.CartEntry;
import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.PageRepository;
import io.gatling.demostore.models.data.Category;
import io.gatling.demostore.models.data.Page;

import io.gatling.demostore.website.controllers.CartController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class Common {

    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @ModelAttribute
    public void sharedData(Model model, @CookieValue(value = CartController.Carts.COOKIE_NAME, required = false) String cartCookie, Principal principal) throws IOException {
        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }

        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();

        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();

        boolean cartActive = false;

        if (cartCookie != null) {
            Map<Integer, CartEntry> cart = CartController.Carts.fromCookieValue(cartCookie);

            int size = 0;
            double total = 0;

            for (CartEntry value : cart.values()) {
                size += value.getQuantity();
                total += value.getQuantity() * Double.parseDouble(value.getPrice());
            }

            model.addAttribute("csize", size);
            model.addAttribute("ctotal", total);

            cartActive = true;
        }

        model.addAttribute("cpages", pages);
        model.addAttribute("ccategories", categories);
        model.addAttribute("cartActive", cartActive);
    }
}
