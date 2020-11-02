package org.bridegelabz.payrollservice;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class EmployeePayrollServiceTest {

	@Test
	public void givenEmployeePayrollInDB_WhenRetrieved_ShouldMatchEmployeeCount() throws EmployeePayrollJDBCException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData;
		employeePayrollData = employeePayrollService.readEmployeePayrollData();
		Assert.assertEquals(3,employeePayrollData.size());
	}

}
