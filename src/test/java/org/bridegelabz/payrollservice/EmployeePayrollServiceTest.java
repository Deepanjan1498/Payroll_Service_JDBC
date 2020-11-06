package org.bridegelabz.payrollservice;

import static org.junit.Assert.*;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Arrays;
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
	    @Test
	    public void givenNewEmployeeForUC7_WhenAdded_ShouldSyncWithDB() throws EmployeePayrollJDBCException {
	    	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
	    	employeePayrollService.readEmployeePayrollData();
	    	employeePayrollService.addEmployeeToPayrollUC7("Mark","M", 5000000.00, LocalDate.now());
	        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
	        Assert.assertTrue(result);
	    }
	    @Test
	    public void givenNewEmployee_WhenAdded_ShouldSyncWithDB() throws EmployeePayrollJDBCException {
	    	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
	    	employeePayrollService.readEmployeePayrollData();
	    	employeePayrollService.addEmployeeToPayroll("Mark","M", 5000000.00, LocalDate.now());
	        boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Mark");
	        Assert.assertTrue(result);
	    }
	    @Test
	    public void givennewEmployeeDetails_addItInEveryTableToCompleteERDiagram_ShouldGiveResult() throws EmployeePayrollJDBCException{
	    	EmployeePayrollService employeePayrollService = new EmployeePayrollService();
			employeePayrollService.readEmployeePayrollData();
			Date date = Date.valueOf("2020-11-01");
			boolean result;
			String[] departments = {"SalesAndBusiness", "Marketing"};
			int[] dept_id = {01, 02};
			EmployeePayrollData employeePayrollData = employeePayrollService.addNewEmployee
						(101, "Sandeep", "M", "7896541235", "Bhopal", date, 6000000,
								"DBS", 11, departments, dept_id );
			boolean results=employeePayrollService.checkEmployeePayrollInSyncWithDB("Sandeep");
			Assert.assertTrue(results);
	    }
	    @Test
		public void givenNameWhenDeletedShouldGetDeletedFromDatabase() throws EmployeePayrollJDBCException {
				EmployeePayrollService employeePayrollService = new EmployeePayrollService();
				employeePayrollService.deleteEmployee("Terisa");
				boolean result = employeePayrollService.checkEmployeePayrollInSyncWithDB("Sandeep");
				assertFalse(result);
		}
	    @Test
	    public void given6Employeess_WhenAddedToDB_ShouldMatchEmployeeEntries() throws EmployeePayrollJDBCException{
	    	EmployeePayrollData[] aarayOfEmps= {
	    			new EmployeePayrollData(0,"Jeff Bezos", "M", 100000.0, LocalDate.now()),
	    			new EmployeePayrollData(0,"Bill Gates", "M", 1500000.0, LocalDate.now()),
	    			new EmployeePayrollData(0,"Mark Zuckerberg", "M", 1200000.0, LocalDate.now()),
	    			new EmployeePayrollData(0,"Suresh", "M", 900000.0, LocalDate.now()),
	    			new EmployeePayrollData(0,"Mukesh", "M", 700000.0, LocalDate.now()),
	    			new EmployeePayrollData(0,"Anil", "M", 800000.0, LocalDate.now()),
	    	};
	    	EmployeePayrollService employeePayrollService=new EmployeePayrollService();
	    	employeePayrollService.readEmployeePayrollData();
	    	Instant start=Instant.now();
	    	employeePayrollService.addEmployeesToPayroll(Arrays.asList(aarayOfEmps));
	    	Instant end=Instant.now();
	    	System.out.println("Duration Without Thread:"+java.time.Duration.between(start, end));
	    	Instant startThread=Instant.now();
	    	employeePayrollService.addEmployeesToPayrollWithThreads(Arrays.asList(aarayOfEmps));
	    	Instant endThread=Instant.now();
	    	System.out.println("Duration With Thread:"+java.time.Duration.between(startThread, endThread));
	    	Assert.assertEquals(7,employeePayrollService.countEntries());
	    }
	   
}






