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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.martus.util.UnicodeStringWriter;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.PointList;
import org.miradi.utils.StringUtilities;
import org.miradi.views.diagram.TestLinkBendPointsMoveHandler;

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
	
	public void testIndicatorThresholdValueWithXmlEscapedChars() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);
		CodeToUserStringMap threshold = new CodeToUserStringMap();
		threshold.putUserString(StatusQuestion.POOR, "&quot; &pos; &gt; &lt; &amp;");
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_THRESHOLDS_MAP, threshold.toJsonString());
		verifyProject();
	}

	public void testConvertingFullProject() throws Exception
	{
		getProject().populateEverything();
		
		AbstractTarget target = getProject().createAndPopulateHumanWelfareTarget();
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);
		getProject().addThresholdWithXmlEscapedData(indicator);
		Task task = getProject().createAndPopulateTask(indicator, "TASK");
		Goal goal = getProject().createAndPopulateGoal(target);
		getProject().addProgressReport(task);
		getProject().addProgressReport(indicator);
		getProject().addProgressReport(strategy);
		getProject().addProgressPercent(goal);
		getProject().addExpenseWithValue(strategy);
		getProject().addResourceAssignment(strategy);
		getProject().createandpopulateThreatReductionResult();
		
		PointList bendPointList = TestLinkBendPointsMoveHandler.createBendPointList();
		getProject().createLinkCellWithBendPoints(bendPointList);
		getProject().createAndPopulateIndicator(strategy);
		
		verifyProject();
	}

	private void verifyProject() throws Exception
	{
		File temporaryMpfFile = File.createTempFile("$$$tempMpfFile", null);
		File temporaryMpzFile = File.createTempFile("$$$tempMpzFile", ".zip");
		System.out.println(temporaryMpfFile.getAbsolutePath());
		try
		{
			ProjectSaver.saveProject(getProject(), temporaryMpfFile);
			new MpfToMpzConverter(getProject()).convert(temporaryMpfFile, temporaryMpzFile, getProject().getFilename());
			String actualMpf = MpzToMpfConverter.convert(temporaryMpzFile, new NullProgressMeter());
			
			String expectedMpfAsString = reloadIntoProjectToRemoveDefaultValues(actualMpf);
				
			actualMpf = stripTimeStamp(actualMpf);
			expectedMpfAsString = stripTimeStamp(expectedMpfAsString);
			assertEquals("Mpf was not converted to mpz?", expectedMpfAsString, actualMpf);
		}
		finally 
		{
			//temporaryMpfFile.deleteOnExit();
			//temporaryMpzFile.deleteOnExit();
		}
	}

	private String reloadIntoProjectToRemoveDefaultValues(String actualMpf) throws Exception
	{
		InputStream is = new ByteArrayInputStream(StringUtilities.getUtf8EncodedBytes(actualMpf));
		ProjectForTesting projectToFill = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToFillWithMpf");
		ProjectLoader.loadProject(is, projectToFill);
		
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		ProjectSaver.saveProject(projectToFill, stringWriter);
		
		return stringWriter.toString();
	}

	private String stripTimeStamp(String mpf)
	{
		int indexOfLastLine = mpf.indexOf("--");
		return mpf.substring(0, indexOfLastLine);
	}
}
