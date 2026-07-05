package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.sla.SlaCreateDto;
import com.company.crmticketing.dto.sla.SlaDto;
import com.company.crmticketing.dto.sla.SlaUpdateDto;
import com.company.crmticketing.service.SlaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/slas")
public class SlaMvcController {

    private final SlaService slaService;

    // ================= LIST =================

    @GetMapping
    public String list(Model model) {
        model.addAttribute("slas", slaService.findAllDtos());
        return "sla/list";
    }

    // ================= DETAILS =================

    @GetMapping("/{id}")
    public String details(@PathVariable Long id, Model model) {

        SlaDto sla = slaService.findDtoById(id);

        model.addAttribute("sla", sla);

        return "sla/details";
    }

    // ================= CREATE =================

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("sla",
                new SlaCreateDto(
                        null,
                        1,
                        1,
                        ""
                ));

        return "sla/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("sla") SlaCreateDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "sla/create";
        }

        slaService.createSla(dto);

        return "redirect:/slas";
    }

    // ================= EDIT =================

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        SlaDto dto = slaService.findDtoById(id);

        SlaUpdateDto updateDto = new SlaUpdateDto(
                dto.getPriorityLevel(),
                dto.getResponseTimeMinutes(),
                dto.getResolutionTimeMinutes(),
                dto.getDescription()
        );

        model.addAttribute("slaId", id);
        model.addAttribute("sla", updateDto);

        return "sla/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("sla") SlaUpdateDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("slaId", id);
            return "sla/edit";
        }

        slaService.updateSla(id, dto);

        return "redirect:/slas";
    }

    // ================= DELETE =================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        slaService.deleteBySlaId(id);

        return "redirect:/slas";
    }
}