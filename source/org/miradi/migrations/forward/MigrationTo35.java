/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.MethodSchema;
import org.miradi.schemas.TaskSchema;

import java.util.ArrayList;
import java.util.Vector;

public class MigrationTo35 extends AbstractMigration
{
	public MigrationTo35(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult migrateForward() throws Exception
	{
		return migrate(false);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		return migrate(true);
	}

	private MigrationResult migrate(boolean reverseMigration) throws Exception
	{
		MigrationResult migrationResult = MigrationResult.createUninitializedResult();

		Vector<Integer> typesToVisit = getTypesToMigrate();

		for(Integer typeToVisit : typesToVisit)
		{
			final TaskMethodVisitor visitor = new TaskMethodVisitor(typeToVisit, reverseMigration);
			visitAllORefsInPool(visitor);
			final MigrationResult thisMigrationResult = visitor.getMigrationResult();
			if (migrationResult == null)
				migrationResult = thisMigrationResult;
			else
				migrationResult.merge(thisMigrationResult);
		}

		return migrationResult;
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
		return EAM.text("This migration promotes methods from tasks to 1st class objects.");
	}

	private Vector<Integer> getTypesToMigrate()
	{
		Vector<Integer> typesToMigrate = new Vector<Integer>();
		typesToMigrate.add(ObjectType.INDICATOR);

		return typesToMigrate;
	}

	private class TaskMethodVisitor extends AbstractMigrationORefVisitor
	{
		public TaskMethodVisitor(int typeToVisit, boolean reverseMigration)
		{
			type = typeToVisit;
			isReverseMigration = reverseMigration;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			RawObject rawObject = getRawProject().findObject(rawObjectRef);
			if (rawObject != null)
			{
				if (isReverseMigration)
					migrationResult = moveMethodsToTask(rawObject);
				else
					migrationResult = moveTasksToMethod(rawObject);
			}

			return migrationResult;
		}

		private MigrationResult moveTasksToMethod(RawObject rawIndicator) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			IdList methodIdList = new IdList(MethodSchema.getObjectType(), "");

			if (getRawProject().containsAnyObjectsOfType(ObjectType.TASK))
			{
				for (ORef taskRef : getMethodsOld(rawIndicator))
				{
					RawObject task = getRawProject().findObject(taskRef);
					ORef methodRef = createMethod(task);
					methodIdList.add(methodRef.getObjectId());
					getRawProject().deleteRawObject(taskRef);
				}
			}

			rawIndicator.setData(TAG_METHOD_IDS_NEW, methodIdList.toJson().toString());
			rawIndicator.remove(TAG_METHOD_IDS_OLD);

			return migrationResult;
		}

		private MigrationResult moveMethodsToTask(RawObject rawIndicator) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createSuccess();

			// note slightly different pattern here where xmpz2 reverse migration means that we might have new tag -> existing tasks

			IdList taskIdList = new IdList(TaskSchema.getObjectType(), safeGetTag(rawIndicator, TAG_METHOD_IDS_NEW));

			if (getRawProject().containsAnyObjectsOfType(ObjectType.METHOD))
			{
				for (ORef methodRef : getMethodsNew(rawIndicator))
				{
					RawObject method = getRawProject().findObject(methodRef);
					ORef taskRef = createTask(method);
					taskIdList.removeId(methodRef.getObjectId());
					taskIdList.add(taskRef.getObjectId());
					getRawProject().deleteRawObject(methodRef);
				}
			}

			rawIndicator.setData(TAG_METHOD_IDS_OLD, taskIdList.toJson().toString());
			rawIndicator.remove(TAG_METHOD_IDS_NEW);

			return migrationResult;
		}

		private ArrayList<ORef> getMethodsOld(RawObject rawIndicator) throws Exception
		{
			return getMethods(rawIndicator, TAG_METHOD_IDS_OLD, ObjectType.TASK);
		}

		private ArrayList<ORef> getMethodsNew(RawObject rawIndicator) throws Exception
		{
			return getMethods(rawIndicator, TAG_METHOD_IDS_NEW, ObjectType.METHOD);
		}

		private ArrayList<ORef> getMethods(RawObject rawIndicator, String tag, int objectType) throws Exception
		{
			ArrayList<ORef> methodRefList = new ArrayList<ORef>(){};

			if (rawIndicator.containsKey(tag))
			{
				String methodIdListAsString = rawIndicator.get(tag);
				if (!methodIdListAsString.isEmpty())
				{
					IdList methodIdList = new IdList(objectType, methodIdListAsString);
					for (int i = 0; i < methodIdList.size(); i++)
					{
						BaseId methodId = methodIdList.get(i);
						ORef methodRef = new ORef(objectType, methodId);
						methodRefList.add(methodRef);
					}
				}
			}

			return methodRefList;
		}

		private ORef createMethod(RawObject task)
		{
			ORef newMethodRef = createMethod();
			RawObject method = getRawProject().findObject(newMethodRef);

			method.setData(TAG_ID, safeGetTag(task, TAG_ID));
			method.setData(TAG_SHORT_LABEL, safeGetTag(task, TAG_SHORT_LABEL));
			method.setData(TAG_LABEL, safeGetTag(task, TAG_LABEL));
			method.setData(TAG_DETAILS, safeGetTag(task, TAG_DETAILS));
			method.setData(TAG_COMMENTS, safeGetTag(task, TAG_COMMENTS));

			return newMethodRef;
		}

		private ORef createMethod()
		{
			getRawProject().ensurePoolExists(ObjectType.METHOD);
			return getRawProject().createObject(ObjectType.METHOD);
		}

		private ORef createTask(RawObject method)
		{
			ORef newTaskRef = createTask();
			RawObject task = getRawProject().findObject(newTaskRef);

			task.setData(TAG_ID, safeGetTag(method, TAG_ID));
			task.setData(TAG_SHORT_LABEL, safeGetTag(method, TAG_SHORT_LABEL));
			task.setData(TAG_LABEL, safeGetTag(method, TAG_LABEL));
			task.setData(TAG_DETAILS, safeGetTag(method, TAG_DETAILS));
			task.setData(TAG_COMMENTS, safeGetTag(method, TAG_COMMENTS));

			task.setData(TAG_SUBTASK_IDS, "");
			task.setData(TAG_RESOURCE_ASSIGNMENT_IDS, "");
			task.setData(TAG_EXPENSE_ASSIGNMENT_REFS, "");
			task.setData(TAG_PROGRESS_REPORT_REFS, "");

			return newTaskRef;
		}

		private ORef createTask()
		{
			getRawProject().ensurePoolExists(ObjectType.TASK);
			return getRawProject().createObject(ObjectType.TASK);
		}

		private String safeGetTag(RawObject rawObject, String tag)
		{
			if (rawObject.hasValue(tag))
				return rawObject.getData(tag);

			return "";
		}

		private int type;
		private boolean isReverseMigration;
	}

	public final static String TAG_METHOD_IDS_OLD = "TaskIds";
	public final static String TAG_METHOD_IDS_NEW = "MethodIds";

	public static final String TAG_LABEL = "Label";
	public static final String TAG_ID = "Id";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_COMMENTS = "Comments";
	public static final String TAG_TEXT = "Text";
	public static final String TAG_SHORT_LABEL = "ShortLabel";

	public static final String TAG_SUBTASK_IDS = "SubtaskIds";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";

	public static final int VERSION_FROM = 34;
	public static final int VERSION_TO = 35;
}