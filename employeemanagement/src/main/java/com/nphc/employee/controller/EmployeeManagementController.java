package com.nphc.employee.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.nphc.employee.entity.Employee;
import com.nphc.employee.message.GetAllEmployeeResponse;
import com.nphc.employee.message.ResponseMessage;
import com.nphc.employee.service.EmployeeManagementService;
import com.nphc.employee.util.CSVHelper;
import com.nphc.employee.util.ParameterValidator;




@RestController
@RequestMapping("/nphc/employee")
public class EmployeeManagementController {

	@Autowired
	EmployeeManagementService employeeManagementService;

	@PostMapping("/upload")
	public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";

		if (CSVHelper.hasCSVFormat(file)) {
			try {
				employeeManagementService.save(file);

				message = "Uploaded the file successfully: " + file.getOriginalFilename();
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Could not upload the file: " + file.getOriginalFilename() + "!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		}

		message = "Please upload a csv file!";
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message));
	}
	
	@GetMapping
	public ResponseEntity<GetAllEmployeeResponse> getAllEmployee() {
		try {
			List<Employee> userList = employeeManagementService.getAllEmployee();

			if (userList.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(new GetAllEmployeeResponse(userList), HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PostMapping
	public ResponseEntity<ResponseMessage> createOrUpdateEmployee(@RequestBody Employee employee) {
		String message = "";
		if(employee.getSalary() == 0.00) {
			message = "Invalid salary ";

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		if(!ParameterValidator.checkDateFormat(employee.getStartDate())) {
			message = "Invalid date ";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		Employee updated = employeeManagementService.createEmployee(employee);
		message = "New Employee Record Created: " + updated.getId();
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}
	
	@GetMapping("/{id}")
	public Employee getUserById(@PathVariable String id) throws Exception {
		Optional<Employee> employee = employeeManagementService.getEmployeeById(id);
		if (!employee.isPresent())
			throw new Exception("Could not find employee with id- " + id);

		return employee.get();
	}
	
	@PutMapping
	public ResponseEntity<ResponseMessage> updateEmployee(@Valid @RequestBody Employee employee) {
		String message = "";
		
		if(employee.getSalary() == 0.00) {
			message = "Invalid salary " ;

			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}

		if(!ParameterValidator.checkDateFormat(employee.getStartDate())) {
			message = "Invalid date ";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
	
		
		Optional<Employee> userOptional = employeeManagementService.getEmployeeById(employee.getId());
		if (!userOptional.isPresent()) {
			message = "No such employee - " + employee.getId();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}

		employeeManagementService.createEmployee(employee);
		message = "Successfully Updated : " + employee.getId();
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<ResponseMessage> deleteEmployeeById(@PathVariable("id") String id) {
		String message = "";
		Optional<Employee> userOptional = employeeManagementService.getEmployeeById(id);

		if (!userOptional.isPresent()) {
			message = "No such employee - " + id;
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		}
		
		employeeManagementService.deleteEmployeeById(id);

		message = "Successfully Deleted : " + id;
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
	}

}
