package NhtK23cnt3.project3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class NhtPromotionController {

    @GetMapping("/promotion")
    public String promotion() {
        return "NhtPromotion";
    }
}
