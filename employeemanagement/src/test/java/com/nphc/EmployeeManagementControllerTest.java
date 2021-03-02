package com.nphc;



import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.nphc.employee.entity.Employee;

import org.junit.jupiter.api.Test;

@SpringBootTest(webEnvironment= WebEnvironment.RANDOM_PORT)
public class EmployeeManagementControllerTest  {
	@LocalServerPort
    int randomServerPort;
	
	
	 @Test
	    public void testAddEmployeeSuccess() throws URISyntaxException 
	    {
	        RestTemplate restTemplate = new RestTemplate();
	        final String baseUrl = "http://localhost:"+randomServerPort+"/nphc/employee";
	        URI uri = new URI(baseUrl);
	        Employee employee = new Employee("e1001", "adam", "Gilly", 10.00,"12/01/2021");
	         
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("X-COM-PERSIST", "true");      
	 
	        HttpEntity<Employee> request = new HttpEntity<>(employee, headers);
	         
	        ResponseEntity<String> result = restTemplate.postForEntity(uri, request, String.class);
	         
	        //Verify request succeed
	        Assertions.assertEquals(200, result.getStatusCodeValue());
	    }
	 @Test
	    public void testGetEmployeeListSuccessWithHeaders() throws URISyntaxException 
	    {
	        RestTemplate restTemplate = new RestTemplate();
	         
	        final String baseUrl = "http://localhost:"+randomServerPort+"/nphc/employee";
	        URI uri = new URI(baseUrl);
	         
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("X-COM-LOCATION", "USA");
	        Employee employee1 = new Employee("e1001", "adam", "Gilly", 10.00,"12/01/2021");
	        Employee employee2 = new Employee("e1002", "Gilly", "Adam", 10.00,"18/01/2021");
	        Employee employee3 = new Employee("e1003", "James", "Adam", 10.00,"17/01/2021");
	        List<Employee> lstEmployee = new ArrayList<>();
	        lstEmployee.add(employee1);
	        lstEmployee.add(employee2);
	        lstEmployee.add(employee3);

	        HttpEntity<List<Employee>> requestEntity = new HttpEntity<>(lstEmployee, headers);
	 
	        try
	        {
	        	ResponseEntity<String> result = restTemplate.exchange(uri, HttpMethod.GET, requestEntity, String.class);
	        	Assertions.assertEquals(204, result.getStatusCodeValue());

	        }
	        catch(HttpClientErrorException ex) 
	        {
	            //Verify bad request and missing header
	            Assertions.assertEquals(400, ex.getRawStatusCode());
	            Assertions.assertEquals(true, ex.getResponseBodyAsString().contains("Missing request header"));
	        }
	    }
	  
	
}
