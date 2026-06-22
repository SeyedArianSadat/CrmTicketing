package com.company.crmticketing.controller;

import com.company.crmticketing.dto.department.DepartmentDto;
import com.company.crmticketing.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/ui/departments")
@RequiredArgsConstructor
public class DepartmentUiController {

    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute(
                "departments",
                departmentService.findAllDtos());

        return "departments/list";
    }

    @GetMapping("/new")
    public String form(Model model) {

        model.addAttribute(
                "department",
                new DepartmentDto());

        return "departments/form";
    }

    @PostMapping
    public String save(
            @ModelAttribute DepartmentDto dto) {

        departmentService.createDepartment(dto);

        return "redirect:/ui/departments";
    }
}
