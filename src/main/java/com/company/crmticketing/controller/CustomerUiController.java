package com.company.crmticketing.controller;


import com.company.crmticketing.dto.customer.CustomerDto;
import com.company.crmticketing.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/ui/customers")
@RequiredArgsConstructor
public class CustomerUiController {

    private final CustomerService customerService;

    @GetMapping
    public String list(Model model) {
        model.addAttribute("customers",
                customerService.findAllDtos());

        return "customers/list";
    }

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("customer",
                new CustomerDto());

        return "customers/form";
    }

    @PostMapping
    public String save(@ModelAttribute CustomerDto dto) {
        customerService.createCustomer(dto);
        return "redirect:/ui/customers";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id,
                         Model model) {

        model.addAttribute("customer",
                customerService.findDtoById(id));

        return "customers/detail";
    }
}