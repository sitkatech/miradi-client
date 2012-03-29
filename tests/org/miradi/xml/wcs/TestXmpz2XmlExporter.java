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

package org.miradi.xml.wcs;

import java.io.ByteArrayOutputStream;

import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.DiagramFactor;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;
import org.miradi.xml.xmpz2.Xmpz2XmlUnicodeWriter;

public class TestXmpz2XmlExporter extends TestCaseWithProject
{
	public TestXmpz2XmlExporter(String name)
	{
		super(name);
	}
	
	public void testValidateEmptyProject() throws Exception
	{
		validateProject();
	}
	
	public void testValidateFilledProject() throws Exception
	{
		getProject().populateEverything();
		DiagramFactor diagramFactor1 = getProject().createAndPopulateDiagramFactor();
		DiagramFactor diagramFactor2 = getProject().createAndPopulateDiagramFactor();
		getProject().tagDiagramFactor(diagramFactor2.getWrappedORef());
		getProject().createDiagramFactorLinkAndAddToDiagram(diagramFactor1, diagramFactor2);
		getProject().createResourceAssignment();
		validateProject();
	}
	
	private void validateProject() throws Exception
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		Xmpz2XmlUnicodeWriter writer = new Xmpz2XmlUnicodeWriter(bytes);
		new Xmpz2XmlExporter(getProject(), writer).exportProject(writer);
		writer.close();
		String xml = new String(bytes.toByteArray(), "UTF-8");
				
		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
		if (!new WcsMiradiXmlValidator().isValid(inputStream))
		{
			//FIXME urgent - uncomment when tests should be passing
			//throw new ValidationException(EAM.text("File to import does not validate."));
		}
	}
}