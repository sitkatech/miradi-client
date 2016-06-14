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
package org.miradi.objects;

import org.martus.util.MultiCalendar;
import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.StrategySchema;

import java.util.Vector;


public class TestIndicator extends ObjectTestCase
{
	public TestIndicator(String name)
	{
		super(name);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();

		project = ProjectForTesting.createProjectWithDefaultObjects(getName());
		strategyActivityRelevancy = new TestStrategyActivityRelevancy(getName(), project);

		DiagramFactor strategyDiagramFactor = project.createAndAddFactorToDiagram(StrategySchema.getObjectType());
		strategy = (Strategy) strategyDiagramFactor.getWrappedFactor();
		activity = project.createTask(strategy);
		indicator = project.createIndicator(strategy);

		DiagramFactor otherStrategyDiagramFactor = project.createAndAddFactorToDiagram(StrategySchema.getObjectType());
		otherStrategy = (Strategy) otherStrategyDiagramFactor.getWrappedFactor();
		otherIndicator = project.createIndicator(otherStrategy);
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	protected int getType()
	{
		return IndicatorSchema.getObjectType();
	}

	public void testFields() throws Exception
	{
		verifyFields(getType());
	}
	
	public void testGetLatestMeasurementRef() throws Exception
	{
		ORef indicatorRef = project.createObject(IndicatorSchema.getObjectType());
		Indicator indicator = (Indicator) project.findObject(indicatorRef);
		assertEquals("Found date?", ORef.INVALID, indicator.getLatestMeasurementRef());
		
		ORef measurementRef1 = project.createObject(MeasurementSchema.getObjectType());
		ORef measurementRef2 = project.createObject(MeasurementSchema.getObjectType());
		ORefList measurementRefList = new ORefList();
		measurementRefList.add(measurementRef1);
		measurementRefList.add(measurementRef2);
		
		CommandSetObjectData setMeasurements = new CommandSetObjectData(indicatorRef, Indicator.TAG_MEASUREMENT_REFS, measurementRefList.toString());
		project.executeCommand(setMeasurements);
		
		MultiCalendar expectedBefore = MultiCalendar.createFromGregorianYearMonthDay(1000, 1, 1);
		CommandSetObjectData setMeasurement1Date = new CommandSetObjectData(measurementRef1, Measurement.TAG_DATE, expectedBefore.toIsoDateString());
		project.executeCommand(setMeasurement1Date);

		MultiCalendar expectedAfter = MultiCalendar.createFromGregorianYearMonthDay(2000, 1, 1);
		CommandSetObjectData setMeasurement2Date = new CommandSetObjectData(measurementRef2, Measurement.TAG_DATE, expectedAfter.toIsoDateString());
		project.executeCommand(setMeasurement2Date);
		
		String latestMeasurmentRefAsString2 = indicator.getPseudoData(indicator.PSEUDO_TAG_LATEST_MEASUREMENT_REF);
		ORef latestMeasurementRef2 = ORef.createFromString(latestMeasurmentRefAsString2);
		assertEquals("found latest measurement?", measurementRef2, latestMeasurementRef2);
		
		Measurement latestMeasurement = (Measurement) project.findObject(latestMeasurementRef2);
		MultiCalendar foundAfter = latestMeasurement.getDateData(Measurement.TAG_DATE);
		assertEquals("Found latest date?", expectedAfter, foundAfter);
	}
	
	public void testIsRefList() throws Exception
	{
		ORef indicatorRef = project.createObject(IndicatorSchema.getObjectType());
		Indicator indicator = (Indicator) project.findObject(indicatorRef);
		assertTrue("is not measurment ref tag?", indicator.isRefList(Indicator.TAG_MEASUREMENT_REFS));
	}

	public void testGetAnnotationType() throws Exception
	{
		ORef indicatorRef = project.createObject(IndicatorSchema.getObjectType());
		Indicator indicator = (Indicator) project.findObject(indicatorRef);
		assertEquals("is wrong annotation type?", MeasurementSchema.getObjectType(), indicator.getAnnotationType(Indicator.TAG_MEASUREMENT_REFS));
	}
	
	//region Strategy Relevancy tests
	public void testStrategyDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantNoOverridesMakeIrrelevant(strategy, indicator);
	}

	public void testStrategyDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantNoOverrideMakeRelevant(strategy, indicator);
	}

	public void testStrategyDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideIrrelevantMakeRelevant(strategy, indicator);
	}

	public void testStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideIrrelevantMakeIrrelevant(strategy, indicator);
	}

	public void testStrategyDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyDefaultRelevantOverrideRelevantMakeIrrelevant(strategy, indicator);
	}

	public void testStrategyDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(strategy, otherIndicator);
	}

	public void testStrategyDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(strategy, otherIndicator);
	}

	public void testStrategyDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(strategy, otherIndicator);
	}

	public void testStrategyDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(strategy, otherIndicator);
	}
	//endregion

	//region Activity Relevancy tests
	public void testActivityDefaultRelevantNoOverridesMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyActivityDefaultRelevantNoOverridesMakeIrrelevant(activity, indicator);
	}

	public void testActivityDefaultRelevantNoOverrideMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyActivityDefaultRelevantNoOverrideMakeRelevant(activity, indicator);
	}

	public void testActivityDefaultRelevantOverrideIrrelevantMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideIrrelevantMakeRelevant(activity, indicator);
	}

	public void testActivityDefaultRelevantOverrideIrrelevantMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideIrrelevantMakeIrrelevant(activity, indicator);
	}

	public void testActivityDefaultRelevantOverrideRelevantMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyActivityDefaultRelevantOverrideRelevantMakeIrrelevant(activity, indicator);
	}

	public void testActivityDefaultIrrelevantNoOverrideMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeRelevant(activity, otherIndicator);
	}

	public void testActivityDefaultIrrelevantNoOverrideMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantNoOverrideMakeIrrelevant(activity, otherIndicator);
	}

	public void testActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantRelevantOverrideMakeIrrelevant(activity, otherIndicator);
	}

	public void testActivityDefaultIrrelevantOverrideRelevantMakeRelevant() throws Exception
	{
		strategyActivityRelevancy.verifyStrategyOrActivityDefaultIrrelevantOverrideRelevantMakeRelevant(activity, otherIndicator);
	}
	//endregion

	private Vector<String> extractSetDataCommands(Command[] commandsToClone, ORef ref)
	{
		Vector<String> tags = new Vector<String>();
		for(int i = 0; i < commandsToClone.length; ++i)
		{
			if(commandsToClone[i].getCommandName().equals(CommandSetObjectData.COMMAND_NAME))
			{
				CommandSetObjectData command = (CommandSetObjectData) commandsToClone[i];
				if(command.getObjectORef().equals(ref))
					tags.add(command.getFieldTag());
			}
		}
		return tags;
	}

	private ProjectForTesting project;
	private TestStrategyActivityRelevancy strategyActivityRelevancy;
	private Strategy strategy;
	private Task activity;
	private Indicator indicator;
	private Strategy otherStrategy;
	private Indicator otherIndicator;
}
