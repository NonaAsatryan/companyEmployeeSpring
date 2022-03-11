package com.example.companyemployeespring.controller;

import com.example.companyemployeespring.entity.Company;
import com.example.companyemployeespring.entity.Employee;
import com.example.companyemployeespring.service.CompanyService;
import com.example.companyemployeespring.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final CompanyService companyService;

    @Value("${companyEmployee.upload.path}")
    private String imagePath;

    @GetMapping("/employees")
    public String employees(ModelMap map){
        List<Employee> employees = employeeService.findAll();
        map.addAttribute("employees",employees);
        return "employee";
    }
    @GetMapping("/employees/add")
    public String addEmployeePage(ModelMap map) {
        map.addAttribute("employees", employeeService.findAll());
        map.addAttribute("companies", companyService.findAll());
        return "addEmployee";
    }

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        InputStream inputStream = new FileInputStream(imagePath + picName);
        return IOUtils.toByteArray(inputStream);
    }

    @PostMapping("/employees/add")
    public String addEmployee(@ModelAttribute Employee employee, @RequestParam("picture") MultipartFile uploadedFile) throws IOException {
            employeeService.saveEmployeeImage(uploadedFile, employee);
        return "redirect:/employees";
    }

    @GetMapping("/employees/byCompany/{id}")
    public String employeesByCompanyPage(ModelMap map, @PathVariable("id") int id) {
        Company company = companyService.getById(id);
        List<Employee> employees = employeeService.findAllByCompany(company);
        map.addAttribute("employees", employees);
        return "employee";
    }
}
