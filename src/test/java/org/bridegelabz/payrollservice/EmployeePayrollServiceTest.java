package org.bridegelabz.payrollservice;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

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
	
	@Test
	public void givenNewSalaryForEmployee_WhenUpdated_ShouldMatch()  throws EmployeePayrollJDBCException {
		EmployeePayrollService employeePayrollService = new EmployeePayrollService();
		List<EmployeePayrollData> employeePayrollData;
		employeePayrollData =employeePayrollService.readEmployeePayrollData();
		employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
		boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
		Assert.assertTrue(result);
	}
	 @Test
	    public void givenEmployeePayroll_WhenUpdatedUsingPreparedStatement_ShouldSyncWithDB() throws EmployeePayrollJDBCException
	    {
	    	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
	    	List<EmployeePayrollData> employeePayrollData;
	    	employeePayrollData =employeePayrollService.readEmployeePayrollData();
			employeePayrollService.updateEmployeeSalary("Terisa",3000000.00);
			boolean result=employeePayrollService.checkEmployeePayrollInSyncWithDB("Terisa");
			Assert.assertTrue(result);
	    }
	 
	    @Test
	    public void givenEmployeePayrollData_WhenRetrievedBasedOnStartDate_ShouldReturnResult() throws EmployeePayrollJDBCException {
				EmployeePayrollService employeePayrollService = new EmployeePayrollService();
				employeePayrollService.readEmployeePayrollData();
				LocalDate startDate = LocalDate.parse("2018-01-01");
				LocalDate endDate = LocalDate.parse("2020-01-31");
				List<EmployeePayrollData> matchingRecords = employeePayrollService
						.getEmployeePayrollDataByStartDate(startDate, endDate);
				Assert.assertEquals(matchingRecords.get(0),employeePayrollService.getEmployeePayrollData("Bill"));
		}
	    @Test
	    public void givenEmployee_PerformedVariousOperations_ShouldGiveResult() throws EmployeePayrollJDBCException {
			EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			employeePayrollService.readEmployeePayrollData();
			Map<String, Double> averageSalaryByGender=employeePayrollService.performOperationByGender("salary","MAX");
			assertEquals(3000000.0,averageSalaryByGender.get("F"), 0.0);
	}
}

