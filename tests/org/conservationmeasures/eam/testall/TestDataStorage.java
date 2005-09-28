/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.testall;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.jenkov.mrpersister.PersistenceManager;
import com.jenkov.mrpersister.itf.IGenericDao;


public class TestDataStorage extends EAMTestCase
{
	public TestDataStorage(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		if(persistenceManager == null)
			persistenceManager = new PersistenceManager();
		
		// this weird line of code causes the hsqldb driver to get loaded
		Class.forName("org.hsqldb.jdbcDriver");
		
		String databaseType = "mem";
		String fileNamePrefix = "testdb";
		String database = "jdbc:hsqldb:" + databaseType + ":" + fileNamePrefix;
		connection = DriverManager.getConnection(database);
	}
	
	public void tearDown() throws Exception
	{
		connection.close();
		super.tearDown();
	}
	
	public void testRawSql() throws Exception
	{
		Statement createTable = connection.createStatement();
		assertFalse("create returned results?", createTable.execute("CREATE TABLE test (text VARCHAR, number INTEGER);"));
		
		Statement insertRow = connection.createStatement();
		assertFalse("insert returned results?", insertRow.execute("INSERT INTO test (text, number) VALUES('hello', 3);"));
		
		Statement selectAll = connection.createStatement();
		assertTrue("select didn't return results?", selectAll.execute("SELECT * FROM test"));
		ResultSet result = selectAll.getResultSet();
		assertTrue("no first row?", result.next());
		assertEquals("hello", result.getString("text"));
		assertEquals(3, result.getInt("number"));
		assertFalse("had a second row?", result.next());
	}
	
	public void testMrPersister() throws Exception
	{
		String table = "TestDataStorage$SampleObject";
		connection.createStatement().execute("CREATE TABLE " + table + " (id INTEGER PRIMARY KEY, type INTEGER, text VARCHAR);");
		IGenericDao dao = persistenceManager.getGenericDaoFactory().createDao(connection);
		
		SampleObject a = new SampleObject();
		a.setId(1234);
		a.setType(99);
		a.setText("This is a good test");
		dao.insert(a);
		
		SampleObject c = (SampleObject)dao.readByPrimaryKey(SampleObject.class, new Integer(a.getId()));
		assertEquals(a.getId(), c.getId());
		assertEquals(a.getType(), c.getType());
		assertEquals(a.getText(), c.getText());
		
		SampleObject b = (SampleObject)dao.read(SampleObject.class, "SELECT * FROM " + table + " WHERE id = 1234");
		assertEquals(a.getId(), b.getId());
		assertEquals(a.getType(), b.getType());
		assertEquals(a.getText(), b.getText());
		
		dao.closeConnection();
	}
	
	public static class SampleObject
	{
		public void setId(int id)
		{
			this.id = id;
		}
		
		public int getId()
		{
			return id;
		}
		
		public void setType(int type)
		{
			this.type = type;
		}
		
		public int getType()
		{
			return type;
		}
		
		public void setText(String text)
		{
			this.text = text;
		}
		
		public String getText()
		{
			return text;
		}

		private int id;
		private int type;
		private String text;
	}

	static PersistenceManager persistenceManager = new PersistenceManager();
	
	Connection connection;
}
