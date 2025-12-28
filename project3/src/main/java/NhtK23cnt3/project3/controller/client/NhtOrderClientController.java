package NhtK23cnt3.project3.controller.client;

import NhtK23cnt3.project3.entity.order.NhtOrder;
import NhtK23cnt3.project3.service.order.NhtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/order")
public class NhtOrderClientController {

    private final NhtOrderService orderService;

    // =============================================================
    //  TRANG THÔNG BÁO ĐẶT HÀNG THÀNH CÔNG
    // =============================================================
    @GetMapping("/success")
    public String orderSuccess(@RequestParam("id") Long id, Model model) {

        NhtOrder order = orderService.getById(id);

        if (order == null) {
            return "redirect:/";  // không có đơn thì về Home
        }

        model.addAttribute("order", order);
        return "order/NhtOrderSuccess";
    }
}
