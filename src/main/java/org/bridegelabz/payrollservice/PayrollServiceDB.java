package org.bridegelabz.payrollservice;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.util.Enumeration;

public class PayrollServiceDB {
       public static void main(String[] args) {
		String jdbcURL = "jdbc:mysql://localhost:3306/payroll_service?useSSL=false";
		String userName = "root";
		String password = " ";
		Connection connection;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.println("Driver loaded!");
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Cannot find the driver in the classpath!",e);
		}
		
		listDrivers();
		
		try {
			System.out.println("Connecting to database:"+jdbcURL);
			connection = DriverManager.getConnection(jdbcURL,userName,password);
			System.out.println("Connection is successful!"+connection);
		   }
		catch(Exception e) {
			e.printStackTrace();
		}
	}
       private static void listDrivers() {
    	   Enumeration<Driver> driverList = DriverManager.getDrivers();
    	   while(driverList.hasMoreElements()) {
    		   Driver driverClass = (Driver) driverList.nextElement();
    		   System.out.println("Driver:"+driverClass.getClass().getName());
    	   }
       }
}
