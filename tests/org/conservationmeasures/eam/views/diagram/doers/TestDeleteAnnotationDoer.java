package org.conservationmeasures.eam.views.diagram.doers;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.ProjectForTesting;
import org.conservationmeasures.eam.views.diagram.DeleteAnnotationDoer;

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
		assertEquals("wrong number of indicators?", 2, strategy.getIndicators().size());
		
		CommandSetObjectData addSharedMethod1 = CommandSetObjectData.createAppendIdCommand(indicator1, Indicator.TAG_TASK_IDS, methodRef.getObjectId());
		project.executeCommand(addSharedMethod1);
		assertEquals("wrong number of tasks?", 1, indicator1.getMethods().size());
		
		CommandSetObjectData addSharedMethod2 = CommandSetObjectData.createAppendIdCommand(indicator2, Indicator.TAG_TASK_IDS, methodRef.getObjectId());
		project.executeCommand(addSharedMethod2);
		assertEquals("wrong number of tasks?", 1, indicator2.getMethods().size());
	
		Task method = (Task) project.findObject(methodRef);
		assertTrue("is not method?", method.isMethod());
		
		Command[] commandsToRemoveIndicator1 = DeleteAnnotationDoer.buildCommandsToDeleteReferencedObject(project, strategy, Strategy.TAG_INDICATOR_IDS, indicator1);
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
