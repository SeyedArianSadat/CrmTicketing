package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardMvcController {

    private final CustomerRepository customerRepository;
    private final DepartmentRepository departmentRepository;
    private final TicketRepository ticketRepository;
    private final MessageRepository messageRepository;
    private final AttachmentRepository attachmentRepository;
    private final TicketHistoryRepository ticketHistoryRepository;
    private final SupportAgentRepository supportAgentRepository;
    private final CustomerRequestRepository customerRequestRepository;
    private final SlaRepository slaRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {

        model.addAttribute("customerCount",
                customerRepository.count());

        model.addAttribute("departmentCount",
                departmentRepository.count());

        model.addAttribute("ticketCount",
                ticketRepository.count());

        model.addAttribute("messageCount",
                messageRepository.count());

        model.addAttribute("attachmentCount",
                attachmentRepository.count());

        model.addAttribute("ticketHistoryCount",
                ticketHistoryRepository.count());

        model.addAttribute("supportAgentCount",
                supportAgentRepository.count());

        model.addAttribute("customerRequestCount",
                customerRequestRepository.count());

        model.addAttribute("slaCount",
                slaRepository.count());

        return "dashboard";
    }

}
