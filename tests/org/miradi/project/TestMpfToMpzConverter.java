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
import java.util.Collection;

import org.martus.util.DirectoryUtils;
import org.martus.util.UnicodeStringWriter;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.StatusQuestion;
import org.miradi.utils.HtmlUtilities;
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
	
	public void testSimpleThreatRating() throws Exception
	{
		getProject().populateSimpleThreatRatingValues();
		String loadedProject = verifyProject();
		ProjectForTesting projectToFill = loadIntoNewProject(loadedProject);	
		Collection<ThreatRatingBundle> allBundles = projectToFill.getSimpleThreatRatingFramework().getAllBundles();
		assertTrue("incorret number of bundles after convert?", allBundles.size() > 0);
	}

	public void testIndicatorThresholdValueWithXmlEscapedChars() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);
		CodeToUserStringMap threshold = new CodeToUserStringMap();
		threshold.putUserString(StatusQuestion.POOR, commentWithXmlEscapedChars);
		getProject().fillObjectUsingCommand(indicator, Indicator.TAG_THRESHOLDS_MAP, threshold.toJsonString());
		verifyProject();
	}
	
	public void testHandlingOfXmlValuesTags() throws Exception
	{
		verifyStrategyComments(commentWithXmlEscapedChars, commentWithXmlEscapedChars);
	}
	
	public void testStrippingHtmlTags() throws Exception
	{
		verifyStrategyComments(HtmlUtilities.stripAllHtmlTags("<html><b>boldText</b> and <i>italic</i></html>"), "<html><b>boldText</b> and <i>italic</i></html>");
	}

	private void verifyStrategyComments(final String expectedComments, final String comment) throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		getProject().fillObjectUsingCommand(strategy, Strategy.TAG_COMMENTS, comment);
		final String expectedMpfAsString = verifyProject();
		ProjectForTesting projectToFill = loadIntoNewProject(expectedMpfAsString);
		ORefList strategyRefs = projectToFill.getStrategyPool().getRefList();
		Strategy loadedStrategy = Strategy.find(projectToFill, strategyRefs.getFirstElement());		
		assertEquals("Comment should not change during conversion?", expectedComments, loadedStrategy.getComment());
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

	private String verifyProject() throws Exception
	{
		File temporaryMpzFile = File.createTempFile("$$$tempMpzFile", ".zip");
		try
		{
			String mpfSnapShot = ProjectSaver.createSnapShot(getProject());
			new MpfToMpzConverter(getProject()).convert(mpfSnapShot, temporaryMpzFile);
			String actualMpf = MpzToMpfConverter.convert(temporaryMpzFile, new NullProgressMeter());
			
			String expectedMpfAsString = reloadIntoProjectToRemoveDefaultValues(actualMpf);
				
			actualMpf = stripTimeStamp(actualMpf);
			String expectedMpfAsStringWithoutTimeStamp = stripTimeStamp(expectedMpfAsString);
			assertEquals("Mpf was not converted to mpz?", expectedMpfAsStringWithoutTimeStamp, actualMpf);
			return expectedMpfAsString;
		}
		finally 
		{
			DirectoryUtils.deleteEntireDirectoryTree(temporaryMpzFile);
		}
	}

	private String reloadIntoProjectToRemoveDefaultValues(String actualMpf) throws Exception
	{
		ProjectForTesting projectToFill = loadIntoNewProject(actualMpf);
		
		UnicodeStringWriter stringWriter = UnicodeStringWriter.create();
		ProjectSaver.saveProject(projectToFill, stringWriter);
		
		return stringWriter.toString();
	}
	
	public ProjectForTesting loadIntoNewProject(String loadedProject) throws Exception
	{
		InputStream is = new ByteArrayInputStream(StringUtilities.getUtf8EncodedBytes(loadedProject));
		ProjectForTesting projectToFill = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToFillWithMpf");
		ProjectLoader.loadProject(is, projectToFill);
		
		return projectToFill;
	}		

	private String stripTimeStamp(String mpf)
	{
		int indexOfLastLine = mpf.indexOf("--");
		return mpf.substring(0, indexOfLastLine);
	}
	
	private static final String commentWithXmlEscapedChars = "&quot; &apos; &gt; &lt; &amp;";
}
