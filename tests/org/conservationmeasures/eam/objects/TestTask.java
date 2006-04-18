/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.objects;

import org.conservationmeasures.eam.project.IdAssigner;
import org.conservationmeasures.eam.testall.EAMTestCase;

public class TestTask extends EAMTestCase
{
	public TestTask(String name)
	{
		super(name);
	}

	public void testBasics()
	{
		int id = 5;
		Task task = new Task(id);
		assertEquals("bad id?", id, task.getId());
		
		String label = "Name of task";
		task.setData(Task.TAG_LABEL, label);
		assertEquals("bad label?", label, task.getData(Task.TAG_LABEL));
		
		Task sameTask = new Task(id);
		assertEquals("same ids not equal?", task, sameTask);
		Task otherTask = new Task(id+1);
		otherTask.setData(Task.TAG_LABEL, label);
		assertNotEquals("different ids are equal?", task, otherTask);
	}
	
	public void testNesting() throws Exception
	{
		IdAssigner idAssigner = new IdAssigner();
		Task top = new Task(idAssigner.takeNextId());
		Task child1 = new Task(idAssigner.takeNextId());
		Task child2 = new Task(idAssigner.takeNextId());
		Task grandchild21 = new Task(idAssigner.takeNextId());
		
		top.addSubtask(child1);
		top.addSubtask(child2);
		child2.addSubtask(grandchild21);
		
		assertEquals("wrong subtask count?", 2, top.getSubtaskCount());
		assertEquals("not zero subtasks?", 0, child1.getSubtaskCount());
		assertEquals("wrong child1?", child1.getId(), top.getSubtask(0).getId());
		assertEquals("wrong child2?", child2.getId(), top.getSubtask(1).getId());
	}
}
