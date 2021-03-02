package com.nphc;

import static org.mockito.Mockito.doReturn;
import static org.mockito.ArgumentMatchers.any;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.nphc.employee.entity.Employee;
import com.nphc.employee.repository.EmployeeManagementRepository;
import com.nphc.employee.service.EmployeeManagementService;


@SpringBootTest
public class EmployeeManagementSeviceTest {

	@Autowired
	private EmployeeManagementService service;

	/**
	 * Create a mock implementation of the EmployeeRepository
	 */
	@MockBean
	private EmployeeManagementRepository repository;

	@Test
	@DisplayName("Test findById Success")
	void testFindById() {
		Employee employee1 = new Employee("e1001", "adam", "Gilly", 10.00, "12/01/2021");
		doReturn(Optional.of(employee1)).when(repository).findById("e1001");

		Optional<Employee> EmployeeOptional = service.getEmployeeById("e1001");

		Assertions.assertTrue(EmployeeOptional.isPresent(), "Employee was not found");
		Assertions.assertSame(EmployeeOptional.get(), employee1, "The Employee returned was not the same as the mock");
	}

	@Test
	@DisplayName("Test findById Not Found")
	void testFindByIdNotFound() {
		doReturn(Optional.empty()).when(repository).findById("e1001");

		Optional<Employee> employee1 = service.getEmployeeById("e100991");

		Assertions.assertFalse(employee1.isPresent(), "Employee should not be found");
	}

	@Test
	@DisplayName("Test save Employee")
	void testSave() {
		Employee employee1 = new Employee("e1001", "adam", "Gilly", 10.00, "12/01/2021");
		doReturn(employee1).when(repository).save(any());

		Employee savedEmployee = service.createEmployee(employee1);

		Assertions.assertNotNull(savedEmployee, "The saved Employee should not be null");
	}

	@Test
	@DisplayName("Test findAll")
	void testFindAll() {
		Employee employee1 = new Employee("e1001", "adam", "Gilly", 10.00, "12/01/2021");
		Employee employee2 = new Employee("e1002", "Harry", "Potter", 10.00, "10/01/2021");
		doReturn(Arrays.asList(employee1, employee2)).when(repository).findAll();

		List<Employee> Employees = service.getAllEmployee();

		Assertions.assertEquals(2, Employees.size(), "findAll should return 2 Employees");
	}
}
