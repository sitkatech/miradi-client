/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.project;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.zip.ZipFile;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.ResourcesHandler;
import org.miradi.main.TestCaseWithProject;

public class TestMpzToMiradiConverter extends TestCaseWithProject
{
	public TestMpzToMiradiConverter(String name)
	{
		super(name);
	}
	
	public void testConvertMpzToDotMiradi() throws Exception
	{
		byte[] mpzBytes = readSampleMpz();
		String projectAsStringFromConverter = convertMpzToDotMiradi(mpzBytes);
		ProjectForTesting project2 = createProjectFromDotMiradi(projectAsStringFromConverter);
		assertEquals(935, project2.getNormalIdAssigner().getHighestAssignedId());
		//FIXME: Need to spot-check various other items
		// text field with newlines
		// numeric field
		// date field
		// choice field
		// reflist
		// simple threat rating bundle values
		// ...
		
		final int expectedSizeAfterTruncationOfSampleException = 4731;
		final String exceptionLog = project2.getExceptionLog();
		assertTrue("Exception log did not get truncated?", exceptionLog.length() < expectedSizeAfterTruncationOfSampleException);
	}

	private byte[] readSampleMpz() throws Exception
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		URL url = ResourcesHandler.getResourceURL("/Sample-v61.mpz");
		InputStream in = url.openStream();
		try
		{
			byte[] buffer = new byte[1024];
			int got = -1;
			while( (got = in.read(buffer)) > 0)
			{
				out.write(buffer, 0, got);
			}
			out.close();
		}
		finally
		{
			in.close();
		}
		return out.toByteArray();
	}

	private ProjectForTesting createProjectFromDotMiradi(String projectAsStringFromConverter) throws Exception
	{
		ProjectForTesting project2 = ProjectForTesting.createProjectWithDefaultObjects(getName());
		project2.clear();
		UnicodeStringReader reader = new UnicodeStringReader(projectAsStringFromConverter);
		ProjectLoader.loadProject(reader, project2);
		return project2;
	}

	private String convertMpzToDotMiradi(byte[] byteArray) throws Exception
	{
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		File tempMpzFile = File.createTempFile("$$$TestMpzToMiradiConverter.mpz", null);
		try
		{
			FileOutputStream fileOut = new FileOutputStream(tempMpzFile);
			fileOut.write(byteArray);
			fileOut.flush();
			fileOut.close();
			MpzToDotMiradiConverter.convert(new ZipFile(tempMpzFile), writer);
			
			return writer.toString();
		}
		finally
		{
			writer.close();
			tempMpzFile.delete();
		}
	}

}
