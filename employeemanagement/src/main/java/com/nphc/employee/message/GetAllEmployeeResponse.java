package com.nphc.employee.message;

import java.util.List;

import com.nphc.employee.entity.Employee;


public class GetAllEmployeeResponse {
	
	List<Employee> results;
	public GetAllEmployeeResponse() {
		
	}

	public GetAllEmployeeResponse(List<Employee> results) {
		super();
		this.results = results;
	}

	public List<Employee> getResults() {
		return results;
	}

	public void setResults(List<Employee> results) {
		this.results = results;
	}

}
