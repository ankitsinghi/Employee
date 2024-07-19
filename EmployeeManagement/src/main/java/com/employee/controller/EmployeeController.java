package com.employee.controller;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.employee.entity.EmpSalary;
import com.employee.entity.Employee;
import com.employee.service.EmployeeService;

@RestController
@RequestMapping("/employeeApi")
public class EmployeeController {

	private EmployeeService employeeService;
	private static final DecimalFormat df= new DecimalFormat("0.00"); 
	
	@Autowired
	public EmployeeController(EmployeeService employeeService) {
		this.employeeService = employeeService;
	}
	
	@PostMapping("/add")
    public ResponseEntity<Employee> saveProduct(@RequestBody Employee employee) {
		Employee newEmp = employeeService.saveEmployee(employee);
        return ResponseEntity.ok(newEmp);
    }
	
	@GetMapping
	public List<Employee> getEmployee() {
		return employeeService.getAllEmployees();
	}
	
	@GetMapping("/getSalary/{id}")
	public EmpSalary getEmployeeById(@PathVariable Integer id) {
		Employee e = employeeService.getEmployeeById(id).get();
		EmpSalary empSal = new EmpSalary();
		empSal.setId(e.getEmployeeId());
		empSal.setfName(e.getFirstName());
		empSal.setlName(e.getLastName());
		Date date = e.getDoj();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		int sal = Integer.parseInt(e.getSalary());
		int remDays = 0;
		int month = cal.get(Calendar.MONTH)+1;
		int year = cal.get(Calendar.YEAR);
		double annualSalary = sal*12;
		if(year == 2024 && month >= 4) {
			remDays = 30 - cal.get(Calendar.DATE) + 1;
			annualSalary = (sal * (15 - month)) + ((sal/30) * remDays);
		}
		double tax = 0;
		double cess = 0;
		if(annualSalary <= 250000) {
			tax = 0;
		} else if(annualSalary <= 500000) {
			tax = (annualSalary - 250000)*0.05;
		} else if(annualSalary <= 1000000) {
			tax = 12500 + ((annualSalary-500000)*0.1);
		} else {
			if(annualSalary > 2500000) {
				cess = (annualSalary - 2500000)*0.02;
			}
			tax = 62500 + ((annualSalary-1000000)*0.2);
		}
		empSal.setAnnualSal(df.format(annualSalary));
		empSal.setCess(df.format(cess));
		empSal.setTaxAmt(df.format(tax));
		
		return empSal;
		
	}
	
	
	
}
