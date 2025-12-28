package NhtK23cnt3.project3.controller;

import NhtK23cnt3.project3.service.product.NhtProductService;
import NhtK23cnt3.project3.service.product.NhtCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NhtHomeController {

    private final NhtCategoryService categoryService;
    private final NhtProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", categoryService.getAll());
        model.addAttribute("products", productService.getAll());
        return "NhtHome";
    }

}
