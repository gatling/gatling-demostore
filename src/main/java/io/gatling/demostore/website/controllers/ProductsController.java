package io.gatling.demostore.website.controllers;

import io.gatling.demostore.models.ProductRepository;
import io.gatling.demostore.models.data.Product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class ProductsController {

    @Autowired
    private ProductRepository productRepository;

    @GetMapping("/{slug}")
    public String product(@PathVariable String slug, Model model) {
        Product product = productRepository.findBySlug(slug);

        if (product == null) {
            return "redirect:/error";
        }

        model.addAttribute("product", product);

        return "product_details";
    }
}
