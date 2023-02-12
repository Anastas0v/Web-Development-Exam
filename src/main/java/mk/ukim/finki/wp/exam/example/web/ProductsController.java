package mk.ukim.finki.wp.exam.example.web;

import mk.ukim.finki.wp.exam.example.model.Category;
import mk.ukim.finki.wp.exam.example.model.Product;
import mk.ukim.finki.wp.exam.example.service.CategoryService;
import mk.ukim.finki.wp.exam.example.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class ProductsController
{

    private final ProductService service;
    private final CategoryService categoryService;

    public ProductsController(ProductService service, CategoryService categoryService)
    {
        this.service = service;
        this.categoryService = categoryService;
    }

    @GetMapping({"/", "/products"})
    public String showProducts(@RequestParam(required = false) String nameSearch, @RequestParam(required = false) Long categoryId, Model model)
    {
        List<Product> productList;
        if (nameSearch == null && categoryId == null) {
            productList = this.service.listAllProducts();
        } else {
            productList = this.service.listProductsByNameAndCategory(nameSearch, categoryId);
        }
        model.addAttribute("products", productList);
        return "list.html";
    }

    @GetMapping("/products/add")
    public String showAdd(Model model)
    {
        List<Category> categoryList = this.categoryService.listAll();
        model.addAttribute("categories", categoryList);
        return "form.html";
    }

    @GetMapping("/products/{id}/edit")
    public String showEdit(@PathVariable Long id, Model model)
    {
        Product product = this.service.findById(id);
        List<Category> categoryList = this.categoryService.listAll();
        model.addAttribute("categories", categoryList);
        model.addAttribute("product", product);
        return "form.html";
    }

    @PostMapping("/products")
    public String create(@RequestParam String name, @RequestParam Double price, @RequestParam Integer quantity, @RequestParam List<Long> categories)
    {
        this.service.create(name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}")
    public String update(@PathVariable Long id, @RequestParam String name, @RequestParam Double price, @RequestParam Integer quantity, @RequestParam List<Long> categories)
    {
        this.service.update(id, name, price, quantity, categories);
        return "redirect:/products";
    }

    @PostMapping("/products/{id}/delete")
    public String delete(@PathVariable Long id) {
        this.service.delete(id);
        return "redirect:/products";
    }
}
