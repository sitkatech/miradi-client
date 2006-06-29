package org.conservationmeasures.eam.project;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.conservationmeasures.eam.testall.EAMTestCase;
import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeReader;
import org.martus.util.UnicodeWriter;

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
			File fileA = new File(testDir, "fileA");
			createSampleFile(fileA);
			File subDirB = new File(testDir, "dirB");
			subDirB.mkdirs();
			File subDirC = new File(subDirB, "dirC");
			subDirC.mkdirs();
			File fileD = new File(subDirC, "fileD");
			createSampleFile(fileD);
			File fileE = new File(subDirC, "fileE");
			createSampleFile(fileE);
			File fileF = new File(testDir, "fileF");
			createSampleFile(fileF);
			
			ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
			ZipOutputStream out = new ZipOutputStream(byteOut);
			ProjectZipper.addTreeToZip(out, "", testDir);
			
			ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
			ZipInputStream in = new ZipInputStream(byteIn);
			
			ZipEntry entryA = in.getNextEntry();
			assertEquals("wrong name? ", fileA.getName(), entryA.getName());
			assertEquals("wrong contents? ", fileA.getAbsolutePath(), readZipEntryContents(in));

			ZipEntry entryD = in.getNextEntry();
			assertEquals("wrong name? ","dirB/dirC/fileD", entryD.getName());
			assertEquals("wrong contents? ", fileD.getAbsolutePath(), readZipEntryContents(in));

			ZipEntry entryE = in.getNextEntry();
			assertEquals("wrong name? ", "dirB/dirC/fileE", entryE.getName());
			assertEquals("wrong contents? ", fileE.getAbsolutePath(), readZipEntryContents(in));


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
