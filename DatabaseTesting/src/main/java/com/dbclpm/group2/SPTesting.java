package com.dbclpm.group2;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;

import org.apache.commons.lang3.StringUtils;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * 
 * @author Group 2
 * Syntax								Store Procedures
 * { call procedure_name() }		Accept no parameters and return no value
 * { call procedure_name(?,?) }		Accept two parameters and return no value
 * {?= call procedure_name() }		Accept no parameters and return value
 * {?= call procedure_name(?) }		Accept one parameters and return value
 */

public class SPTesting {
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	CallableStatement cStmt = null;
	ResultSet rs1, rs2;
	
	@BeforeClass
	public void setup() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels", "root", "sapassword");
	}
	
	@AfterClass
	public void shutdown() throws SQLException {
		con.close();
	}
	
	// =========== TEST EXIST ===========
	@Test(priority = 1)
	public void test_storedProceduresSelectAllCustomerExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='SelectAllCustomer'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "SelectAllCustomer");
	}
	
	@Test(priority = 2)
	public void test_storedProceduresSelectAllCustomerByCityExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='SelectAllCustomerByCity'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "SelectAllCustomerByCity");
	}
	
	@Test(priority = 3)
	public void test_storedProceduresSelectAllCustomerByCityAndPinExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='SelectAllCustomerByCityAndPin'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "SelectAllCustomerByCityAndPin");
	}
	
	@Test(priority = 4)
	public void test_storedProceduresGetOrderByCustExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='GetOrderByCust'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "GetOrderByCust");
	}
	
	@Test(priority = 5)
	public void test_storedProceduresGetCustomerShippingExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='GetCustomerShipping'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "GetCustomerShipping");
	}
	
	@Test(priority = 6)
	public void test_storedProceduresInsertSupplierProductExists() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show procedure status where Name='InsertSupplierProduct'");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "InsertSupplierProduct");
	}
	
	// =========== TEST RESULT OF PROCEDURE ===========

	@Test(priority = 7)
	public void test_SelectAllCustomers() throws SQLException {
		cStmt = con.prepareCall("{call SelectAllCustomer()}");
		rs1 = cStmt.executeQuery();
		
		Statement stmt = con.createStatement();
		rs2 = stmt.executeQuery("select * from customers");
		
		Assert.assertEquals(compareResultSet(rs1, rs2), true);
	}
	
	@Test(priority = 8)
	public void test_SelectAllCustomerByCity() throws SQLException {
		cStmt = con.prepareCall("{call SelectAllCustomerByCity(?)}");
		cStmt.setString(1, "Singapore");
		rs1 = cStmt.executeQuery();
		
		Statement stmt = con.createStatement();
		rs2 = stmt.executeQuery("select * from customers where city='Singapore'");
		
		Assert.assertEquals(compareResultSet(rs1, rs2), true);
	}
	
	@Test(priority = 9)
	public void test_SelectAllCustomerByCityAndPin() throws SQLException {
		cStmt = con.prepareCall("{call SelectAllCustomerByCityAndPin(?,?)}");
		cStmt.setString(1, "Singapore");
		cStmt.setString(2, "079903");
		rs1 = cStmt.executeQuery();
		
		Statement stmt = con.createStatement();
		rs2 = stmt.executeQuery("select * from customers where city='Singapore' and postalCode='079903'");
		
		Assert.assertEquals(compareResultSet(rs1, rs2), true);
	}

	@Test(priority = 10)
	public void test_GetOrderByCust() throws SQLException {
		cStmt = con.prepareCall("{call GetOrderByCust(?,?,?,?,?)}");		
		cStmt.setInt(1, 141);
		
		cStmt.registerOutParameter(2, Types.INTEGER);
		cStmt.registerOutParameter(3, Types.INTEGER);
		cStmt.registerOutParameter(4, Types.INTEGER);
		cStmt.registerOutParameter(5, Types.INTEGER);
		cStmt.executeQuery();
		
		int shipped = cStmt.getInt(2);
		int canceled = cStmt.getInt(3);
		int resolved = cStmt.getInt(4);
		int disputed = cStmt.getInt(5);
		
		//System.out.println(shipped + " " + canceled + " " + resolved + " " + disputed);
		
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("select \r\n"
				+ "(select count(*) as 'shipped' from orders where customerNumber=141 and status='Shipped') as Shipped,\r\n"
				+ "(select count(*) as 'canceled' from orders where customerNumber=141 and status='Canceled') as Canceled,\r\n"
				+ "(select count(*) as 'resolved' from orders where customerNumber=141 and status='Resolved') as Resolved,\r\n"
				+ "(select count(*) as 'disputed' from orders where customerNumber=141 and status='Disputed') as Disputed;");
		rs.next();
		
		int exp_shipped = rs.getInt("shipped");
		int exp_canceled = rs.getInt("canceled");
		int exp_resolved = rs.getInt("resolved");
		int exp_disputed = rs.getInt("disputed");
		
		if(shipped == exp_shipped && canceled == exp_canceled && resolved == exp_resolved && disputed == exp_disputed) {
			Assert.assertTrue(true);
		}else {
			Assert.assertTrue(false);
		}
	}
	
	@Test(priority = 11)
	public void test_GetCustomerShipping() throws SQLException {
		cStmt = con.prepareCall("{call GetCustomerShipping(?,?)}");
		cStmt.setInt(1, 112);
		cStmt.registerOutParameter(2, Types.VARCHAR);
		cStmt.executeQuery();
		
		String pShipping = cStmt.getString(2);
		
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("select country,\r\n"
				+ "case\r\n"
				+ "when country='USA' then '2-day Shipping'\r\n"
				+ "     when country='Canada' then '3-day Shipping'\r\n"
				+ "     else '5-day Shipping'\r\n"
				+ "end as pShipping\r\n"
				+ "  from customers where customerNumber=112;");
		rs.next();
		
		String exp_pShipping = rs.getString("pShipping");
		
		Assert.assertEquals(pShipping, exp_pShipping);
	}
	
	// =========== JAVA METHOD ===========
	public boolean compareResultSet(ResultSet resultSet1, ResultSet resultSet2) throws SQLException {
		while(resultSet1.next()) {
			resultSet2.next();
			int count = resultSet1.getMetaData().getColumnCount();
			for(int i = 1; i<=count; i++) {
				if(!StringUtils.equals(resultSet1.getString(i), resultSet2.getString(i))) {
					return false;
				}
			}
		}
		return true;
	}
}
