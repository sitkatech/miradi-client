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
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Cause;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ProgressPercent;
import org.miradi.objects.ProgressReport;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Strategy;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.ThreatRatingModeChoiceQuestion;
import org.miradi.questions.TncOperatingUnitsQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.TestTranslations;
import org.miradi.utils.Translation;

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
			new ConproXmlExporter(getProject()).export(tempXmlOutFile);
			assertTrue("did not validate?", new ConProMiradiXmlValidator().isValid(new FileInputStream(tempXmlOutFile)));
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
			new ConproXmlExporter(getProject()).export(tempXmlOutFile);
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
	
	public void testStatuses() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		verifyExport();

		ProgressReport emptyProgressReport = getProject().createProgressReport();
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_PROGRESS_REPORT_REFS, new ORefList(emptyProgressReport));
		verifyExport();
		
		createFilledProgressReportAndAddToIndicator(indicator, "Planned", "", "");
		createFilledProgressReportAndAddToIndicator(indicator, "", "2009-10-10", "");
		createFilledProgressReportAndAddToIndicator(indicator, "", "", "Some Details");
		verifyExport();
	}
	
	public void testMeasurements() throws Exception
	{
		Indicator indicator = getProject().createIndicatorWithCauseParent();
		verifyExport();

		Measurement emptyMeasurement = getProject().createMeasurement();
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, new ORefList(emptyMeasurement));
		verifyExport();
		
		createFilledMeasurementAndAddToIndicator(indicator, "SomeSummary", "", "", "", "");		
		createFilledMeasurementAndAddToIndicator(indicator, "", "2009-10-10", "", "", "");
		createFilledMeasurementAndAddToIndicator(indicator, "", "", "Rapid Assessment", "", "");
		createFilledMeasurementAndAddToIndicator(indicator, "", "", "", "Strong Increase", "");
		createFilledMeasurementAndAddToIndicator(indicator, "", "", "", "", "Fair");
		verifyExport();
	}
	
	public void testProgressPercent() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Objective objective = getProject().createObjective(strategy);
		verifyExport();
		
		ProgressPercent emptyProgressPercent = getProject().createProgressPercent();
		getProject().fillObjectUsingCommand(objective, Objective.TAG_PROGRESS_PERCENT_REFS, new ORefList(emptyProgressPercent));
		verifyExport();
		
		createfilledProgressPercentAndAddToObjective(objective, "2009-01-23", "", "");
		createfilledProgressPercentAndAddToObjective(objective, "", "21", "");
		createfilledProgressPercentAndAddToObjective(objective, "", "", "some percent complete notes");
		verifyExport();
	}
	
	public void testExportingWithObsoleteOperatingUnitsCode() throws Exception
	{
		CodeList operatingUnitCodes = new CodeList();
		operatingUnitCodes.add(TncOperatingUnitsQuestion.TNC_SUPERSEDED_OU_CODE);
		getProject().fillObjectUsingCommand(getProject().getMetadata(), ProjectMetadata.TAG_TNC_OPERATING_UNITS, operatingUnitCodes.toString());
		
		verifyExport();
	}
	
	public void testExportingThreatReductionResultReferringToInvalidThreat() throws Exception
	{
		ThreatReductionResult threatReductionResult = getProject().createThreatReductionResult();
		Objective objective = getProject().createAndPopulateObjective(threatReductionResult);
		
		BaseId SOME_BOGUS_NON_EXISTING_ID = new BaseId(99999);
		ORef someNonExistingThreatRef = new ORef(Cause.getObjectType(), SOME_BOGUS_NON_EXISTING_ID);
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_RELATED_DIRECT_THREAT_REF, someNonExistingThreatRef.toString());
		
		IdList objectiveIds = new IdList(Objective.getObjectType());
		objectiveIds.add(objective.getId());
		getProject().fillObjectUsingCommand(threatReductionResult, ThreatReductionResult.TAG_OBJECTIVE_IDS, objectiveIds.toString());
		
		verifyExport();	
	}
	
	private void createfilledProgressPercentAndAddToObjective(Objective objective, String date, String percentComplete, String notes) throws Exception
	{
		ProgressPercent progressPercent = getProject().createProgressPercent();
		getProject().fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_DATE, date);
		getProject().fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE, percentComplete);
		getProject().fillObjectUsingCommand(progressPercent, ProgressPercent.TAG_PERCENT_COMPLETE_NOTES, notes);
		
		ORefList progressPercentRefs = objective.getProgressPercentRefs();
		progressPercentRefs.add(progressPercent);
		getProject().fillObjectUsingCommand(objective, Objective.TAG_PROGRESS_PERCENT_REFS, progressPercentRefs);
	}
	
	private void createFilledMeasurementAndAddToIndicator(Indicator indicator, String summary, String date, String statusConfidence, String trend, String status) throws Exception
	{
		Measurement measurement = getProject().createMeasurement();
		getProject().fillObjectUsingCommand(measurement, Measurement.TAG_SUMMARY, summary);
		getProject().fillObjectUsingCommand(measurement, Measurement.TAG_DATE, date);
		getProject().fillObjectUsingCommand(measurement, Measurement.TAG_STATUS_CONFIDENCE, statusConfidence);
		getProject().fillObjectUsingCommand(measurement, Measurement.TAG_TREND, trend);
		getProject().fillObjectUsingCommand(measurement, Measurement.TAG_STATUS, status);
		
		ORefList measurementRefs = indicator.getMeasurementRefs();
		measurementRefs.add(measurement);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_MEASUREMENT_REFS, measurementRefs);
	}
	
	private void createFilledProgressReportAndAddToIndicator(Indicator indicator, String status, String date, String details) throws Exception
	{
		ProgressReport progressReport = getProject().createProgressReport();
		getProject().fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_STATUS, status);
		getProject().fillObjectUsingCommand(progressReport, ProgressReport.TAG_PROGRESS_DATE, date);
		getProject().fillObjectUsingCommand(progressReport, ProgressReport.TAG_DETAILS, details);
		
		ORefList progressReportRefs = indicator.getProgressReportRefs();
		progressReportRefs.add(progressReport);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_PROGRESS_REPORT_REFS, progressReportRefs);
	}
}
