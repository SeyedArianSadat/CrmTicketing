package com.company.crmticketing.controller;

import com.company.crmticketing.dto.Ticket.TicketDto;
import com.company.crmticketing.service.TicketService;
import com.company.crmticketing.service.DepartmentService;
import com.company.crmticketing.service.SupportAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/ui/tickets")
@RequiredArgsConstructor
public class TicketUiController {

    private final TicketService ticketService;
    private final DepartmentService departmentService;
    private final SupportAgentService supportAgentService;


    @GetMapping
    public String listTickets(Model model) {
        List<TicketDto> tickets = ticketService.findAllDtos();
        model.addAttribute("tickets", tickets);
        return "tickets/list";   // templates/tickets/list.html
    }


    @GetMapping("/{id}")
    public String viewTicket(@PathVariable Long id, Model model) {
        return ticketService.findByIdWithAllDetails(id)
                .map(dto -> {
                    model.addAttribute("ticket", dto);
                    return "tickets/detail";
                })
                .orElse("redirect:/ui/tickets?error=notfound");
    }


    @GetMapping("/new")
    public String newTicketForm(Model model) {
        model.addAttribute("ticketDto", new TicketDto());
        model.addAttribute("departments", departmentService.findAllDtos());
        model.addAttribute("agents", supportAgentService.findAllDtos());
        return "tickets/form";
    }


    @PostMapping
    public String saveTicket(@ModelAttribute TicketDto ticketDto, RedirectAttributes ra) {
        try {
            ticketService.createTicket(ticketDto);
            ra.addFlashAttribute("successMessage", "تیکت با موفقیت ایجاد شد");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "خطا در ایجاد تیکت: " + e.getMessage());
        }
        return "redirect:/ui/tickets";
    }

    @PostMapping("/{id}")
    public String updateTicket(@PathVariable Long id, @ModelAttribute TicketDto updateDto, RedirectAttributes ra) {
        try {
            ticketService.updateTicket(id, updateDto);
            ra.addFlashAttribute("successMessage", "تیکت به‌روزرسانی شد");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "خطا در به‌روزرسانی: " + e.getMessage());
        }
        return "redirect:/ui/tickets/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ticketService.deleteByTicketId(id);
            ra.addFlashAttribute("successMessage", "تیکت حذف شد");
        } catch (Exception e) {
            ra.addFlashAttribute("errorMessage", "خطا در حذف: " + e.getMessage());
        }
        return "redirect:/ui/tickets";
    }
}