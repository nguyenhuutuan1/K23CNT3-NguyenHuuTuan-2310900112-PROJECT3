package NhtK23cnt3.project3.controller.client;

import NhtK23cnt3.project3.entity.order.NhtOrder;
import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.model.cart.NhtCartItem;
import NhtK23cnt3.project3.order.NhtCheckoutForm;
import NhtK23cnt3.project3.service.cart.NhtCartService;
import NhtK23cnt3.project3.service.order.NhtOrderService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class NhtCheckoutClientController {

    private final NhtCartService cartService;
    private final NhtOrderService orderService;

    // ============= GET CHECKOUT =============
    @GetMapping("/checkout")
    public String showCheckoutPage(Model model, HttpSession session) {

        // ⭐ 1. Kiểm tra đăng nhập
        NhtUser user = (NhtUser) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login?redirect=/checkout";
        }

        // ⭐ 2. Kiểm tra giỏ hàng
        List<NhtCartItem> items = cartService.getItems(session);
        if (items == null || items.isEmpty()) {
            return "redirect:/cart";
        }

        // ⭐ 3. Khởi tạo form nếu chưa có
        if (!model.containsAttribute("checkoutForm")) {
            model.addAttribute("checkoutForm", new NhtCheckoutForm());
        }

        model.addAttribute("items", items);
        model.addAttribute("total", cartService.getTotal(session));

        return "order/NhtCheckout";
    }

    // ============= POST CHECKOUT =============
    @PostMapping("/checkout")
    public String handleCheckout(@Valid @ModelAttribute("checkoutForm") NhtCheckoutForm form,
                                 BindingResult bindingResult,
                                 HttpSession session,
                                 Model model) {

        // ⭐ 1. Chưa login → bắt buộc login
        NhtUser user = (NhtUser) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login?redirect=/checkout";
        }

        // ⭐ 2. Lấy giỏ hàng
        List<NhtCartItem> items = cartService.getItems(session);
        if (items == null || items.isEmpty()) {
            return "redirect:/cart";
        }

        // ⭐ 3. Validate lỗi
        if (bindingResult.hasErrors()) {
            model.addAttribute("items", items);
            model.addAttribute("total", cartService.getTotal(session));
            return "order/NhtCheckout";
        }

        // ⭐ 4. Tạo đơn hàng GẮN USER
        NhtOrder order = orderService.createOrderFromCart(form, items, user);

        // ⭐ 5. Xóa giỏ
        cartService.clear(session);

        return "redirect:/order/success?id=" + order.getId();
    }
}
