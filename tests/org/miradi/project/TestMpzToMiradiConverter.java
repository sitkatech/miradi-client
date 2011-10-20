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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.martus.util.UnicodeStringReader;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.TestCaseWithProject;

public class TestMpzToMiradiConverter extends TestCaseWithProject
{
	public TestMpzToMiradiConverter(String name)
	{
		super(name);
	}
	
	public void testConvertMpzToDotMiradi() throws Exception
	{
		getProject().populateEverything();
		getProject().populateSimpleThreatRatingValues();
		ByteArrayOutputStream out = addProjectAsMpzToZip(getProject());
		byte[] byteArray = out.toByteArray();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
		UnicodeStringWriter writer = UnicodeStringWriter.create();
		MpzToDotMiradiConverter.convert(zipInputStream, writer);
		writer.close();
		String projectAsStringFromConverter = writer.toString();
		
		ProjectForTesting project2 = new ProjectForTesting(getName());
		project2.clear();
		UnicodeStringReader reader = new UnicodeStringReader(projectAsStringFromConverter);
		
		ProjectLoader.loadProject(reader, project2);
		
		TestProjectSaver.compareProjects(getProject(), project2);
	}

	private ByteArrayOutputStream addProjectAsMpzToZip(ProjectForTesting projectForTesting) throws Exception
	{
		ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
		ZipOutputStream out = new ZipOutputStream(byteOut);
		try
		{
			ProjectMpzWriter.writeProjectZip(out, projectForTesting);
		}
		finally
		{
			out.flush();
			out.close();
			byteOut.flush();
			byteOut.close();
		}
		
		return byteOut;
	}
}
