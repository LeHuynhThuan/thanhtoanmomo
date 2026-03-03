package LeHuynhThuan.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/contact")
public class ContactController {

    @GetMapping
    public String contactForm() {
        return "contact";
    }

    @PostMapping
    public String submitContact(@RequestParam String fullName,
                                 @RequestParam String email,
                                 @RequestParam String message,
                                 Model model) {
        model.addAttribute("fullName", fullName);
        model.addAttribute("email", email);
        model.addAttribute("message", message);
        return "contact-result";
    }
}
