package NhtK23cnt3.project3.controller.client;

import NhtK23cnt3.project3.order.NhtOrderTrackForm;
import NhtK23cnt3.project3.service.order.NhtOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import jakarta.validation.Valid;

@Controller
@RequiredArgsConstructor
public class NhtOrderTrackController {

    private final NhtOrderService orderService;

    @GetMapping("/order/track")
    public String showForm(Model model) {
        model.addAttribute("trackForm", new NhtOrderTrackForm());
        return "order/NhtOrderTracking";
    }

    @PostMapping("/order/track")
    public String trackOrder(
            @Valid NhtOrderTrackForm form,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("trackForm", form);
            return "order/NhtOrderTracking";
        }

        var order = orderService.findByIdAndPhone(form.getOrderId(), form.getPhone());

        if (order == null) {
            model.addAttribute("notFound", true);
            model.addAttribute("trackForm", form);
            return "order/NhtOrderTracking";
        }

        model.addAttribute("order", order);
        model.addAttribute("trackForm", form);
        return "order/NhtOrderTracking";
    }
}
