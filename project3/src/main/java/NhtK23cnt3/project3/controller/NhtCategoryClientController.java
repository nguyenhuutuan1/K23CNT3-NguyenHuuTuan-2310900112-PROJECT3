package NhtK23cnt3.project3.controller;

import NhtK23cnt3.project3.entity.product.NhtCategory;
import NhtK23cnt3.project3.service.product.NhtProductService;
import NhtK23cnt3.project3.service.product.NhtCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor
public class NhtCategoryClientController {

    private final NhtCategoryService categoryService;
    private final NhtProductService productService;

    @GetMapping("/category/{id}")
    public String categoryPage(@PathVariable Long id, Model model) {

        // Lấy category hiện tại
        NhtCategory cat = categoryService.getById(id);
        if (cat == null) {
            // Nếu category không tồn tại → chuyển về trang chủ hoặc trang 404
            return "redirect:/?error=category_not_found";
        }

        // Danh sách category để hiển thị menu
        model.addAttribute("categories", categoryService.getAll());

        // Danh sách sản phẩm thuộc category
        model.addAttribute("products", productService.getByCategory(id));

        // Active styling cho category đang chọn
        model.addAttribute("activeCategoryId", id);

        // Để hiển thị tên category trên tiêu đề trang
        model.addAttribute("currentCategory", cat);

        return "NhtCategory";
    }
}
