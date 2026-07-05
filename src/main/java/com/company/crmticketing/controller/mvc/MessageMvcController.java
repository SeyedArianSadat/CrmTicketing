package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.message.MessageCreateDto;
import com.company.crmticketing.dto.message.MessageDto;
import com.company.crmticketing.dto.message.MessageUpdateDto;
import com.company.crmticketing.service.MessageService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/messages")
public class MessageMvcController {

    private final MessageService messageService;

    // ========================= LIST =========================

    @GetMapping
    public String list(Model model) {
        model.addAttribute("messages", messageService.findAllDtos());
        return "message/list";
    }

    // ========================= DETAIL =========================

    @GetMapping("/{id}")
    public String detail(@PathVariable Long id, Model model) {

        MessageDto message = messageService.findDtoById(id);

        model.addAttribute("message", message);

        return "message/details";
    }

    // ========================= CREATE =========================

    @GetMapping("/create")
    public String createForm(Model model) {

        model.addAttribute("message", new MessageCreateDto(
                "",
                false,
                null,
                null,
                null
        ));

        return "message/create";
    }

    @PostMapping("/create")
    public String create(
            @Valid @ModelAttribute("message") MessageCreateDto dto,
            BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "message/create";
        }

        messageService.createMessage(dto);

        return "redirect:/messages";
    }

    // ========================= EDIT =========================

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {

        MessageDto dto = messageService.findDtoById(id);

        MessageUpdateDto updateDto = new MessageUpdateDto(
                dto.getContent(),
                dto.isInternalNote(),
                dto.getRequestId(),
                dto.getTicketId(),
                dto.getSenderUserId()
        );

        model.addAttribute("messageId", id);
        model.addAttribute("message", updateDto);

        return "message/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            @Valid @ModelAttribute("message") MessageUpdateDto dto,
            BindingResult bindingResult,
            Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("messageId", id);
            return "message/edit";
        }

        messageService.updateMessage(id, dto);

        return "redirect:/messages";
    }

    // ========================= DELETE =========================

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {

        messageService.deleteMessageById(id);

        return "redirect:/messages";
    }
}