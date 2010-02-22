/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.TestSimpleThreatRatingFramework;
import org.miradi.project.TestStressBasedThreatRatingFramework;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;

public class TestWcsExporter extends TestCaseWithProject
{
	public TestWcsExporter(String name)
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
	
	public void testProjectWithStressBasedThreatRatingData() throws Exception
	{
		getProject().setMetadata(ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		Cause threat = getProject().createCause();
		getProject().enableAsThreat(threat);
		Target target = (Target) targetDiagramFactor.getWrappedFactor();
		TestStressBasedThreatRatingFramework.createThreatFactorLink(getProject(), threat, target);
		validateProject();
	}
	
	public void testProjectWithSimpleThreatRatingData() throws Exception
	{
		getProject().setMetadata(ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.SIMPLE_BASED_CODE);
		
		DiagramFactor threatDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(ObjectType.CAUSE);
		getProject().enableAsThreat(threatDiagramFactor.getWrappedORef());

		DiagramFactor targetDiagramFactor = getProject().createDiagramFactorAndAddToDiagram(Target.getObjectType());
		getProject().createFactorLink(threatDiagramFactor.getWrappedORef(), targetDiagramFactor.getWrappedORef());
		
		TestSimpleThreatRatingFramework.populateBundle(getProject().getSimpleThreatRatingFramework(), threatDiagramFactor.getWrappedId(), targetDiagramFactor.getWrappedId(), getProject().getSimpleThreatRatingFramework().getValueOptions()[0]);
		
		validateProject();
	}

	private void validateProject() throws Exception
	{
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		UnicodeWriter writer = new UnicodeWriter(bytes);
		new WcsXmlExporter(getProject()).exportProject(writer);
		writer.close();
		String xml = new String(bytes.toByteArray(), "UTF-8");

		// NOTE: Uncomment for debugging only
//		File file = createTempFile();
//		file.createNewFile();
//		UnicodeWriter tempWriter = new UnicodeWriter(file);
//		tempWriter.writeln(xml);
//		tempWriter.close();
		
		InputStreamWithSeek inputStream = new StringInputStreamWithSeek(xml);
		if (!new WcsMiradiXmlValidator().isValid(inputStream))
		{
			throw new ValidationException(EAM.text("File to import does not validate."));
		}
	}
}
