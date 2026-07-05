package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.supportAgent.SupportAgentCreateDto;
import com.company.crmticketing.dto.supportAgent.SupportAgentDto;
import com.company.crmticketing.dto.supportAgent.SupportAgentUpdateDto;
import com.company.crmticketing.exception.SupportAgentNotFoundException;
import com.company.crmticketing.service.SupportAgentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Controller
@RequiredArgsConstructor
@RequestMapping("/agents")
public class SupportAgentMvcController {

    private final SupportAgentService supportAgentService;


    @GetMapping
    public String list(Model model) {

        model.addAttribute("agents",
                supportAgentService.findAllDtos());

        return "supportAgent/list";
    }

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {

        SupportAgentDto agent = supportAgentService.findById(id)
                .orElseThrow(() -> new SupportAgentNotFoundException(id));

        model.addAttribute("agent", agent);

        return "supportAgent/details";

    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute(
                "agent",
                new SupportAgentCreateDto(
                        "",
                        null,
                        null
                )
        );

        return "supportAgent/create";
    }

    @PostMapping
    public String save(
            HttpServletRequest request,
            @ModelAttribute("agent") SupportAgentCreateDto dto
    ) {

        request.getParameterMap().forEach((k,v) ->
                System.out.println(k + " = " + Arrays.toString(v)));

        System.out.println(dto);

        supportAgentService.createAgent(dto);

        return "redirect:/agents";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        SupportAgentDto agent = supportAgentService.findById(id)
                .orElseThrow(() -> new SupportAgentNotFoundException(id));

        model.addAttribute("agent", agent);

        return "supportAgent/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute("agent") SupportAgentDto dto
    ) {

        supportAgentService.updateAgent(id, dto);

        return "redirect:/agents";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        supportAgentService.deleteAgentById(id);

        return "redirect:/agents";
    }

}