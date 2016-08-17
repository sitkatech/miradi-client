/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.migrations.forward;

import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.TaskSchema;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;

public class MigrationTo26 extends AbstractMigration
{
	public MigrationTo26(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		// decision made not to put child tasks back to their original place in the activity hierarchy
		return MigrationResult.createSuccess();
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();
		AbstractMigrationORefVisitor visitor;
		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			visitor = new PromoteChildTasksVisitor(typeToVisit);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.TASK);

		return typesToMigrate;
	}

	@Override
	protected int getToVersion()
	{
		return VERSION_TO;
	}

	@Override
	protected int getFromVersion()
	{
		return VERSION_FROM;
	}

	@Override
	protected String getDescription()
	{
		return EAM.text("This migration moves child tasks up to the activity level.");
	}

	private class PromoteChildTasksVisitor extends AbstractMigrationORefVisitor
	{
		public PromoteChildTasksVisitor(int typeToVisit)
		{
			type = typeToVisit;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			HashMap<BaseId, BaseId> subTaskIdMap = getOrCreateSubTaskIdToParentIdMap();

			RawObject rawTask = getRawProject().findObject(rawObjectRef);
			if (rawTask != null)
			{
				BaseId taskId = rawObjectRef.getObjectId();
				if (subTaskIdMap.containsKey(taskId))
				{
					BaseId parentTaskId = subTaskIdMap.get(taskId);
					ORef parentTaskRef = new ORef(ObjectType.TASK, parentTaskId);
					RawObject rawParentTask = getRawProject().findObject(parentTaskRef);

					RawObject rawParentActivity = getParentActivity(subTaskIdMap, taskId);

					if (!rawParentTask.equals(rawParentActivity))
					{
						IdList parentTaskSubTaskIdList = new IdList(TaskSchema.getObjectType(), rawParentTask.get(TAG_SUBTASK_IDS));
						if (parentTaskSubTaskIdList.contains(taskId))
						{
							parentTaskSubTaskIdList.removeId(taskId);
							rawParentTask.setData(TAG_SUBTASK_IDS, parentTaskSubTaskIdList.toJson().toString());
						}

						IdList parentActivitySubTaskIdList = new IdList(TaskSchema.getObjectType(), rawParentActivity.get(TAG_SUBTASK_IDS));
						if (!parentActivitySubTaskIdList.contains(taskId))
						{
							parentActivitySubTaskIdList.add(taskId);
							rawParentActivity.setData(TAG_SUBTASK_IDS, parentActivitySubTaskIdList.toJson().toString());
						}
					}
				}
			}

			return MigrationResult.createSuccess();
		}

		private RawObject getParentActivity(HashMap<BaseId, BaseId> subTaskIdMap, BaseId subTaskId)
		{
			BaseId parentTaskId = subTaskIdMap.get(subTaskId);
			if (subTaskIdMap.containsKey(parentTaskId))
				return getParentActivity(subTaskIdMap, parentTaskId);

			ORef parentActivityRef = new ORef(ObjectType.TASK, parentTaskId);
			return getRawProject().findObject(parentActivityRef);
		}

		private HashMap<BaseId, BaseId> getOrCreateSubTaskIdToParentIdMap() throws ParseException
		{
			if (subTaskIdToParentIdMap == null)
				subTaskIdToParentIdMap = createSubTaskIdToParentIdMap();

			return subTaskIdToParentIdMap;
		}

		private HashMap<BaseId, BaseId> createSubTaskIdToParentIdMap() throws ParseException
		{
			HashMap<BaseId, BaseId> subTaskIdMap = new HashMap<BaseId, BaseId>();

			ORefList taskRefs = getRawProject().getAllRefsForType(ObjectType.TASK);
			for (ORef taskRef : taskRefs)
			{
				RawObject rawTask = getRawProject().findObject(taskRef);
				if (rawTask != null && rawTask.containsKey(TAG_SUBTASK_IDS))
				{
					BaseId parentTaskId = taskRef.getObjectId();
					IdList subTaskIdList = new IdList(TaskSchema.getObjectType(), rawTask.get(TAG_SUBTASK_IDS));
					for (int i = 0; i < subTaskIdList.size(); ++i)
					{
						BaseId subTaskId = subTaskIdList.get(i);
						subTaskIdMap.put(subTaskId, parentTaskId);
					}
				}
			}

			return subTaskIdMap;
		}

		private int type;
		private HashMap<BaseId, BaseId> subTaskIdToParentIdMap;
	}

	public static final String TAG_SUBTASK_IDS = "SubtaskIds";

	public static final int VERSION_FROM = 25;
	public static final int VERSION_TO = 26;
}