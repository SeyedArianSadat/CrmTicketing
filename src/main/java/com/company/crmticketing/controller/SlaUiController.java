package com.company.crmticketing.controller;

import com.company.crmticketing.dto.sla.SlaDto;
import com.company.crmticketing.service.SlaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/slas")
@RequiredArgsConstructor
public class SlaUiController {

    private final SlaService service;

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "slas",
                service.findAllDtos());

        return "slas/list";
    }

    @GetMapping("/new")
    public String form(Model model) {

        model.addAttribute(
                "sla",
                new SlaDto());

        return "slas/form";
    }
}
