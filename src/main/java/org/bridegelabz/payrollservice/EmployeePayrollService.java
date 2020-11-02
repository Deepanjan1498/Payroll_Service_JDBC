package org.bridegelabz.payrollservice;

import java.util.List;


	public class EmployeePayrollService {
	public PayrollServiceDB payrollServiceDB;
	public List<EmployeePayrollData> employeePayrollList;
		
		public EmployeePayrollService() {
		super();
		this.payrollServiceDB = new PayrollServiceDB();
	    }
		public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollJDBCException{
			this.employeePayrollList = this.payrollServiceDB.readData();
			return this.employeePayrollList;
		}
		public void updateEmployeeSalary(String name,double salary) throws EmployeePayrollJDBCException
		{
			int result=new PayrollServiceDB().updateEmployeePayrollDataUsingPreparedStatement(name,salary);
			if(result==0)
				return;
			EmployeePayrollData employeePayrollData=this.getEmployeePayrollData(name);
			if(employeePayrollData !=null) employeePayrollData.setSalary(salary);
		}

		private EmployeePayrollData getEmployeePayrollData(String name) {
			return this.employeePayrollList.stream()
					.filter(employeePayrollObject->employeePayrollObject.getName().equals(name))
					.findFirst().orElse(null);
		}

		public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollJDBCException {
			List<EmployeePayrollData> employeePayrollDataList=new PayrollServiceDB().getEmployeePayrollDataFromDB(name);
			return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
		}
	}
	
	