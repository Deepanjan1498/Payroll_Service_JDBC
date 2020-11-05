package org.bridegelabz.payrollservice;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

		public EmployeePayrollData getEmployeePayrollData(String name) {
			return this.employeePayrollList.stream()
					.filter(employeePayrollObject->employeePayrollObject.getName().equals(name))
					.findFirst().orElse(null);
		}

		public boolean checkEmployeePayrollInSyncWithDB(String name) throws EmployeePayrollJDBCException {
			List<EmployeePayrollData> employeePayrollDataList=new PayrollServiceDB().getEmployeePayrollDataFromDB(name);
			return employeePayrollDataList.get(0).equals(getEmployeePayrollData(name));
		}
		public List<EmployeePayrollData> getEmployeePayrollDataByStartDate(LocalDate startDate, LocalDate endDate)throws EmployeePayrollJDBCException {
			return this.payrollServiceDB.getEmployeePayrollDataByStartingDate(startDate, endDate);
		}

		public Map<String, Double> performOperationByGender(String column,String operation) throws EmployeePayrollJDBCException {
			return this.payrollServiceDB.performAverageAndMinAndMaxOperations(column,operation);
		}
		public void addEmployeeToPayrollUC7(String name, String gender, double salary, LocalDate startDate) throws EmployeePayrollJDBCException {
			employeePayrollList.add(payrollServiceDB.addEmployeeToPayroll(name,gender,salary,startDate));
			
		}
		public void addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate) throws EmployeePayrollJDBCException {
			employeePayrollList.add(payrollServiceDB.addEmployeeToPayroll(name,gender,salary,startDate));
			
		}
		public EmployeePayrollData addNewEmployee(int id, String name, String gender, String phone_no, String address,Date date, double salary, String comp_name, int comp_id, String[] department, int[] dept_id) throws EmployeePayrollJDBCException{
			return PayrollServiceDB.getInstance().addNewEmployee
									(id, name, gender, phone_no, address, date, salary, comp_name, comp_id, department, dept_id);
		}
		public void deleteEmployee(String name) throws EmployeePayrollJDBCException {
			if (!this.checkEmployeePayrollInSyncWithDB(name))
				throw new EmployeePayrollJDBCException("employee absent");
			PayrollServiceDB.getInstance().deleteEmployee(name);
		}
		public void addEmployeesToPayroll(List<EmployeePayrollData> employeePayrollDataList) throws EmployeePayrollJDBCException{
			employeePayrollDataList.forEach(employeePayrollData->{
				System.out.println("Employee Being Added:"+employeePayrollData.getName());
				try {
					this.addEmployeeToPayroll(employeePayrollData.getName(),employeePayrollData.getGender(),employeePayrollData.getSalary(),employeePayrollData.getStartDate());
				} catch (EmployeePayrollJDBCException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("Employee Added:"+employeePayrollData.getName());
			});
			System.out.println(this.employeePayrollList);
		}

		public int countEntries() {
			return employeePayrollList.size();
		}
		
}
	