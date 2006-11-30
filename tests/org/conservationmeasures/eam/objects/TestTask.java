/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdAssigner;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.objecthelpers.CreateTaskParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;

public class TestTask extends ObjectTestCase
{
	public TestTask(String name)
	{
		super(name);
	}

	public void testBasics() throws Exception
	{
		CreateTaskParameter extraInfo = getTaskExtraInfo();
		verifyFields(ObjectType.TASK, extraInfo);
		BaseId id = new BaseId(5);
		
		Task task = new Task(id, extraInfo);
		assertEquals("bad id?", id, task.getId());
		
		String label = "Name of task";
		task.setData(Task.TAG_LABEL, label);
		assertEquals("bad label?", label, task.getData(Task.TAG_LABEL));
		
		Task sameTask = new Task(id, extraInfo);
		assertEquals("same ids not equal?", task, sameTask);
		Task otherTask = new Task(new BaseId(id.asInt()+1), extraInfo);
		otherTask.setData(Task.TAG_LABEL, label);
		assertNotEquals("different ids are equal?", task, otherTask);
	}
	
	public void testData() throws Exception
	{
		IdList sampleIds = new IdList();
		CreateTaskParameter extraInfo = getTaskExtraInfo();
		sampleIds.add(1);
		sampleIds.add(1527);
		String sampleIdData = sampleIds.toString(); 
		Task task = new Task(new BaseId(0), extraInfo);
		task.setData(Task.TAG_SUBTASK_IDS, sampleIdData);
		assertEquals("bad data?", sampleIdData, task.getData(Task.TAG_SUBTASK_IDS));
	}
	
	public void testNesting() throws Exception
	{
		CreateTaskParameter extraInfo = getTaskExtraInfo();
		IdAssigner idAssigner = new IdAssigner();
		Task top = new Task(idAssigner.takeNextId(), extraInfo);
		Task child1 = new Task(idAssigner.takeNextId(), extraInfo);
		Task child2 = new Task(idAssigner.takeNextId(), extraInfo);
		Task grandchild21 = new Task(idAssigner.takeNextId(), extraInfo);
		
		top.addSubtaskId(child1.getId());
		top.addSubtaskId(child2.getId());
		child2.addSubtaskId(grandchild21.getId());
		
		assertEquals("wrong subtask count?", 2, top.getSubtaskCount());
		assertEquals("not zero subtasks?", 0, child1.getSubtaskCount());
		assertEquals("wrong child1?", child1.getId(), top.getSubtaskId(0));
		assertEquals("wrong child2?", child2.getId(), top.getSubtaskId(1));
	}
	
	public void testSubtaskIdList() throws Exception
	{
		Task parent = createBasicTree();
		
		IdList ids = parent.getSubtaskIdList();
		assertEquals("wrong count?", 2, ids.size());
		assertEquals("wrong 1?", parent.getSubtaskId(0), ids.get(0));
		assertEquals("wrong 2?", parent.getSubtaskId(1), ids.get(1));
		
		IdList shouldBeCopy = parent.getSubtaskIdList();
		shouldBeCopy.add(2727);
		assertEquals("modified the actual list?", 2, parent.getSubtaskIdList().size());
	}
	
	public void testResourceIdList() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();
		CreateTaskParameter extraInfo = getTaskExtraInfo();
		Task task = new Task(idAssigner.takeNextId(), extraInfo);
		assertEquals("not empty to start?", 0, task.getResourceCount());
		
		IdList resources = new IdList();
		int[] resourceIds = {7, 43, 99};
		for(int i = 0; i < resourceIds.length; ++i)
			resources.add(resourceIds[i]);
		
		task.setData(Task.TAG_RESOURCE_IDS, resources.toString());
		assertEquals("didn't update count?", resources.size(), task.getResourceCount());
		assertEquals("wrong list?", resources, task.getResourceIdList());
		assertEquals("can't get?", resources.toString(), task.getData(Task.TAG_RESOURCE_IDS));
		
		Task got = (Task)EAMBaseObject.createFromJson(task.getType(), task.toJson());
		assertEquals("didn't jsonize?", resources, got.getResourceIdList());
	}

	public void testJson() throws Exception
	{
		Task parent = createBasicTree();
		
		Task got = (Task)EAMBaseObject.createFromJson(parent.getType(), parent.toJson());
		assertEquals("wrong count?", parent.getSubtaskCount(), got.getSubtaskCount());
	}

	private Task createBasicTree() throws Exception
	{
		CreateTaskParameter extraInfo = getTaskExtraInfo();
		Task parent = new Task(new BaseId(1), extraInfo);
		Task child1 = new Task(new BaseId(2), extraInfo);
		Task child2 = new Task(new BaseId(3), extraInfo);
		parent.addSubtaskId(child1.getId());
		parent.addSubtaskId(child2.getId());
		return parent;
	}
	
	public void testExtraInfo() throws Exception
	{
		Task task = new Task(BaseId.INVALID, getTaskExtraInfo());
		ORef parentRef = ((CreateTaskParameter)task.getCreationExtraInfo()).getParentRef();
		assertEquals("not same parent?", getTaskExtraInfo().getParentRef(), parentRef);
	}

	private CreateTaskParameter getTaskExtraInfo()
	{
		ORef parentRef = new ORef(ObjectType.MODEL_NODE, new BaseId(45));
		CreateTaskParameter extraInfo = new CreateTaskParameter(parentRef);
		return extraInfo;
	}
	
}
