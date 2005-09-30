/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.database;

import java.io.File;
import java.sql.ResultSet;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.martus.util.DirectoryUtils;


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
		createSampleTable(db);
	}

	public void tearDown() throws Exception
	{
		db.rawExecute("DROP TABLE " + sampleTable);
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
		SampleObject a = new SampleObject();
		a.setType(99);
		a.setText("This is a good test");
		db.insert(a);
		
		SampleObject c = (SampleObject)db.read(SampleObject.class, a.getId());
		assertEquals(a.getId(), c.getId());
		assertEquals(a.getType(), c.getType());
		assertEquals(a.getText(), c.getText());
		
		SampleObject b = (SampleObject)db.select(SampleObject.class, "SELECT * FROM " + sampleTable + " WHERE id = " + a.getId());
		assertEquals(a.getId(), b.getId());
		assertEquals(a.getType(), b.getType());
		assertEquals(a.getText(), b.getText());
	}
	
	public void testEscaping() throws Exception
	{
		SampleObject containsIckyStuff = new SampleObject();
		containsIckyStuff.setText("this 'could `(cause \" problems ");
		db.insert(containsIckyStuff);
		SampleObject gotBack = (SampleObject)db.read(SampleObject.class, containsIckyStuff.getId());
		assertEquals("Didn't write and read ok?", containsIckyStuff.getText(), gotBack.getText());
	}
	
	public void testDiskDatabase() throws Exception
	{
		File tempDirectory = createTempDirectory();
		File baseName = new File(tempDirectory, "testdb");
		Database disk = new Database();
		disk.openDiskDatabase(baseName);
		try
		{
			createSampleTable(disk);
			SampleObject object = new SampleObject();
			object.setText("Hello");
			disk.insert(object);
			disk.close();
			
			disk.openDiskDatabase(baseName);
			SampleObject got = (SampleObject)disk.read(SampleObject.class, object.getId());
			assertEquals("didn't read back?", object.getText(), got.getText());
			disk.close();
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempDirectory);
		}
	}
	
	private void createSampleTable(Database database) throws Exception
	{
		database.rawExecute("CREATE TABLE " + sampleTable + " (id INTEGER IDENTITY PRIMARY KEY, type INTEGER, text VARCHAR);");
	}
	
	final static String sampleTable = "SampleObject";
	Database db;
}


