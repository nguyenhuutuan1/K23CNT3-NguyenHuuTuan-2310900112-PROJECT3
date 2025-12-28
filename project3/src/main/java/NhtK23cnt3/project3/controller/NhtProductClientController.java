package NhtK23cnt3.project3.controller;

import NhtK23cnt3.project3.entity.product.NhtProductComment;
import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.repository.product.NhtProductCommentRepository;
import NhtK23cnt3.project3.service.product.NhtProductService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class NhtProductClientController {

    private final NhtProductService productService;
    private final NhtProductCommentRepository commentRepository;

    /* ========== CHI TIẾT SẢN PHẨM + COMMENT ========== */
    @GetMapping("/product/{id}")
    public String getDetail(@PathVariable Long id,
                            Model model,
                            HttpSession session) {

        var product = productService.getById(id);
        if (product == null) {
            return "redirect:/";
        }

        // user đăng nhập
        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");

        // danh sách comment
        List<NhtProductComment> comments =
                commentRepository.findByProductIdOrderByCreatedAtDesc(id);

        model.addAttribute("product", product);
        model.addAttribute("comments", comments);
        model.addAttribute("currentUser", currentUser);

        return "product/NhtProductDetail";
    }

    /* ========== GỬI BÌNH LUẬN + SAO ========== */
    @PostMapping("/product/{id}/comment")
    public String postComment(@PathVariable Long id,
                              @RequestParam("content") String content,
                              @RequestParam("star") int star,
                              HttpSession session) {

        NhtUser currentUser = (NhtUser) session.getAttribute("currentUser");
        if (currentUser == null) {
            return "redirect:/login";
        }

        var product = productService.getById(id);
        if (product == null) {
            return "redirect:/";
        }

        NhtProductComment comment = NhtProductComment.builder()
                .content(content)
                .star(star)
                .product(product)
                .user(currentUser)
                .createdAt(LocalDateTime.now())
                .build();

        commentRepository.save(comment);

        return "redirect:/product/" + id;
    }

    /* ========== TAG ========== */
    @GetMapping("/tag/{tag}")
    public String getByTag(@PathVariable String tag, Model model) {

        Map<String, String> tagDisplay = Map.of(
                "tangbanhai", "Tặng bạn gái",
                "bigsize", "Gấu size lớn",
                "intenn", "Gấu bông in tên"
        );

        String displayName = tagDisplay.getOrDefault(tag, tag);

        model.addAttribute("displayName", displayName);
        model.addAttribute("products", productService.findByTag(tag));

        return "product/NhtTagProducts";
    }
}
