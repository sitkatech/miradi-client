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
package org.miradi.commands;

import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.ids.IdList;
import org.miradi.ids.TaskId;
import org.miradi.main.TestCaseWithProject;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Task;

public class TestCommandSetObjectData extends TestCaseWithProject
{
	public TestCommandSetObjectData(String name)
	{
		super(name);
	}

	public void testGetReverseCommand() throws Exception
	{
		CommandSetObjectData commandSetObjectData = new CommandSetObjectData(ObjectType.TASK, new BaseId(68), Task.TAG_LABEL, "some value");
		CommandSetObjectData reverseCommand = (CommandSetObjectData) commandSetObjectData.getReverseCommand();
		
		assertEquals("not same type?", commandSetObjectData.getObjectType(), reverseCommand.getObjectType());
		assertEquals("not same id?", commandSetObjectData.getObjectId(), reverseCommand.getObjectId());
		assertEquals("not same tag?", commandSetObjectData.getFieldTag(), reverseCommand.getFieldTag());
		assertEquals("not same value?", commandSetObjectData.getPreviousDataValue(), reverseCommand.getDataValue());
		assertNotNull("not null value?", reverseCommand.getPreviousDataValue());
	}
	
	public void testListInsert() throws Exception
	{
		Task task = new Task(getObjectManager(), new FactorId(39));
		TaskId id1 = new TaskId(75);
		CommandSetObjectData fromEmpty = CommandSetObjectData.createInsertIdCommand(task, Task.TAG_SUBTASK_IDS, id1, 0);
		assertEquals("wrong type?", task.getType(), fromEmpty.getObjectType());
		assertEquals("wrong id?", task.getId(), fromEmpty.getObjectId());
		assertEquals("wrong tag", Task.TAG_SUBTASK_IDS, fromEmpty.getFieldTag());
		IdList expected = new IdList(Task.getObjectType());
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
		Task task = new Task(getObjectManager(), new FactorId(47));
		task.addSubtaskId(new BaseId(12));
		BaseId id2 = new BaseId(99);
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
