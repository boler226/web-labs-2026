package com.university.productcatalog.controller;

import com.university.productcatalog.entity.Product;
import com.university.productcatalog.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) String query, Model model) {
        model.addAttribute("products", productService.findAll(query));
        model.addAttribute("query", query);
        return "products/list";
    }

    @GetMapping("/new")
    public String newForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("formAction", "/products");
        return "products/form";
    }

    @PostMapping
    public String create(@Valid @ModelAttribute("product") Product product,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/products");
            return "products/form";
        }
        productService.create(product);
        redirectAttributes.addFlashAttribute("message", "Товар \"%s\" успішно додано".formatted(product.getName()));
        return "redirect:/products";
    }

    @GetMapping("/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.findById(id));
        model.addAttribute("formAction", "/products/" + id);
        return "products/form";
    }

    @PostMapping("/{id}")
    public String update(@PathVariable Long id,
                          @Valid @ModelAttribute("product") Product product,
                          BindingResult bindingResult,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/products/" + id);
            return "products/form";
        }
        productService.update(id, product);
        redirectAttributes.addFlashAttribute("message", "Товар \"%s\" оновлено".formatted(product.getName()));
        return "redirect:/products";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Product product = productService.findById(id);
        productService.delete(id);
        redirectAttributes.addFlashAttribute("message", "Товар \"%s\" видалено".formatted(product.getName()));
        return "redirect:/products";
    }
}
