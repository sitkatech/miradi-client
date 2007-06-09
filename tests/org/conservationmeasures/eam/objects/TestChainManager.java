/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ChainManager;
import org.conservationmeasures.eam.project.ProjectForTesting;

public class TestChainManager extends EAMTestCase
{
	public TestChainManager(String name)
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
	}
	
	public void testChain() throws Exception
	{
		BaseId factorId = project.createFactor(ObjectType.CAUSE);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId taskId1 = project.addItemToIndicatorList(indicatorId, ObjectType.TASK, Indicator.TAG_TASK_IDS);
		BaseId taskId2 = project.addItemToTaskList(taskId1, ObjectType.TASK, Task.TAG_SUBTASK_IDS);
		ChainManager cm = new ChainManager(project);
		Factor owner = cm.getDirectOrIndirectOwningFactor(new ORef(ObjectType.TASK, taskId2));
		assertEquals(owner.getId(),factorId);
		
		BaseId loopTaskId1 = project.createObject(ObjectType.TASK);
		BaseId loopTaskId2 = project.addItemToTaskList(loopTaskId1, ObjectType.TASK, Task.TAG_SUBTASK_IDS);
		IdList idList = new IdList(new BaseId[] {loopTaskId1});
		project.setObjectData(ObjectType.TASK, loopTaskId2, Task.TAG_SUBTASK_IDS, idList.toString());
		
		//TODO: the code for a timeout threat to wait for a second threat and time it out is known 
		// but I am not sure if we wish to go that far here.
		cm.getDirectOrIndirectOwningFactor(new ORef(ObjectType.TASK, loopTaskId2));
		
	}

	ProjectForTesting project;
}
