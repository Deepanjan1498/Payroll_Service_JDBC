package org.bridegelabz.payrollservice;

import java.util.List;


	public class EmployeePayrollService {
	public PayrollServiceDB payrollServiceDB;
		
		public EmployeePayrollService() {
		super();
		this.payrollServiceDB = new PayrollServiceDB();
	    }
		public List<EmployeePayrollData> readEmployeePayrollData() throws EmployeePayrollJDBCException{
			return this.payrollServiceDB.readData();
		}
	}