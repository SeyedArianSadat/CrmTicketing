package com.company.crmticketing.controller.mvc;

import com.company.crmticketing.dto.department.DepartmentCreateDto;
import com.company.crmticketing.dto.department.DepartmentDto;
import com.company.crmticketing.dto.department.DepartmentUpdateDto;
import com.company.crmticketing.exception.DepartmentNotFoundException;
import com.company.crmticketing.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/departments")
public class DepartmentMvcController {

    private final DepartmentService departmentService;

    @GetMapping
    public String list(Model model) {

        model.addAttribute("departments",
                departmentService.findAllDtos());

        return "department/list";
    }

    @GetMapping("/{id}")
    public String details(
            @PathVariable Long id,
            Model model
    ) {

        DepartmentDto department = departmentService.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        model.addAttribute("department", department);

        return "department/details";
    }

    @GetMapping("/create")
    public String create(Model model) {

        model.addAttribute(
                "department",
                new DepartmentCreateDto("")
        );

        return "department/create";
    }

    @PostMapping
    public String save(
            @ModelAttribute("department")
            DepartmentCreateDto dto
    ) {

        departmentService.createDepartment(dto);

        return "redirect:/departments";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Long id,
            Model model
    ) {

        DepartmentDto department = departmentService.findById(id)
                .orElseThrow(() -> new DepartmentNotFoundException(id));

        model.addAttribute("department", department);

        return "department/edit";
    }

    @PostMapping("/edit/{id}")
    public String update(
            @PathVariable Long id,
            @ModelAttribute DepartmentUpdateDto dto
    ) {

        departmentService.updateDepartment(id, dto);

        return "redirect:/departments";
    }

    @GetMapping("/delete/{id}")
    public String delete(
            @PathVariable Long id
    ) {

        departmentService.deleteDepartmentById(id);

        return "redirect:/departments";
    }

}