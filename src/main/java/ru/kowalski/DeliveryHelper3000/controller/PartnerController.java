package ru.kowalski.DeliveryHelper3000.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Person;
import ru.kowalski.DeliveryHelper3000.security.PersonDetails;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.PersonService;

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final PersonService personService;

    @GetMapping()
    public String partnerPage(Model model){
        model.addAttribute("partners",partnerService.findAll());
        return "/partner/index";
    }

    @GetMapping("/new")
    public String createPartnerPage(@ModelAttribute("partner") Partner partner){
        return "/partner/new";
    }

    @PostMapping("/new")
    public String createNewPartner(@ModelAttribute("partner") Partner partner){
        partnerService.save(partner);
        return "redirect:/partner";
    }

    @GetMapping("/show/{id}")
    public String showPartner(@PathVariable("id") Long id,Model model){
        model.addAttribute("partner",partnerService.findOne(id));
        return "/partner/show";
    }


    private Person getCurrentPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        return personDetails.getPerson();
    }
}
