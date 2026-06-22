package com.company.crmticketing.controller;

import com.company.crmticketing.dto.supportAgent.SupportAgentDto;
import com.company.crmticketing.service.DepartmentService;
import com.company.crmticketing.service.SupportAgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/agents")
@RequiredArgsConstructor
public class SupportAgentUiController {

    private final SupportAgentService service;
    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "agents",
                service.findAllDtos());

        return "agents/list";
    }

    @GetMapping("/new")
    public String form(Model model) {

        model.addAttribute(
                "agent",
                new SupportAgentDto());

        model.addAttribute(
                "departments",
                departmentService.findAllDtos());

        return "agents/form";
    }
}
