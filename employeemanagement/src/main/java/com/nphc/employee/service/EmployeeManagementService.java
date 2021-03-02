package com.nphc.employee.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.nphc.employee.entity.Employee;
import com.nphc.employee.repository.EmployeeManagementRepository;
import com.nphc.employee.util.CSVHelper;

@Service
public class EmployeeManagementService {

	@Autowired
	EmployeeManagementRepository employeeRepository;

	public void save(MultipartFile file) {
		try {
			List<Employee> tutorials = CSVHelper.csvToUsers(file.getInputStream());
			employeeRepository.saveAll(tutorials);
		} catch (IOException e) {
			throw new RuntimeException("fail to store csv data: " + e.getMessage());
		}
	}

	public List<Employee> getAllEmployee() {
		return employeeRepository.findAll();
	}

	public Employee createEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public Optional<Employee> getEmployeeById(String id) {
		return employeeRepository.findById(id);
	}

	public void deleteEmployeeById(String id) {
		employeeRepository.deleteById(id);
		
	}
}
