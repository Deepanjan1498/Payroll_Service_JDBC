package org.bridegelabz.payrollservice;

import java.util.ArrayList;
import java.util.List;

public class EmployeePayrollRestService {
	List<EmployeePayrollData> employeePayrollDataList;

	public EmployeePayrollRestService(List<EmployeePayrollData> employeeList) {
		employeePayrollDataList = new ArrayList<>(employeeList);
	}

	public long countRestEntries() {
		return employeePayrollDataList.size();
	}
}
