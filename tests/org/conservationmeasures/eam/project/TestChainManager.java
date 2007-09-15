/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAMTestCase;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Task;

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
		BaseId factorId = project.createFactorAndReturnId(ObjectType.CAUSE);
		BaseId indicatorId = project.addItemToFactorList(factorId, ObjectType.INDICATOR, Factor.TAG_INDICATOR_IDS);
		BaseId taskId1 = project.addItemToIndicatorList(indicatorId, ObjectType.TASK, Indicator.TAG_TASK_IDS);
		BaseId taskId2 = project.addItemToTaskList(taskId1, ObjectType.TASK, Task.TAG_SUBTASK_IDS);
		ChainManager cm = new ChainManager(project);
		BaseObject owner1 = cm.getProject().findObject(new ORef(ObjectType.TASK, taskId2));
		Factor owner = owner1.getDirectOrIndirectOwningFactor();
		assertEquals(owner.getId(),factorId);
		
		BaseId loopTaskId1 = project.createObjectAndReturnId(ObjectType.TASK);
		BaseId loopTaskId2 = project.addItemToTaskList(loopTaskId1, ObjectType.TASK, Task.TAG_SUBTASK_IDS);
		IdList idList = new IdList(new BaseId[] {loopTaskId1});
		project.setObjectData(ObjectType.TASK, loopTaskId2, Task.TAG_SUBTASK_IDS, idList.toString());
		BaseObject owner2 = cm.getProject().findObject(new ORef(ObjectType.TASK, loopTaskId2));
		
		Factor ownerLoop = owner2.getDirectOrIndirectOwningFactor();
		assertEquals(null,ownerLoop);
		
	}

	ProjectForTesting project;
}
