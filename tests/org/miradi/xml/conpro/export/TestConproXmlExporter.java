/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.xml.conpro.export;

import java.io.File;
import java.io.FileInputStream;

import org.martus.util.DirectoryUtils;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;

public class TestConproXmlExporter extends TestCaseWithProject
{
	public TestConproXmlExporter(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		tempDir = createTempDirectory();
		fillProjectWithData();
	}
	
	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		DirectoryUtils.deleteEntireDirectoryTree(tempDir);
	}
	
	public void testValidatedExport() throws Exception
	{
		File tempXmlOutFile = new File(tempDir, "conpro.xml");
		new ConproXmlExporter(getProject()).export(tempXmlOutFile);
		assertTrue("did not validate?", new testSampleXml().validate(new FileInputStream(tempXmlOutFile)));
	}
	
	private void fillProjectWithData() throws Exception
	{
		ORef targetRef = getProject().createFactorAndReturnRef(Target.getObjectType());
		ORef keyEcologicalAttributeRef = getProject().createFactorAndReturnRef(KeyEcologicalAttribute.getObjectType());
		getProject().executeCommand(new CommandSetObjectData(keyEcologicalAttributeRef, KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE, KeyEcologicalAttributeTypeQuestion.SIZE));
		
		Target target = Target.find(getProject(), targetRef);
		getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(target, Target.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, keyEcologicalAttributeRef.getObjectId()));
		
		ORef indicatorRef = getProject().createFactorAndReturnRef(Indicator.getObjectType());
		KeyEcologicalAttribute keyEcologicalAttribute = KeyEcologicalAttribute.find(getProject(), keyEcologicalAttributeRef);
		getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(keyEcologicalAttribute, KeyEcologicalAttribute.TAG_INDICATOR_IDS, indicatorRef.getObjectId()));
		
		ORef measurementRef = getProject().createFactorAndReturnRef(Measurement.getObjectType());
		Indicator indicator = Indicator.find(getProject(), indicatorRef);
		getProject().executeCommand(CommandSetObjectData.createAppendORefCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, measurementRef));
		
		String FAIR = "2";
		getProject().executeCommand(new CommandSetObjectData(measurementRef, Measurement.TAG_STATUS, FAIR));
		
		assertEquals("wrong tnc viability calculation", "2", Target.computeTNCViability(getProject()));
	}
	
	private File tempDir;
}
