package io.gatling.demostore;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashMap;
import java.util.List;

import io.gatling.demostore.models.Cart;
import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.PageRepository;
import io.gatling.demostore.models.data.Category;
import io.gatling.demostore.models.data.Page;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
@SuppressWarnings("unchecked")
public class Common {

    @Autowired
    private PageRepository pageRepo;

    @Autowired
    private CategoryRepository categoryRepo;

    @ModelAttribute
    public void sharedData(Model model, HttpSession session, Principal principal) {
        if (principal != null) {
            model.addAttribute("principal", principal.getName());
        }

        List<Page> pages = pageRepo.findAllByOrderBySortingAsc();

        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();

        boolean cartActive = false;

        if (session.getAttribute("cart") != null) {
            HashMap<Integer, Cart> cart = (HashMap<Integer, Cart>) session.getAttribute("cart");

            int size = 0;
            double total = 0;

            for (Cart value : cart.values()) {
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
