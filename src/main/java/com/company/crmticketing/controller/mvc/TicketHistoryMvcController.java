package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.ticketHistory.TicketHistoryCreateDto;
import com.company.crmticketing.dto.ticketHistory.TicketHistoryDto;
import com.company.crmticketing.dto.ticketHistory.TicketHistoryUpdateDto;
import com.company.crmticketing.service.TicketHistoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ticketHistories")
public class TicketHistoryMvcController {

    private final TicketHistoryService ticketHistoryService;

    // ===================== LIST =====================

    @GetMapping
    public String list(Model model) {

        model.addAttribute("ticketHistories",
                ticketHistoryService.findAllDtos());

        return "ticketHistory/list";
    }

    // ===================== DETAILS =====================

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {

        TicketHistoryDto dto = ticketHistoryService.findDtoById(id);

        model.addAttribute("ticketHistory", dto);

        return "ticketHistory/details";
    }

    // ===================== CREATE =====================

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("ticketHistory",
                new TicketHistoryCreateDto(
                        "",
                        "",
                        "",
                        null
                ));

        return "ticketHistory/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid
            @ModelAttribute("ticketHistory")
            TicketHistoryCreateDto dto,
            BindingResult result) {

        if (result.hasErrors()) {
            return "ticketHistory/create";
        }

        ticketHistoryService.createTicketHistory(dto);

        return "redirect:/ticketHistories";
    }

    // ===================== EDIT =====================

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id,
                           Model model) {

        TicketHistoryDto dto =
                ticketHistoryService.findDtoById(id);

        TicketHistoryUpdateDto updateDto =
                new TicketHistoryUpdateDto(
                        dto.getFieldChanged(),
                        dto.getOldValue(),
                        dto.getNewValue()
                );

        model.addAttribute("ticketHistoryId", id);
        model.addAttribute("ticketHistory", updateDto);

        return "ticketHistory/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid
            @ModelAttribute("ticketHistory")
            TicketHistoryUpdateDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {

            model.addAttribute("ticketHistoryId", id);

            return "ticketHistory/edit";
        }

        ticketHistoryService.updateTicketHistory(id, dto);

        return "redirect:/ticketHistories";
    }

    // ===================== DELETE =====================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        ticketHistoryService.deleteByTicketHistoryId(id);

        return "redirect:/ticketHistories";
    }
}