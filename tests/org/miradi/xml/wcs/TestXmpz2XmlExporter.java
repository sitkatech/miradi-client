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

import org.martus.util.UnicodeWriter;
import org.martus.util.inputstreamwithseek.InputStreamWithSeek;
import org.martus.util.inputstreamwithseek.StringInputStreamWithSeek;
import org.miradi.exceptions.ValidationException;
import org.miradi.main.EAM;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.ProjectMetadata;
import org.miradi.xml.xmpz2.Xmpz2XmlExporter;

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
	
	public void testProjectWithHtmlInQuarantinedContent() throws Exception
	{
		getProject().appendToQuarantineFile("some <br/> random <b>bolded</b> text");
		validateProject();
	}
	
	public void testUserTextFieldWithHtml() throws Exception
	{
		String sampleText = "one <br/> and <b>some bold</b> <br/>" +
				"two <b>spanning lines<br/>" +
				"</b>" +
				"<a href=\"www.miradi.org\">link</a>" +
				"<i>some <u><strike>combining</strike></u></i>" +
				"<ul><li>test one</li><li><b>bold item</b></li></ul>";
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_PROJECT_DESCRIPTION, sampleText);
		validateProject();
	}
	
	public void testValidateFilledProject() throws Exception
	{
		// FIXME urgent: This needs to do a better job of populating everything
		// It should auto-populate any newly created field so they are immediately 
		// flagged as needing to be added to the schema.
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
		final UnicodeWriter writer = new UnicodeWriter(bytes);
		new Xmpz2XmlExporter(getProject()).exportProject(writer);
		writer.close();
		String xml = new String(bytes.toByteArray(), "UTF-8");
				
		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
		if (!new Xmpz2XmlValidator().isValid(inputStream))
		{
			throw new ValidationException(EAM.text("File to import does not validate."));
		}
	}
}
