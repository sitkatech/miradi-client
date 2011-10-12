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

import org.martus.util.UnicodeWriter;
import org.miradi.main.TestCaseWithProject;

public class TestProjectSaver extends TestCaseWithProject
{
	public TestProjectSaver(String name)
	{
		super(name);
	}
	
	public void testBasics() throws Exception
	{
		getProject().populateEverything();
		getProject().populateSimpleThreatRatingValues();
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		UnicodeWriter consoleWriter = new UnicodeWriter(System.out);
		try
		{
			writeProject(bytes);
			String output = new String(bytes.toByteArray(), "UTF-8");
			consoleWriter.writeln(output);
		}
		finally 
		{
			consoleWriter.close();
			bytes.close();
		}
	}

	public void writeProject(ByteArrayOutputStream bytes) throws Exception
	{
		UnicodeWriter writer = new UnicodeWriter(bytes);
		try
		{
			ProjectSaver.saveProject(getProject(), writer);
		}
		finally 
		{
			writer.close();	
		}
	}
}
