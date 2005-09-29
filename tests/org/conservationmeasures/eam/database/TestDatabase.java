/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.sql.ResultSet;

import org.conservationmeasures.eam.testall.EAMTestCase;


public class TestDatabase extends EAMTestCase
{
	public TestDatabase(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		
		db = new Database();
		db.openMemoryDatabase("testdb");
	}
	
	public void tearDown() throws Exception
	{
		db.close();
		super.tearDown();
	}
	
	public void testRawSql() throws Exception
	{
		boolean createGaveResults = db.rawExecute("CREATE TABLE test (text VARCHAR, number INTEGER);");
		assertFalse("create returned results?", createGaveResults);
		
		boolean insertGaveResults = db.rawExecute("INSERT INTO test (text, number) VALUES('hello', 3);");
		assertFalse("insert returned results?", insertGaveResults);
		
		ResultSet result = db.rawSelect("SELECT * FROM test");
		assertTrue("no first row?", result.next());
		assertEquals("hello", result.getString("text"));
		assertEquals(3, result.getInt("number"));
		assertFalse("had a second row?", result.next());
	}
	
	public void testMrPersister() throws Exception
	{
		String table = "SampleObject";
		db.rawExecute("CREATE TABLE " + table + " (id INTEGER PRIMARY KEY, type INTEGER, text VARCHAR);");
		
		SampleObject a = new SampleObject();
		a.setId(1234);
		a.setType(99);
		a.setText("This is a good test");
		db.insert(a);
		
		SampleObject c = (SampleObject)db.read(SampleObject.class, a.getId());
		assertEquals(a.getId(), c.getId());
		assertEquals(a.getType(), c.getType());
		assertEquals(a.getText(), c.getText());
		
		SampleObject b = (SampleObject)db.select(SampleObject.class, "SELECT * FROM " + table + " WHERE id = 1234");
		assertEquals(a.getId(), b.getId());
		assertEquals(a.getType(), b.getType());
		assertEquals(a.getText(), b.getText());
	}
	
	Database db;
}


