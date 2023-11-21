package ru.kowalski.DeliveryHelper3000.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kowalski.DeliveryHelper3000.model.Partner;
import ru.kowalski.DeliveryHelper3000.model.Product;
import ru.kowalski.DeliveryHelper3000.services.PartnerService;
import ru.kowalski.DeliveryHelper3000.services.ProductService;
import ru.kowalski.DeliveryHelper3000.util.GeoCoderService;
import ru.kowalski.DeliveryHelper3000.util.GeoRouterService;

import java.util.List;

// Класс-контроллер для операций с торговыми точками пользователей

@Controller
@RequestMapping("/partner")
@RequiredArgsConstructor
public class PartnerController {

    private final PartnerService partnerService;
    private final ProductService productService;
    private final GeoCoderService geoCoderService;
    private final GeoRouterService geoRouterService;

    @GetMapping()
    public String partnerPage(Model model) {
        model.addAttribute("partners", partnerService.findAll());
        return "/partner/index";
    }

    @GetMapping("/new")
    public String createPartnerPage(@ModelAttribute("partner") Partner partner) {
        return "/partner/new";
    }

    @PostMapping("/new")
    public String createNewPartner(@ModelAttribute("partner") Partner partner) throws JsonProcessingException {
        String[] geoCode = geoCoderService.getGeoCodeFromHTTP(partner.getPartnerAddress());
        String address = geoCode[0];
        Double latitude = Double.valueOf(geoCode[1]);
        Double longitude = Double.valueOf(geoCode[2]);
        int time = Integer.parseInt(geoRouterService.getGeoRouteFromHTTP(longitude, latitude)[0]) / 60;
        partner.setPartnerAddress(address);
        partner.setLatitude(latitude);
        partner.setLongitude(longitude);
        partner.setTimeForDeliveryInMinutes(time);
        partnerService.save(partner);
        return "redirect:/partner";
    }

    @GetMapping("/show/{id}")
    public String showPartner(@PathVariable("id") Long id, Model model) {
        model.addAttribute("partner", partnerService.findPartnerById(id));
        return "/partner/show";
    }

    @GetMapping("/edit/{id}")
    public String editPartner(@PathVariable("id") Long id, Model model) {
        model.addAttribute("partner", partnerService.findPartnerById(id));
        return "/partner/edit";
    }

    @GetMapping("/getProducts")
    @ResponseBody
    public List<Product> getProducts() {
        return productService.getAllProducts();
    }

}
