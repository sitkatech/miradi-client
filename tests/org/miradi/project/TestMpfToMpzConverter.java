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

import java.io.File;

import org.miradi.main.TestCaseWithProject;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.utils.MpfToMpzConverter;
import org.miradi.utils.NullProgressMeter;
import org.miradi.utils.PointList;
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
		try
		{
			String projectAsString = ProjectSaver.saveProject(getProject(), temporaryMpfFile);
			new MpfToMpzConverter(getProject().getFilename()).convert(temporaryMpfFile, temporaryMpzFile);
			String mpf = MpzToMpfConverter.convert(temporaryMpzFile, new NullProgressMeter());
			mpf = stripTimeStamp(mpf);
			projectAsString = stripTimeStamp(projectAsString);
			assertEquals("Mpf was not converted to mpz?", projectAsString, mpf);
		}
		finally 
		{
			temporaryMpfFile.deleteOnExit();
			temporaryMpzFile.deleteOnExit();
		}
	}

	private String stripTimeStamp(String mpf)
	{
		int indexOfLastLine = mpf.indexOf("--");
		return mpf.substring(0, indexOfLastLine);
	}
}
