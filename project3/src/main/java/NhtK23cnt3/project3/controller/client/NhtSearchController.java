package NhtK23cnt3.project3.controller.client;

import NhtK23cnt3.project3.entity.product.NhtProduct;
import NhtK23cnt3.project3.service.product.NhtProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NhtSearchController {

    private final NhtProductService productService;

    @GetMapping("/search")
    public String searchProducts(@RequestParam("keyword") String keyword, Model model) {

        List<NhtProduct> results = productService.searchByKeyword(keyword);

        model.addAttribute("keyword", keyword);
        model.addAttribute("products", results);

        return "product/NhtSearchResult";   // View kết quả
    }
}
