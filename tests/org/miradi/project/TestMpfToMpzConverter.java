/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import java.io.File;

import org.miradi.main.TestCaseWithProject;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.NullProgressMeter;

public class TestMpfToMpzConverter extends TestCaseWithProject
{
	public TestMpfToMpzConverter(String name)
	{
		super(name);
	}

	public void testConvertingEmptyProject() throws Exception
	{
		verifyProject();
	}

	public void testConvertingFullProject() throws Exception
	{
		getProject().populateEverything();
		verifyProject();
	}

	private void verifyProject() throws Exception
	{
		File temporaryMpfFile = File.createTempFile("$$$tempMpfFile", null);
		File temporaryMpzFile = File.createTempFile("$$$tempMpzFile", ".zip");
		try
		{
			String projectAsString = ProjectSaver.saveProject(getProject(), temporaryMpfFile);
			new MpfToMpzConverter(getProject().getFilename()).convert(temporaryMpfFile, temporaryMpzFile);
			String mpf = MpzToMpfConverter.convert(temporaryMpzFile, new NullProgressMeter());
			mpf = stripTimeStamp(mpf);
			projectAsString = stripTimeStamp(projectAsString);
			assertEquals("Mpf was not converted to mpz?", projectAsString, mpf);
		}
		finally 
		{
			temporaryMpfFile.deleteOnExit();
			temporaryMpzFile.deleteOnExit();
		}
	}

	private String stripTimeStamp(String mpf)
	{
		int indexOfLastLine = mpf.indexOf("--");
		return mpf.substring(0, indexOfLastLine);
	}
}
