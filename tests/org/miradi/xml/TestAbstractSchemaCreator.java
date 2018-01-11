/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

package org.miradi.xml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.URL;

import org.miradi.main.ResourcesHandler;
import org.miradi.main.TestCaseWithProject;

abstract public class TestAbstractSchemaCreator extends TestCaseWithProject
{
	public TestAbstractSchemaCreator(String name)
	{
		super(name);
	}
	
	public String getExpectedLines() throws Exception
	{
		URL resourceURL = ResourcesHandler.getEnglishResourceURL(getStaticSchemaPath());
		FileInputStream fileInputStream = new FileInputStream(resourceURL.getFile());
		DataInputStream in = new DataInputStream(fileInputStream);
		BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
		StringBuffer allLines = new StringBuffer();
		String expectedLine;
		while ((expectedLine = bufferedReader.readLine()) != null)   
		{
			allLines.append(expectedLine);
			allLines.append("\n");
		}

		return allLines.toString();
	}

	abstract public String getStaticSchemaPath();
}
