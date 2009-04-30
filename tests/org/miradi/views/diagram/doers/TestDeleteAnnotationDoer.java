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
package org.miradi.views.diagram.doers;

import org.miradi.commands.Command;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAMTestCase;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.ProjectForTesting;
import org.miradi.views.diagram.DeleteAnnotationDoer;

public class TestDeleteAnnotationDoer extends EAMTestCase
{
	public TestDeleteAnnotationDoer(String name)
	{
		super(name);
	}

	public void setUp() throws Exception
	{
		super.setUp();
		project = new ProjectForTesting(getName());
	}

	public void tearDown() throws Exception
	{
		super.tearDown();
		project.close();
		project = null;
	}
	
	public void testBuildCommandsToDeleteAnnotation() throws Exception
	{
		ORef strategyRef = project.createFactorAndReturnRef(Strategy.getObjectType());
		ORef indicatorRef1 = project.createFactorAndReturnRef(Indicator.getObjectType());
		ORef indicatorRef2 = project.createFactorAndReturnRef(Indicator.getObjectType());
		ORef methodRef = project.createFactorAndReturnRef(Task.getObjectType());
	
		Strategy strategy = (Strategy) project.findObject(strategyRef);
		Indicator indicator1 = (Indicator) project.findObject(indicatorRef1);
		Indicator indicator2 = (Indicator) project.findObject(indicatorRef2);
		
		CommandSetObjectData addIndicator1 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_INDICATOR_IDS, indicatorRef1.getObjectId());
		project.executeCommand(addIndicator1);
		
		CommandSetObjectData addIndicator2 = CommandSetObjectData.createAppendIdCommand(strategy, Strategy.TAG_INDICATOR_IDS, indicatorRef2.getObjectId());
		project.executeCommand(addIndicator2);
		assertEquals("wrong number of indicators?", 2, strategy.getIndicatorIds().size());
		
		CommandSetObjectData addSharedMethod1 = CommandSetObjectData.createAppendIdCommand(indicator1, Indicator.TAG_METHOD_IDS, methodRef.getObjectId());
		project.executeCommand(addSharedMethod1);
		assertEquals("wrong number of tasks?", 1, indicator1.getMethodRefs().size());
		
		CommandSetObjectData addSharedMethod2 = CommandSetObjectData.createAppendIdCommand(indicator2, Indicator.TAG_METHOD_IDS, methodRef.getObjectId());
		project.executeCommand(addSharedMethod2);
		assertEquals("wrong number of tasks?", 1, indicator2.getMethodRefs().size());
	
		Task method = (Task) project.findObject(methodRef);
		assertTrue("is not method?", method.isMethod());
		
		Command[] commandsToRemoveIndicator1 = DeleteAnnotationDoer.buildCommandsToDeleteAnnotation(project, strategy, Strategy.TAG_INDICATOR_IDS, indicator1);
		project.executeCommandsAsTransaction(commandsToRemoveIndicator1);
		
		Task foundMethod = (Task) project.findObject(methodRef);
		assertEquals("method was deleted?", method.getRef(), foundMethod.getRef());
		
		ORefList referrers = method.findObjectsThatReferToUs(Indicator.getObjectType());
		assertEquals("wrong number of referrers?", 1, referrers.size());
		assertEquals("incorrect referrer?", indicator2.getRef(), referrers.get(0));
		
		Indicator foundIndicator1 = (Indicator) project.findObject(indicatorRef1);
		assertEquals("indicator1 was not deleted?", null, foundIndicator1);
	}
	
	private ProjectForTesting project;
}
