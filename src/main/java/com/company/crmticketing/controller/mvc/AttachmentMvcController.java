package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.attachment.AttachmentCreateDto;
import com.company.crmticketing.dto.attachment.AttachmentDto;
import com.company.crmticketing.dto.attachment.AttachmentUpdateDto;
import com.company.crmticketing.service.AttachmentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttachmentMvcController {

    private final AttachmentService attachmentService;

    // ======================= LIST =======================

    @GetMapping
    public String list(Model model) {

        model.addAttribute("attachments", attachmentService.findAllDtos());

        return "attachment/list";
    }

    // ======================= DETAIL =======================

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {

        AttachmentDto attachment = attachmentService.findDtoById(id);

        model.addAttribute("attachment", attachment);

        return "attachment/details";
    }

    // ======================= CREATE =======================

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("attachment",
                new AttachmentCreateDto(
                        "",
                        "",
                        null
                ));

        return "attachment/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("attachment") AttachmentCreateDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "attachment/create";
        }

        attachmentService.createAttachment(dto);

        return "redirect:/attachments";
    }

    // ======================= EDIT =======================

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        AttachmentDto dto = attachmentService.findDtoById(id);

        AttachmentUpdateDto updateDto =
                new AttachmentUpdateDto(
                        dto.getFileName(),
                        dto.getFilePath(),
                        dto.getTicketId()
                );

        model.addAttribute("attachmentId", id);
        model.addAttribute("attachment", updateDto);

        return "attachment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("attachment") AttachmentUpdateDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("attachmentId", id);
            return "attachment/edit";
        }

        attachmentService.updateAttachment(id, dto);

        return "redirect:/attachments";
    }

    // ======================= DELETE =======================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        attachmentService.deleteByAttachmentId(id);

        return "redirect:/attachments";
    }
}