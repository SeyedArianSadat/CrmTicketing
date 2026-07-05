package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.ticket.TicketCreateDto;
import com.company.crmticketing.dto.ticket.TicketDto;
import com.company.crmticketing.dto.ticket.TicketUpdateDto;
import com.company.crmticketing.exception.TicketNotFoundException;
import com.company.crmticketing.service.DepartmentService;
import com.company.crmticketing.service.SlaService;
import com.company.crmticketing.service.SupportAgentService;
import com.company.crmticketing.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/tickets")
public class TicketMvcController {

    private final TicketService ticketService;
    private final DepartmentService departmentService;
    private final SupportAgentService supportAgentService;
    private final SlaService slaService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("tickets",
                ticketService.findAllDtos());

        return "ticket/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id,
                          Model model) {

        TicketDto ticket = ticketService.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        model.addAttribute("ticket", ticket);

        return "detail";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute("ticket",
                new TicketCreateDto(
                        "",
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                ));

        model.addAttribute("departments",
                departmentService.findAllDtos());

        model.addAttribute("agents",
                supportAgentService.findAllDtos());

        model.addAttribute("slas",
                slaService.findAllDtos());

        return "ticket/create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute TicketCreateDto dto) {

        ticketService.createTicket(dto);

        return "redirect:/tickets";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id,
                       Model model) {

        TicketDto ticket = ticketService.findById(id)
                .orElseThrow(() -> new TicketNotFoundException(id));

        model.addAttribute("ticket", ticket);

        model.addAttribute("departments",
                departmentService.findAllDtos());

        model.addAttribute("agents",
                supportAgentService.findAllDtos());

        model.addAttribute("slas",
                slaService.findAllDtos());

        return "ticket/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute TicketUpdateDto dto) {

        ticketService.updateTicket(id, dto);

        return "redirect:/tickets";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        ticketService.deleteByTicketId(id);

        return "redirect:/tickets";
    }

}