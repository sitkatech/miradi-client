package org.conservationmeasures.eam.project;

import java.io.File;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.martus.util.DirectoryUtils;

public class TestTncCapWorkbookImporter extends EAMTestCase
{
	public TestTncCapWorkbookImporter(String name)
	{
		super(name);
	}
	
	public void setUp() throws Exception
	{
		super.setUp();
		projectDirectory = createTempDirectory();
	}
	
	public void tearDown() throws Exception
	{
		DirectoryUtils.deleteEntireDirectoryTree(projectDirectory);
		super.tearDown();
	}

	public void testProjectInfoImport() throws Exception
	{
		try 
		{
			new TncCapWorkbookImporter("./tests/CAP_vXa_beta_091806_San_Miguel_v5_data.xls");
			fail("Should have trown for bad file version");
		}
		catch (Exception ignoreExpected)
		{	
		}
		
		
		TncCapWorkbookImporter tncCapWookbook = new TncCapWorkbookImporter("./tests/CAP_v5a_beta_091806_San_Miguel_v5_data.xls");
		assertEquals(PROJECT_VERSION, tncCapWookbook.getProjectVersion());
		assertEquals(PROJECT_VERSION_DATE, tncCapWookbook.getProjectVersionDate());
		assertEquals(PROJECT_DOWNLOAD_DATE, tncCapWookbook.getProjectDownloadDate());
	}
	
	final static String PROJECT_VERSION = "CAP_v5a.xls";
	final static String PROJECT_VERSION_DATE = "2006-08-23";
	final static String PROJECT_DOWNLOAD_DATE = "";
	File projectDirectory;
}
