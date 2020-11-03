package org.bridegelabz.payrollservice;

import java.sql.Connection;
import java.sql.Date;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayrollServiceDB {
	private static PayrollServiceDB employeePayrollServiceDB;
	private PreparedStatement preparedStatementForUpdation;
	private PreparedStatement employeePayrollDataStatement;
	public PayrollServiceDB() {}

	public static PayrollServiceDB getInstance() {
		if(employeePayrollServiceDB==null) {
			employeePayrollServiceDB=new PayrollServiceDB();
		}
		return employeePayrollServiceDB;
	}
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
   		try (Connection connection = this.getConnection()) {
   			Statement statement = connection.createStatement();
   			ResultSet resultSet = statement.executeQuery(sql);
   			return this.getEmployeePayrollListFromResultset(resultSet);
   		}catch (SQLException e) {
   	   			throw new EmployeePayrollJDBCException("Unable to Retrieve data From Table!");
   	   		}
       }
       private List<EmployeePayrollData> getEmployeePayrollListFromResultset(ResultSet resultSet) throws EmployeePayrollJDBCException {
   		List<EmployeePayrollData> employeePayrollList = new ArrayList<EmployeePayrollData>();
   		try {
   			while (resultSet.next()) {
   				int id = resultSet.getInt("id");
   				String objectname = resultSet.getString("name");
   				String gender = resultSet.getString("gender");
   				double salary = resultSet.getDouble("salary");
   				LocalDate startDate = resultSet.getDate("start").toLocalDate();
   				employeePayrollList.add(new EmployeePayrollData(id, objectname,gender,salary,startDate));
   			}
   		return employeePayrollList;
       }catch (SQLException e) {
	   			throw new EmployeePayrollJDBCException("Unable to use the Result set!");
	   		}
       }
       public List<EmployeePayrollData> getEmployeePayrollDataFromDB(String name) throws EmployeePayrollJDBCException 
   	  {
    	   if (this.employeePayrollDataStatement == null) {
   			this.prepareStatementForEmployeePayrollDataRetrieval();
   		}
   		try (Connection connection = this.getConnection()) {
   			this.employeePayrollDataStatement.setString(1, name);
   			ResultSet resultSet = employeePayrollDataStatement.executeQuery();
   			return this.getEmployeePayrollListFromResultset(resultSet);
   		} catch (SQLException e) {
   			throw new EmployeePayrollJDBCException("Unable to read data");
   		}
      }
	public int updateEmployeeDataUsingStatement(String name, double salary) throws EmployeePayrollJDBCException  {
		// TODO Auto-generated method stub
		String sql=String.format("UPDATE employee_payroll SET salary=%.2f WHERE name='%s'",salary,name);
			try (Connection connection=this.getConnection()){
				Statement statement=connection.createStatement();
				int rowsAffected=statement.executeUpdate(sql);
				return rowsAffected;
			} catch (SQLException e) {
				throw new EmployeePayrollJDBCException("Unable To update data in database");
			}
	    }
	public int updateEmployeePayrollDataUsingPreparedStatement(String name, double salary) throws EmployeePayrollJDBCException {
		if(this.preparedStatementForUpdation==null) {
			this.prepareStatementForEmployeePayroll();
		}
		try {
			preparedStatementForUpdation.setDouble(1, salary);
			preparedStatementForUpdation.setString(2, name);
			int rowsAffected=preparedStatementForUpdation.executeUpdate();
			return rowsAffected;
		}catch(SQLException e) {
			throw new EmployeePayrollJDBCException("Unable to use prepared statement");
		}
	}

	private void prepareStatementForEmployeePayroll() throws EmployeePayrollJDBCException {
		try {
			Connection connection=this.getConnection();
			String sql="UPDATE employee_payroll SET salary=? WHERE name=?";
			this.preparedStatementForUpdation=connection.prepareStatement(sql);
		}catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Unable to prepare statement");
		}
	}
	private void prepareStatementForEmployeePayrollDataRetrieval() throws EmployeePayrollJDBCException {
		try {
			Connection connection = this.getConnection();
			String sql = "SELECT * FROM employee_payroll WHERE name=?";
			this.employeePayrollDataStatement = connection.prepareStatement(sql);
		} catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Unable to create prepare statement");
		}
	}
	public List<EmployeePayrollData> getEmployeePayrollDataByStartingDate(LocalDate startDate, LocalDate endDate)
			throws EmployeePayrollJDBCException {
		String sql = String.format("SELECT * FROM employee_payroll WHERE start BETWEEN cast('%s' as date) and cast('%s' as date);",
				startDate.toString(), endDate.toString());
		try (Connection connection = this.getConnection()) {
			Statement statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return this.getEmployeePayrollListFromResultset(resultSet);
		} catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Connection Failed.");
		}
	}
	public Map<String, Double> performAverageAndMinAndMaxOperations(String column, String operation) throws EmployeePayrollJDBCException{
		String sql=String.format("SELECT gender , %s(%s) FROM employee_payroll GROUP BY gender;" , operation , column);
		Map<String,Double> mapValues = new HashMap<>();
		try(Connection connection = this.getConnection()) {
			PreparedStatement preparedStatement = connection.prepareStatement(sql);
			ResultSet resultSet = preparedStatement.executeQuery();
			while(resultSet.next())
			{
				mapValues.put(resultSet.getString(1), resultSet.getDouble(2));
			}
		}
		catch (SQLException e) {
			throw new EmployeePayrollJDBCException("Connection Failed.");
		}
		return mapValues;
	}

	public EmployeePayrollData addEmployeeToPayroll(String name, String gender, double salary, LocalDate startDate) throws EmployeePayrollJDBCException {
		int employeeId = -1;
		EmployeePayrollData employeePayrollData = null;
		String sql = String.format("INSERT INTO employee_payroll(name,gender,salary,start) " +
		                           "VALUES ( '%s', '%s', %s, '%s' )", name,gender,salary, Date.valueOf(startDate));
		try (Connection connection=this.getConnection()){
			Statement statement=connection.createStatement();
			int rowsAffected=statement.executeUpdate(sql,statement.RETURN_GENERATED_KEYS);
            if(rowsAffected == 1) {
            	ResultSet resultSet = statement.getGeneratedKeys();
            	if(resultSet.next()) employeeId = resultSet.getInt(1);
            }
            employeePayrollData = new EmployeePayrollData(employeeId,name,gender,salary,startDate);
		} catch(SQLException e) {
			e.printStackTrace();
		}
            return employeePayrollData;
	}
}