package org.bridegelabz.payrollservice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PayrollServiceDB {
	    public Connection getConnection() throws EmployeePayrollJDBCException{
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = "Deepa@43";
		Connection connection;
		try {
			System.out.println("Connecting to database:"+jdbcURL);
			connection = DriverManager.getConnection(jdbcURL,userName,password);
			System.out.println("Connection is successful!"+connection);
			return connection;
		   }
		     catch(SQLException e) {
			throw new EmployeePayrollJDBCException("Wrong Entry!Unable to connect!");
		}
	}
       private static void listDrivers() {
    	   Enumeration<Driver> driverList = DriverManager.getDrivers();
    	   while(driverList.hasMoreElements()) {
    		   Driver driverClass = (Driver) driverList.nextElement();
    		   System.out.println("Driver:"+driverClass.getClass().getName());
    	   }
       }
       public List<EmployeePayrollData> readData() throws EmployeePayrollJDBCException{
   		String sql = "SELECT * FROM employee_payroll;";
   		List<EmployeePayrollData> employeePayrollList = new ArrayList<>();
   		try (Connection connection = this.getConnection()) {
   			Statement statement = connection.createStatement();
   			ResultSet resultSet = statement.executeQuery(sql);
   			while (resultSet.next()) {
   				int id = resultSet.getInt("id");
   				String name = resultSet.getString("name");
   				double salary = resultSet.getDouble("salary");
   				LocalDate startDate = resultSet.getDate("start").toLocalDate();
   				employeePayrollList.add(new EmployeePayrollData(id, name, salary,startDate));
   			}
   			connection.close();
   		} catch (SQLException e) {
   			throw new EmployeePayrollJDBCException("Unable to Retrieve data From Table!");
   		}
   		return employeePayrollList;
       }

       public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws EmployeePayrollJDBCException 
   	{
   			String sql=String.format("select * from employee_payroll where name='%s'",name);
   			List<EmployeePayrollData> employeePayrollList=new ArrayList<EmployeePayrollData>();
   			try (Connection connection=this.getConnection()){
   				Statement statement=connection.createStatement();
   				ResultSet resultSet=statement.executeQuery(sql);
   				while(resultSet.next()) {
   					int id=resultSet.getInt("id");
   					String objectname=resultSet.getString("name");
   					double salary=resultSet.getDouble("salary");
   					LocalDate start=resultSet.getDate("start").toLocalDate();
   					employeePayrollList.add(new EmployeePayrollData(id, objectname, salary,start));
   				}
   				return employeePayrollList;
   			} catch (SQLException e) {
   				throw new EmployeePayrollJDBCException("Unable to get data from database");
   			}
      }
	public int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollJDBCException  {
		// TODO Auto-generated method stub
		String sql=String.format("update employee_payroll set salary=%.2f where name='%s'",salary,name);
			try (Connection connection=this.getConnection()){
				Statement statement=connection.createStatement();
				int rowsAffected=statement.executeUpdate(sql);
				return rowsAffected;
			} catch (SQLException e) {
				throw new EmployeePayrollJDBCException("Unable To update data in database");
			}
	    }	
	}

