package NhtK23cnt3.project3.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/service")
public class NhtServiceController {

    @GetMapping
    public String serviceHome() {
        return "service/index";
    }

    @GetMapping("/gift")
    public String gift() {
        return "service/gift";
    }

    @GetMapping("/delivery")
    public String delivery() {
        return "service/delivery";
    }

    @GetMapping("/clean")
    public String clean() {
        return "service/clean";
    }

    @GetMapping("/combo")
    public String combo() {
        return "service/combo";
    }

    @GetMapping("/business")
    public String business() {
        return "service/business";
    }

    @GetMapping("/advice")
    public String advice() {
        return "service/advice";
    }
}
