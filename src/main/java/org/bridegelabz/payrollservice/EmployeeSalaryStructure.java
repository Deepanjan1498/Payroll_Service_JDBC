package org.bridegelabz.payrollservice;

import java.util.Objects;

public class EmployeeSalaryStructure {
	public String name;
	public double salary;
	public EmployeeSalaryStructure(String name, double salary) {
		super();
		this.name = name;
		this.salary = salary;
	}
	@Override
	public int hashCode()
	{
		return Objects.hash(name,salary);
	}
}
