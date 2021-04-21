package io.gatling.demostore.website.controllers;

import javax.validation.Valid;
import java.util.List;

import io.gatling.demostore.models.CategoryRepository;
import io.gatling.demostore.models.data.Category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoriesController {

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping
    public String index(Model model) {
        List<Category> categories = categoryRepo.findAllByOrderBySortingAsc();

        model.addAttribute("categories", categories);

        return "admin/categories/index";
    }

    @GetMapping("/add")
    public String add(Category category) {
        return "admin/categories/add";
    }

    @PostMapping("/add")
    public String add(
            @Valid Category category,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "admin/categories/add";
        }

        redirectAttributes.addFlashAttribute("message", "Category added");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = category.getName().toLowerCase().replace(" ", "-");

        Category categoryExists = categoryRepo.findByName(category.getName());

        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
            redirectAttributes.addFlashAttribute("categoryInfo", category);
        } else {
            category.setSlug(slug);
            category.setSorting(100);

            // DO NO SAVE (readonly)
        }

        return "redirect:/admin/categories/add";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, Model model) {
        Category category = categoryRepo.getOne(id);

        model.addAttribute("category", category);

        return "admin/categories/edit";
    }

    @PostMapping("/edit")
    public String edit(
            @Valid Category category,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Category categoryCurrent = categoryRepo.getOne(category.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("categoryName", categoryCurrent.getName());
            return "admin/categories/edit";
        }

        redirectAttributes.addFlashAttribute("message", "Category edited");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        String slug = category.getName().toLowerCase().replace(" ", "-");

        Category categoryExists = categoryRepo.findByName(category.getName());

        if (categoryExists != null) {
            redirectAttributes.addFlashAttribute("message", "Category exists, choose another");
            redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
        } else {
            category.setSlug(slug);

            // DO NOT SAVE (readonly)
        }

        return "redirect:/admin/categories/edit/" + category.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, RedirectAttributes redirectAttributes) {
        categoryRepo.deleteById(id);

        redirectAttributes.addFlashAttribute("message", "Category deleted");
        redirectAttributes.addFlashAttribute("alertClass", "alert-success");

        return "redirect:/admin/categories";
    }

    @PostMapping("/reorder")
    @ResponseBody
    public String reorder() {
        return "ok";
    }
}
