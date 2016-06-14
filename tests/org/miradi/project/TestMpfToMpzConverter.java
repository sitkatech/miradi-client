/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import org.martus.util.DirectoryUtils;
import org.miradi.main.ResourcesHandler;
import org.miradi.main.TestCaseWithProject;
import org.miradi.migrations.RawObject;
import org.miradi.migrations.RawProject;
import org.miradi.migrations.RawProjectLoader;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.StatusQuestion;
import org.miradi.schemas.StrategySchema;
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

	private void verifyStrategyComments(final String expectedComments, final String commentToStore) throws Exception
	{
		final String STRATEGY_LABEL = "StrategyToVerifyLabel";
		RawProject rawProject = loadSampleProject();
		createStrategy(rawProject, STRATEGY_LABEL, commentToStore);
		
		String verifiedProjectAsString = verifyProject(rawProject);
		RawProject verifiedProject = RawProjectLoader.loadProject(verifiedProjectAsString);
		RawObject strategy = findStrategyMatchingLabel(verifiedProject, STRATEGY_LABEL);
		final String actualComment = strategy.getData(Strategy.TAG_COMMENTS);
		assertEquals("Comment should not change during conversion?", expectedComments, actualComment);
	}

	public void testConvertingFullProject() throws Exception
	{
		getProject().populateEverything();
		
		AbstractTarget target = getProject().createAndPopulateHumanWelfareTarget();
		Strategy strategy = getProject().createStrategy();
		Indicator indicator = getProject().createAndPopulateIndicator(strategy);
		getProject().addThresholdWithXmlEscapedData(indicator);
		Task task = getProject().createAndPopulateTask(strategy, "TASK");
		Goal goal = getProject().createAndPopulateGoal(target);
		getProject().addProgressReport(task);
		getProject().addProgressReport(indicator);
		getProject().addProgressReport(strategy);
		getProject().addProgressPercent(goal);
		getProject().addExpenseWithValue(strategy);
		getProject().addResourceAssignment(strategy);
		getProject().createAndPopulateThreatReductionResult();
		
		PointList bendPointList = TestLinkBendPointsMoveHandler.createBendPointList();
		getProject().createLinkCellWithBendPoints(bendPointList);
		getProject().createAndPopulateIndicator(strategy);
		
		verifyProject();
	}

	private String verifyProject() throws Exception
	{
		return verifyProject(loadSampleProject());
	}

	private String verifyProject(RawProject rawProject) throws Exception
	{
		String actualMpf = toMpzAndBack(rawProject);
		final String actualWithoutTimestamp = stripLastMod(stripTimeStamp(actualMpf));
		final String expectedMpfSnapShot = RawProjectSaver.saveProject(rawProject);
		final String expectedWithoutTimeStamp = stripLastMod(stripTimeStamp(expectedMpfSnapShot));
		assertEquals("Mpf was not converted to mpz?", expectedWithoutTimeStamp, actualWithoutTimestamp);

		return setHighLowVersionsToLatestMpf(actualMpf);
	}

	private String toMpzAndBack(RawProject rawProject)throws Exception
	{
		File temporaryMpzFile = File.createTempFile("$$$tempMpzFile", ".zip");
		try
		{
			MpfToMpzConverter.convertWithoutMigrating(rawProject, getProject().getFilename(), temporaryMpzFile);

			return MpzToMpfConverter.convert(temporaryMpzFile, new NullProgressMeter());
		}
		finally 
		{
			DirectoryUtils.deleteEntireDirectoryTree(temporaryMpzFile);
		}
	}

	private String setHighLowVersionsToLatestMpf(String mpfContent)
	{
		String latestHeaderLine = AbstractMiradiProjectSaver.createLowHighVersionHeaderLine(Project.VERSION_HIGH, Project.VERSION_LOW);
		String headerLineForMpz = AbstractMiradiProjectSaver.createLowHighVersionHeaderLine(MpzToMpfConverter.FIRST_LOW_VERSION_OF_MPF, MpzToMpfConverter.FIRST_HIGH_VERSION_OF_MPF);
		
		return mpfContent.replaceAll(headerLineForMpz, latestHeaderLine);
	}

	private ProjectForTesting loadIntoNewProject(String loadedProject) throws Exception
	{
		InputStream is = new ByteArrayInputStream(StringUtilities.getUtf8EncodedBytes(loadedProject));
		ProjectForTesting projectToFill = ProjectForTesting.createProjectWithoutDefaultObjects("ProjectToFillWithMpf");
		ProjectLoader.loadProject(is, projectToFill);
		
		return projectToFill;
	}		

	private String stripTimeStamp(String mpf)
	{
		return stripLine(mpf, "--");
	}
	
	private String stripLastMod(String mpf)
	{
		return stripLine(mpf, "UL	LastModified=");
	}

	private String stripLine(String mpf, final String str)
	{
		int indexOfLastLine = mpf.indexOf(str);
		return mpf.substring(0, indexOfLastLine);
	}
	
	private RawObject findStrategyMatchingLabel(final RawProject rawProject, final String strategyLabelToMatch) throws Exception
	{
		ORefList strategyRefs = rawProject.getAllRefsForType(StrategySchema.getObjectType());
		for(ORef ref : strategyRefs)
		{
			RawObject strategy = rawProject.findObject(ref);		
			final String data = strategy.getData(Strategy.TAG_LABEL);
			if (data.equals(strategyLabelToMatch))
				return strategy;
		}
		
		throw new Exception("Strategy must exist in the project");
	}
	
	private RawProject loadSampleProject() throws IOException, Exception
	{
		URL url = ResourcesHandler.getResourceURL("/SampleProjectSavedFromVersion-4.1.0.Miradi");
		String mpfSnapShot = ResourcesHandler.loadFile(url);
		
		return RawProjectLoader.loadProject(mpfSnapShot);
	}
	
	private void createStrategy(RawProject rawProject, final String label, final String comments)	throws Exception
	{
		ORef strategyRef = rawProject.createObject(StrategySchema.getObjectType());
		rawProject.setObjectData(strategyRef, Strategy.TAG_COMMENTS, comments);		
		rawProject.setObjectData(strategyRef, Strategy.TAG_LABEL, label);
	}
	
	private static final String commentWithXmlEscapedChars = "&quot; &apos; &gt; &lt; &amp;";
}
