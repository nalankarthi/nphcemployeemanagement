package com.nphc.employee.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nphc.employee.entity.Employee;

@Repository
public interface EmployeeManagementRepository  extends JpaRepository<Employee, String> {

}
