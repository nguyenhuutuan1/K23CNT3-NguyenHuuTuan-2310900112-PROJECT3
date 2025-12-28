package NhtK23cnt3.project3.controller.client;

import NhtK23cnt3.project3.entity.user.NhtUser;
import NhtK23cnt3.project3.service.order.NhtOrderService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class NhtMyOrderController {

    private final NhtOrderService orderService;

    @GetMapping("/my-orders")
    public String viewMyOrders(HttpSession session, Model model) {

        // Check login
        NhtUser user = (NhtUser) session.getAttribute("currentUser");
        if (user == null) {
            return "redirect:/login?redirect=/my-orders";
        }

        model.addAttribute("orders",
                orderService.findByUserId(user.getId())
        );

        return "order/NhtMyOrders";
    }
}
