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
package org.miradi.xml.conpro.exporter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.martus.util.DirectoryUtils;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ProjectMetadata;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.utils.TestTranslations;
import org.miradi.utils.Translation;

public class TestConproXmlExporterVersion2 extends TestCaseWithProject
{
	public TestConproXmlExporterVersion2(String name)
	{
		super(name);
	}
	
	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		getProject().populateEverything();
	}
	
	public void testValidatedExport() throws Exception
	{
		verifyExportInEnglish();
		verifyExportInNonEglish();
	}

	private void verifyExportInNonEglish() throws Exception, IOException, FileNotFoundException
	{
		TestTranslations.setTestLocale();
		verifyExport();
		Translation.restoreDefaultLocalization();
	}

	private void verifyExportInEnglish() throws IOException, Exception,	FileNotFoundException
	{
		verifyLanguageIsInEnglish();
		verifyExport();
		verifyLanguageIsInEnglish();
	}

	private void verifyLanguageIsInEnglish()
	{
		assertTrue("is not in eglish?", Translation.isDefaultLocalization());
	}

	public void testValidatedExportInStressMode() throws Exception
	{	
		ORef metedataRef = getProject().getMetadata().getRef();
		getProject().setObjectData(metedataRef, ProjectMetadata.TAG_THREAT_RATING_MODE, ThreatRatingModeChoiceQuestion.STRESS_BASED_CODE);
		
		verifyExport();
	}
	
	private void verifyExport() throws IOException, Exception, FileNotFoundException
	{
		File tempXmlOutFile = createTempFileFromName("conproVersion2.xml");
		try
		{
			new ConproXmlExporterVersion2(getProject()).export(tempXmlOutFile);
			assertTrue("did not validate?", new ConProMiradiXmlValidatorVersion2().isValid(new FileInputStream(tempXmlOutFile)));
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempXmlOutFile);
		}
	}	
}
