/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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

import java.io.IOException;

import org.martus.util.UnicodeStringWriter;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.project.Project;
import org.miradi.project.ProjectForTesting;
import org.miradi.project.ProjectSaver;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.UnicodeXmlWriter;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlImporter;

public class TestCaseForXmpz2ExportAndImport extends TestCaseWithProject
{
	public TestCaseForXmpz2ExportAndImport(String name)
	{
		super(name);
	}

	protected void verifyRoundTripExportImport() throws Exception,
			IOException
	{
		Project roundTrip = exportAndImportProject(getProject());
		String originalMpf = getProjectMpfWithoutTimestamp(getProject());
		String roundTripMpf = getProjectMpfWithoutTimestamp(roundTrip);
		assertEquals(originalMpf, roundTripMpf);
	}

	private String getProjectMpfWithoutTimestamp(Project roundTrip)
			throws IOException, Exception
	{
		UnicodeStringWriter roundTripWriter = UnicodeStringWriter.create();
		ProjectSaver.saveProject(roundTrip, roundTripWriter);
		String roundTripMpf = roundTripWriter.toString();
		int roundTripEndLine = roundTripMpf.indexOf("-- ");
		roundTripMpf = roundTripMpf.substring(0, roundTripEndLine);
		return roundTripMpf;
	}

	private Project exportAndImportProject(Project projectToExport) throws Exception
	{
		String xml = exportProject(projectToExport);
		return importIntoNewProject(xml);
	}

	private Project importIntoNewProject(String xml) throws Exception
	{
		ProjectForTesting projectToImportInto = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToImportInto");
		Xmpz2XmlImporter xmlImporter = new Xmpz2XmlImporter(projectToImportInto, new NullProgressMeter());
		StringInputStreamWithSeek stringInputStream = new StringInputStreamWithSeek(xml);
		try
		{
			xmlImporter.importProjectXml(stringInputStream);
			return xmlImporter.getProject();
		}
		finally
		{
			stringInputStream.close();
		}
		
	}

	protected String exportProject(Project projectToExport) throws Exception
	{
		final UnicodeXmlWriter writer = UnicodeXmlWriter.create();
		new Xmpz2XmlExporter(projectToExport).exportProject(writer);
		writer.close();
		return writer.toString();
	}

}
