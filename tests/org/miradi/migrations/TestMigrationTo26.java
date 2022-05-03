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

package org.miradi.migrations;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.migrations.forward.MigrationTo26;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.schemas.TaskSchema;

import static org.miradi.objects.Strategy.TAG_ACTIVITY_IDS;

public class TestMigrationTo26 extends AbstractTestMigration
{
	public TestMigrationTo26(String name)
	{
		super(name);
	}
	
	public void testChildTasksMovedByForwardMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task activity = getProject().createTask(strategy);
		Task childTask = getProject().createTask(activity);
		Task grandchildTask = getProject().createTask(childTask);

		assertEquals(strategy.getActivityRefs(), new ORefList(activity.getRef()));
		assertEquals(activity.getChildTaskRefs(), new ORefList(childTask.getRef()));
		assertEquals(childTask.getChildTaskRefs(), new ORefList(grandchildTask.getRef()));

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo26.VERSION_TO));
		migrateProject(rawProject, new VersionRange(Project.VERSION_HIGH));

		RawPool strategyPool = rawProject.getRawPoolForType(ObjectType.STRATEGY);
		assertNotNull(strategyPool);
		assertEquals(strategyPool.keySet().size(), 1);
		RawObject rawStrategy = strategyPool.get(strategyPool.keySet().toArray()[0]);
		assertNotNull(rawStrategy);
		assertTrue(rawStrategy.containsKey(TAG_ACTIVITY_IDS));
		IdList activityIdList = new IdList(TaskSchema.getObjectType(), rawStrategy.get(TAG_ACTIVITY_IDS));
		assertEquals(activityIdList, new IdList(activity));

		RawPool taskPool = rawProject.getRawPoolForType(ObjectType.TASK);
		assertNotNull(taskPool);
		assertEquals(taskPool.keySet().size(), 3);
		for(ORef taskRef : taskPool.keySet())
		{
			RawObject rawTask = taskPool.get(taskRef);
			BaseId taskId = taskRef.getObjectId();
			if (taskId.equals(activity.getId()))
			{
				IdList childTaskIdList = new IdList(TaskSchema.getObjectType(), rawTask.get(MigrationTo26.TAG_SUBTASK_IDS));
				assertEquals(childTaskIdList, new IdList(ObjectType.TASK, new BaseId[]{childTask.getId(), grandchildTask.getId()}));
			}
			if (taskId.equals(childTask.getId()))
			{
				IdList grandchildTaskIdList = new IdList(TaskSchema.getObjectType(), rawTask.get(MigrationTo26.TAG_SUBTASK_IDS));
				assertEquals(grandchildTaskIdList, new IdList(ObjectType.TASK, new BaseId[]{}));
			}
			if (taskId.equals(grandchildTask.getId()))
			{
				assertFalse(rawTask.containsKey(MigrationTo26.TAG_SUBTASK_IDS));
			}
		}
	}

	public void testChildTasksNotMovedByReverseMigration() throws Exception
	{
		Strategy strategy = getProject().createStrategy();
		Task activity = getProject().createTask(strategy);
		Task childTask = getProject().createTask(activity);
		Task grandchildTask = getProject().createTask(childTask);

		assertEquals(strategy.getActivityRefs(), new ORefList(activity.getRef()));
		assertEquals(activity.getChildTaskRefs(), new ORefList(childTask.getRef()));
		assertEquals(childTask.getChildTaskRefs(), new ORefList(grandchildTask.getRef()));

		RawProject rawProject = reverseMigrate(new VersionRange(MigrationTo26.VERSION_TO));

		RawPool strategyPool = rawProject.getRawPoolForType(ObjectType.STRATEGY);
		assertNotNull(strategyPool);
		assertEquals(strategyPool.keySet().size(), 1);
		RawObject rawStrategy = strategyPool.get(strategyPool.keySet().toArray()[0]);
		assertNotNull(rawStrategy);
		assertTrue(rawStrategy.containsKey(TAG_ACTIVITY_IDS));
		IdList activityIdList = new IdList(TaskSchema.getObjectType(), rawStrategy.get(TAG_ACTIVITY_IDS));
		assertEquals(activityIdList, new IdList(activity));

		RawPool taskPool = rawProject.getRawPoolForType(ObjectType.TASK);
		assertNotNull(taskPool);
		assertEquals(taskPool.keySet().size(), 3);
		for(ORef taskRef : taskPool.keySet())
		{
			RawObject rawTask = taskPool.get(taskRef);
			BaseId taskId = taskRef.getObjectId();
			if (taskId.equals(activity.getId()))
			{
				IdList childTaskIdList = new IdList(TaskSchema.getObjectType(), rawTask.get(MigrationTo26.TAG_SUBTASK_IDS));
				assertEquals(childTaskIdList, new IdList(ObjectType.TASK, new BaseId[]{childTask.getId()}));
			}
			if (taskId.equals(childTask.getId()))
			{
				IdList grandchildTaskIdList = new IdList(TaskSchema.getObjectType(), rawTask.get(MigrationTo26.TAG_SUBTASK_IDS));
				assertEquals(grandchildTaskIdList, new IdList(ObjectType.TASK, new BaseId[]{grandchildTask.getId()}));
			}
			if (taskId.equals(grandchildTask.getId()))
			{
				assertFalse(rawTask.containsKey(MigrationTo26.TAG_SUBTASK_IDS));
			}
		}
	}

	@Override
	protected int getFromVersion()
	{
		return MigrationTo26.VERSION_FROM;
	}
	
	@Override
	protected int getToVersion()
	{
		return MigrationTo26.VERSION_TO;
	}
}
