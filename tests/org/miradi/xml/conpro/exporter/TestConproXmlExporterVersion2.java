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
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Objective;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Strategy;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StrategyClassificationQuestion;
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
	
	public void testExceptionThrownForIncorrectIUCNChoice() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		String nonSelectableCode = getNonSelectableTaxonomyCode();
		strategy.setData(Strategy.TAG_TAXONOMY_CODE, nonSelectableCode);
		
		File tempXmlOutFile = createTempFileFromName("conproVersion2.xml");
		try
		{
			new ConproXmlExporterVersion2(getProject()).export(tempXmlOutFile);
			fail("should not export non selectable IUCN code?");
		}
		catch (Exception ignoreException)
		{
		}
		finally
		{
			DirectoryUtils.deleteEntireDirectoryTree(tempXmlOutFile);
		}
	}

	private String getNonSelectableTaxonomyCode()
	{
		StrategyClassificationQuestion question = new StrategyClassificationQuestion();
		ChoiceItem[] choices = question.getChoices();
		for (int index = 0; index < choices.length; ++index)
		{
			if (!choices[index].isSelectable())
				return choices[index].getCode();
		}
		
		throw new RuntimeException("Did not find a non selectable classification choice.");
	}
	
	public void testProgressPercent() throws Exception
	{
		Objective objective = getProject().createObjective();
		verifyExport();
		
		ProgressPercent progressPercent1 = getProject().createProgressPercent();
		getProject().fillObjectUsingCommand(objective, Objective.TAG_PROGRESS_PERCENT_REFS, new ORefList(progressPercent1).toString());
		verifyExport();
		
		fillProgressPercent(progressPercent1, "2009-01-23", "", "");
		verifyExport();
		
		fillProgressPercent(progressPercent1, "", "21", "");
		verifyExport();
		
		fillProgressPercent(progressPercent1, "", "", "some percent complete notes");
		verifyExport();
		
		ProgressPercent progressPercent2 = getProject().createProgressPercent();
		fillProgressPercent(progressPercent2, "2009-01-23", "", "");
		
		ProgressPercent progressPercent3 = getProject().createProgressPercent();
		fillProgressPercent(progressPercent3, "", "23", "");
		
		ORefList progressPercentRefs = new ORefList();
		progressPercentRefs.add(progressPercent1);
		progressPercentRefs.add(progressPercent2);
		progressPercentRefs.add(progressPercent3);
		getProject().fillObjectUsingCommand(objective, Objective.TAG_PROGRESS_PERCENT_REFS, progressPercentRefs.toString());
		verifyExport();
	}
	
	private void fillProgressPercent(ProgressPercent progressPercent, String date, String percentComplete, String notes) throws Exception
	{
		getProject().fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_DATE, date);
		getProject().fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE, percentComplete);
		getProject().fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES, notes);
	}
}
