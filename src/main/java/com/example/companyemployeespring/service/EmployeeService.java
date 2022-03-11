package com.example.companyemployeespring.service;

import com.example.companyemployeespring.entity.Company;
import com.example.companyemployeespring.entity.Employee;
import com.example.companyemployeespring.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Value("${companyEmployee.upload.path}")
    public String imagePath;


    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public List<Employee> findAllByCompany(Company company) {
        return employeeRepository.findAllByCompany(company);
    }

    public void saveEmployeeImage(MultipartFile uploadedFile, Employee employee) throws IOException {
        if (!uploadedFile.isEmpty()) {
            String fileName = System.currentTimeMillis() + "_" + uploadedFile.getOriginalFilename();
            File newFile = new File(imagePath + fileName);
            uploadedFile.transferTo(newFile);
            employee.setPicUrl(fileName);
        }

        employeeRepository.save(employee);
    }

}
