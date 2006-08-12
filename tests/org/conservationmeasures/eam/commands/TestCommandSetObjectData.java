/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.commands;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.IdList;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestCommandSetObjectData extends EAMTestCase
{
	public TestCommandSetObjectData(String name)
	{
		super(name);
	}

	public void testListInsert() throws Exception
	{
		Task task = new Task(new BaseId(39));
		BaseId id1 = new BaseId(75);
		CommandSetObjectData fromEmpty = CommandSetObjectData.createInsertIdCommand(task, Task.TAG_SUBTASK_IDS, id1, 0);
		assertEquals("wrong type?", task.getType(), fromEmpty.getObjectType());
		assertEquals("wrong id?", task.getId(), fromEmpty.getObjectId());
		assertEquals("wrong tag", Task.TAG_SUBTASK_IDS, fromEmpty.getFieldTag());
		IdList expected = new IdList();
		expected.add(id1);
		assertEquals("wrong data value?", expected.toString(), fromEmpty.getDataValue());
		task.addSubtaskId(id1);
		
		BaseId id2 = new BaseId(101);
		CommandSetObjectData insertAnother = CommandSetObjectData.createInsertIdCommand(task, Task.TAG_SUBTASK_IDS, id2, 0);
		expected.insertAt(id2, 0);
		assertEquals("didn't insert to front of list?", expected.toString(), insertAnother.getDataValue());
	}
	
	public void testListRemove() throws Exception
	{
		BaseId id2 = new BaseId(99);
		Task task = new Task(new BaseId(47));
		task.addSubtaskId(new BaseId(12));
		task.addSubtaskId(id2);
		task.addSubtaskId(new BaseId(747));
		CommandSetObjectData removeMiddle = CommandSetObjectData.createRemoveIdCommand(task, Task.TAG_SUBTASK_IDS, id2);
		assertEquals("wrong type?", task.getType(), removeMiddle.getObjectType());
		assertEquals("wrong id?", task.getId(), removeMiddle.getObjectId());
		assertEquals("wrong tag", Task.TAG_SUBTASK_IDS, removeMiddle.getFieldTag());
		IdList expected = task.getSubtaskIdList();
		expected.removeId(id2);
		assertEquals("didn't remove correctly?", expected.toString(), removeMiddle.getDataValue());
	}
}
