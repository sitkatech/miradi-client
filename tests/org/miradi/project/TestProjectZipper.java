/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.miradi.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;
import org.miradi.main.EAMTestCase;
import org.miradi.project.ProjectZipper;

public class TestProjectZipper extends EAMTestCase
{
	public TestProjectZipper(String name)
	{
		super(name);
	}

	public void testCreatedZipFile() throws Exception
	{
		File testDir = createTempDirectory();
		
		try
		{
			Vector zipFileEntries = new Vector();
			File fileA = new File(testDir, "fileA");
			createSampleFile(fileA);
			zipFileEntries.add(fileA);
			
			File subDirB = new File(testDir, "dirB");
			subDirB.mkdirs();
			
			File subDirC = new File(subDirB, "dirC");
			subDirC.mkdirs();
			
			File fileD = new File(subDirC, "fileD");
			createSampleFile(fileD);
			zipFileEntries.add(fileD);
			
			File fileE = new File(subDirC, "fileE");
			createSampleFile(fileE);
			zipFileEntries.add(fileE);
			
			File fileF = new File(testDir, "fileF");
			createSampleFile(fileF);
			zipFileEntries.add(fileF);
			
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ZipOutputStream out = new ZipOutputStream(byteOut);
			ProjectZipper.addTreeToZip(out, "", testDir);
			
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ZipInputStream in = new ZipInputStream(byteIn);
			
			String[] zipEntryNames = {"fileA", "fileF", "dirB/dirC/fileD", "dirB/dirC/fileE"};
			while (in.available() != 0)
			{
				ZipEntry e = in.getNextEntry();
				ArrayList aList = new ArrayList(Arrays.asList(zipEntryNames));
				int aIndex = aList.indexOf(e.getName());
				String entrySearched = aList.get(aIndex).toString();

				File foundFile = null;
				for (int i = 0; i < zipFileEntries.size(); i++)
					if (e.getName().endsWith(((File)zipFileEntries.get(i)).getName()))
						foundFile = (File)zipFileEntries.get(i);
				
				assertEquals("wrong name?",e.getName(), entrySearched);
				assertEquals("wrong contents? ", foundFile.getAbsoluteFile().toString(), readZipEntryContents(in));				
			}
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(testDir);
		}
		
	}

	private void createSampleFile(File file) throws Exception
	{
		UnicodeWriter writer = new UnicodeWriter(file);
		writer.writeln(file.getAbsolutePath());
		writer.close();
	}
	
	private String readZipEntryContents(ZipInputStream in) throws Exception
	{
		UnicodeReader reader = new UnicodeReader(in);
		
		return reader.readLine();
	}
}
