package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.customerRequest.CustomerRequestCreateDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestDto;
import com.company.crmticketing.dto.customerRequest.CustomerRequestUpdateDto;
import com.company.crmticketing.exception.CustomerRequestNotFoundException;
import com.company.crmticketing.service.CustomerRequestService;
import com.company.crmticketing.model.enums.Channel;
import com.company.crmticketing.model.enums.RequestStatus;
import com.company.crmticketing.model.enums.RequestType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/customerRequests")
public class CustomerRequestMvcController {

    private final CustomerRequestService customerRequestService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "customerRequests",
                customerRequestService.findAllDtos());

        return "customerRequest/list";
    }

    @GetMapping("/{id}")
    public String details(
            @PathVariable Long id,
            Model model
    ) {

        CustomerRequestDto request =
                customerRequestService.findById(id)
                        .orElseThrow(() ->
                                new CustomerRequestNotFoundException(id));

        model.addAttribute("customerRequest", request);

        return "customerRequest/details";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute(
                "customerRequest",
                new CustomerRequestCreateDto(
                        "",
                        "",
                        RequestType.QUESTION,
                        null
                ));

        model.addAttribute("types", RequestType.values());

        return "customerRequest/create";
    }

    @PostMapping
    public String save(
            @ModelAttribute("customerRequest")
            CustomerRequestCreateDto dto
    ) {

        customerRequestService.createCustomerRequest(dto);

        return "redirect:/customerRequests";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        CustomerRequestDto request =
                customerRequestService.findById(id)
                        .orElseThrow(() ->
                                new CustomerRequestNotFoundException(id));

        model.addAttribute("customerRequest", request);
        model.addAttribute("channels", Channel.values());
        model.addAttribute("statuses", RequestStatus.values());
        model.addAttribute("types", RequestType.values());

        return "customerRequest/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("customerRequest")
            CustomerRequestDto dto
    ) {

        CustomerRequestUpdateDto updateDto =
                new CustomerRequestUpdateDto(
                        dto.getTitle(),
                        dto.getDescription(),
                        dto.getChannel(),
                        dto.getRequestStatus(),
                        dto.getRequestType(),
                        dto.getCustomerId()
                );

        customerRequestService.updateCustomerRequest(id, updateDto);

        return "redirect:/customerRequests";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id
    ) {

        customerRequestService.deleteByRequestId(id);

        return "redirect:/customerRequests";
    }
}