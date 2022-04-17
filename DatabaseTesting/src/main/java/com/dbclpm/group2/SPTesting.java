package com.dbclpm.group2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class SPTesting {
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	
	@BeforeClass
	public void setup() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "sapassword");
	}
	
	@AfterClass
	public void shutdown() throws SQLException {
		con.close();
	}
	
	@Test(priority = 1)
	public void test_storedProceduresSelectAllCustomerExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='SelectAllCustomer'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "SelectAllCustomer");
	}
	
	@Test(priority = 1)
	public void test_storedProceduresSelectAllCustomerByCityExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='SelectAllCustomerByCity'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "SelectAllCustomerByCity");
	}
}
