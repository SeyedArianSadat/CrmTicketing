package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.customer.CustomerCreateDto;
import com.company.crmticketing.dto.customer.CustomerDto;
import com.company.crmticketing.dto.customer.CustomerUpdateDto;
import com.company.crmticketing.exception.CustomerNotFoundException;
import com.company.crmticketing.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customers")
public class CustomerMvcController {

    private final CustomerService customerService;

    @GetMapping
    public String list(Model model){

        model.addAttribute("customers",
                customerService.findAllDtos());

        return "customer/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model){

        CustomerDto customer = customerService.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        model.addAttribute("customer", customer);

        return "customer/details";
    }

    @GetMapping("/create")
    public String create(Model model){

        model.addAttribute("customer",
                new CustomerCreateDto(
                        "",
                        "",
                        "",
                        null
                ));

        return "customer/create";
    }

    @PostMapping
    public String save(
            @ModelAttribute CustomerCreateDto dto){

        customerService.createCustomer(dto);

        return "redirect:/customers";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model){

        CustomerDto customer = customerService.findById(id)
                .orElseThrow(() -> new CustomerNotFoundException(id));

        model.addAttribute("customer", customer);

        return "customer/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute CustomerUpdateDto dto){

        customerService.updateCustomer(id,dto);

        return "redirect:/customers";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){

        customerService.deleteByCustomerId(id);

        return "redirect:/customers";
    }

}