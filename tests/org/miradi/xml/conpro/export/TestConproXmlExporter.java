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
import org.miradi.main.TestCaseWithProject;

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
		initializeProjectSampleData();
	}
	
	public void testValidatedExport() throws Exception
	{
		File tempXmlOutFile = createTempFileFromName("conpro.xml");
		try
		{
			new ConproXmlExporter(getProject()).export(tempXmlOutFile);
			assertTrue("did not validate?", new testSampleXml().isValid(new FileInputStream(tempXmlOutFile)));
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempXmlOutFile);
		}
	}
	
	private void initializeProjectSampleData() throws Exception
	{
		getProject().createAndPopulateDirectThreatLink();
		getProject().createAndPopulateIndicator();
		getProject().createAndPopulateKea();
		getProject().createAndPopulateMeasurement();
		getProject().createAndPopulateProjectResource();
		getProject().createAndPopulateStress();
		getProject().createAndPopulateSubTarget();
		getProject().createAndPopulateTarget();
		getProject().createAndPopulateTask();
		getProject().createAndPopulateThreat();
		getProject().createAndPopulateThreatStressRating();
		getProject().createAndPopulateObjective();
	}
}
