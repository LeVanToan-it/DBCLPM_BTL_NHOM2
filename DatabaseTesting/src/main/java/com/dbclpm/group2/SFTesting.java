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

public class SFTesting {
	
	Connection con = null;
	Statement stmt = null;
	ResultSet rs = null;
	ResultSet rs1 = null;
	ResultSet rs2 = null;
	CallableStatement cStmt = null;	
	
	@BeforeClass
	public void setup() throws SQLException {
		con = DriverManager.getConnection("jdbc:mysql://localhost:3306/classicmodels","root","sapassword");
	}
	
	@AfterClass
	public void shutdown() throws SQLException {
		con.close();
	}
	
	// =========== STORE FUNCTION TESTING ===========
	@Test(priority = 1)
	public void test_storeFunctionCustomerLevelExist() throws SQLException {
		stmt = con.createStatement();
		rs = stmt.executeQuery("show function status where Name='CustomerLevel';");
		rs.next();
		
		Assert.assertEquals(rs.getString("Name"), "CustomerLevel");
	}
	
	@Test(priority = 2)
	public void test_storeFunctionCustomerLevelFromSQLStatement() throws SQLException {
		rs1 = con.createStatement().executeQuery("select customerName, CustomerLevel(creditLimit) from customers");
		rs2 = con.createStatement().executeQuery("select customerName,\r\n"
				+ "case\r\n"
				+ "when creditLimit > 50000 then 'PLATINUM'\r\n"
				+ "   when creditLimit >= 10000 and creditLimit <= 50000 then  'GOLD'\r\n"
				+ "   when creditLimit < 10000 then 'SILVER'\r\n"
				+ "end as customerlevel from customers;");
		
		Assert.assertEquals(compareResultSet(rs1, rs2), true);		
	}
	
	@Test(priority = 3)
	public void test_storeFunctionCustomerLevelThroughStoreProcedure() throws SQLException {
		cStmt = con.prepareCall("call GetCustomerLevel(?,?)");
		cStmt.setInt(1, 131);
		cStmt.registerOutParameter(2, Types.VARCHAR);
		cStmt.executeQuery();
		
		String customerLevel = cStmt.getString(2);
		
		Statement stmt = con.createStatement();
		rs = stmt.executeQuery("select\r\n"
				+ "case\r\n"
				+ "when creditLimit > 50000 then 'PLATINUM'\r\n"
				+ "   when creditLimit >= 10000 and creditLimit <= 50000 then  'GOLD'\r\n"
				+ "   when creditLimit < 10000 then 'SILVER'\r\n"
				+ "end as customerlevel from customers where customerNumber=131;");
		rs.next();
		
		String exp_customerLevel = rs.getString("customerLevel");
		
		Assert.assertEquals(customerLevel, exp_customerLevel);
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
