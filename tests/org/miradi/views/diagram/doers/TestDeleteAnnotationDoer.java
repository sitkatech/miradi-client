/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
package org.miradi.views.diagram.doers;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.MiradiTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.Method;
import org.miradi.objects.Strategy;
import org.miradi.project.ProjectForTesting;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.MethodSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class TestDeleteAnnotationDoer extends MiradiTestCase
{
	public TestDeleteAnnotationDoer(String name)
	{
		super(name);
	}

	@Override
	public void setUp() throws Exception
	{
		super.setUp();
		project = ProjectForTesting.createProjectWithDefaultObjects(getName());
	}

	@Override
	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testBuildCommandsToDeleteAnnotation() throws Exception
	{
		ORef strategyRef = project.createObject(StrategySchema.getObjectType());
		ORef indicatorRef1 = project.createObject(IndicatorSchema.getObjectType());
		ORef indicatorRef2 = project.createObject(IndicatorSchema.getObjectType());
		ORef methodRef = project.createObject(MethodSchema.getObjectType());
	
		Strategy strategy = (Strategy) project.findObject(strategyRef);
		Indicator indicator1 = (Indicator) project.findObject(indicatorRef1);
		Indicator indicator2 = (Indicator) project.findObject(indicatorRef2);
		
		CommandSetObjectData addIndicator1 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_INDICATOR_IDS, indicatorRef1.getObjectId());
		project.executeCommand(addIndicator1);
		
		CommandSetObjectData addIndicator2 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_INDICATOR_IDS, indicatorRef2.getObjectId());
		project.executeCommand(addIndicator2);
		assertEquals("wrong number of indicators?", 2, strategy.getOnlyDirectIndicatorIds().size());
		
		CommandSetObjectData addSharedMethod1 = CommandSetObjectData.createAppendIdCommand(indicator1, Indicator.TAG_METHOD_IDS, methodRef.getObjectId());
		project.executeCommand(addSharedMethod1);
		assertEquals("wrong number of tasks?", 1, indicator1.getMethodRefs().size());
		
		CommandSetObjectData addSharedMethod2 = CommandSetObjectData.createAppendIdCommand(indicator2, Indicator.TAG_METHOD_IDS, methodRef.getObjectId());
		project.executeCommand(addSharedMethod2);
		assertEquals("wrong number of tasks?", 1, indicator2.getMethodRefs().size());

		Method method = (Method) project.findObject(methodRef);
		assertNotNull(method);

		Command[] commandsToRemoveIndicator1 = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(project, strategy, Strategy.TAG_INDICATOR_IDS, indicator1);
		project.executeCommands(commandsToRemoveIndicator1);

		Method foundMethod = (Method) project.findObject(methodRef);
		assertEquals("method was deleted?", method.getRef(), foundMethod.getRef());
		
		ORefList referrers = method.findObjectsThatReferToUs(IndicatorSchema.getObjectType());
		assertEquals("wrong number of referrers?", 1, referrers.size());
		assertEquals("incorrect referrer?", indicator2.getRef(), referrers.get(0));
		
		Indicator foundIndicator1 = (Indicator) project.findObject(indicatorRef1);
		assertEquals("indicator1 was not deleted?", null, foundIndicator1);
	}
	
	private ProjectForTesting project;
}
