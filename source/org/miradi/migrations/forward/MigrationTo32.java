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
import org.miradi.objecthelpers.DateUnit;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MigrationTo32 extends AbstractMigration
{
	public MigrationTo32(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		// TODO: MRD-6013 - do we need to recreate the deleted data...?
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
			visitor = new DeleteSupersededAssignmentsVisitor(typeToVisit);
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
		typesToMigrate.add(ObjectType.STRATEGY);

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
		return EAM.text("This migration removes superseded assignments from the work plan.");
	}

	private class DeleteSupersededAssignmentsVisitor extends AbstractMigrationORefVisitor
	{
		public DeleteSupersededAssignmentsVisitor(int typeToVisit)
		{
			type = typeToVisit;
			assignmentsToDelete = new HashMap<RawObject, RawObject>() {};
		}

		public int getTypeToVisit()
		{
			return type;
		}

		public HashMap<RawObject, RawObject> getAssignmentsToDelete()
		{
			return assignmentsToDelete;
		}

		@Override
		protected MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			MigrationResult migrationResult = MigrationResult.createUninitializedResult();

			// TODO: MRD-6013 - build cache of RawObject -> Assignments / Expenses

			RawObject rawStrategyToMigrate = getRawProject().findObject(rawObjectRef);
			if (rawStrategyToMigrate != null)
			{
				visitAssignments(rawStrategyToMigrate);
				visitExpenses(rawStrategyToMigrate);
				visitTasks(rawStrategyToMigrate);
			}

			if (getAssignmentsToDelete().size() > 0)
			{
				for (Map.Entry<RawObject, RawObject> entry : getAssignmentsToDelete().entrySet())
				{
					migrationResult.addDataLoss(buildDataLossMessage(entry.getKey(), entry.getValue()));
					deleteAssignment(entry.getKey());
				}

				return migrationResult;
			}

			return MigrationResult.createSuccess();
		}

		private void visitAssignments(RawObject rawObjectToVisit) throws Exception
		{
			for (RawObject assignment : getAssignments(rawObjectToVisit))
			{
				if (assignmentIsSuperseded(rawObjectToVisit, assignment))
					getAssignmentsToDelete().put(assignment, rawObjectToVisit);
			}
		}

		private void visitExpenses(RawObject rawObjectToVisit) throws Exception
		{
			for (RawObject expense : getExpenses(rawObjectToVisit))
			{
				if (expenseIsSuperseded(rawObjectToVisit, expense))
					getAssignmentsToDelete().put(expense, rawObjectToVisit);
			}
		}

		private void visitTasks(RawObject rawObjectToVisit) throws Exception
		{
			for (RawObject task : getChildTasks(rawObjectToVisit))
			{
				visitAssignments(task);
				visitExpenses(task);
				visitTasks(task);
			}
		}

		private ArrayList<RawObject> getAssignments(RawObject rawObject) throws Exception
		{
			ArrayList<RawObject> assignmentList = new ArrayList<RawObject>(){};

			if (rawObject.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS))
			{
				IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), rawObject.getData(TAG_RESOURCE_ASSIGNMENT_IDS));
				for (int i = 0; i < assignmentIdList.size(); i++)
				{
					BaseId assignmentId = assignmentIdList.get(i);
					ORef assignmentRef = new ORef(ObjectType.RESOURCE_ASSIGNMENT, assignmentId);
					RawObject assignment = getRawProject().findObject(assignmentRef);
					assignmentList.add(assignment);
				}
			}

			return assignmentList;
		}

		private ArrayList<RawObject> getExpenses(RawObject rawObject) throws Exception
		{
			ArrayList<RawObject> expenseList = new ArrayList<RawObject>(){};

			if (rawObject.containsKey(TAG_EXPENSE_ASSIGNMENT_REFS))
			{
				ORefList expenseRefList = new ORefList(rawObject.getData(TAG_EXPENSE_ASSIGNMENT_REFS));
				for (ORef expenseRef : expenseRefList)
				{
					RawObject expense = getRawProject().findObject(expenseRef);
					expenseList.add(expense);
				}
			}

			return expenseList;
		}

		private ArrayList<RawObject> getChildTasks(RawObject rawObject) throws Exception
		{
			ArrayList<RawObject> taskList = new ArrayList<RawObject>(){};

			String taskIdsTag = getChildTasksTag(rawObject);
			if (rawObject.containsKey(taskIdsTag))
			{
				IdList taskIdList = new IdList(TaskSchema.getObjectType(), rawObject.get(taskIdsTag));
				for (int i = 0; i < taskIdList.size(); i++)
				{
					BaseId taskId = taskIdList.get(i);
					ORef taskRef = new ORef(ObjectType.TASK, taskId);
					RawObject task = getRawProject().findObject(taskRef);
					taskList.add(task);
				}
			}

			return taskList;
		}

		private DateUnitEffortList getDateUnitEffortList(RawObject rawObject) throws Exception
		{
			DateUnitEffortList dateUnitEffortList = new DateUnitEffortList();

			String dateUnitEffortAsString =  safeGetTag(rawObject, TAG_DATEUNIT_EFFORTS);
			if (dateUnitEffortAsString != null && dateUnitEffortAsString.length() > 0)
				dateUnitEffortList = new DateUnitEffortList(dateUnitEffortAsString);

			return dateUnitEffortList;
		}

		private boolean assignmentIsSuperseded(RawObject rawObject, RawObject assignment) throws Exception
		{
			ArrayList<RawObject> childTasks = getChildTasks(rawObject);
			for (RawObject task : childTasks)
			{
				ArrayList<RawObject> childAssignments = getAssignments(task);
				for (RawObject childAssignment : childAssignments)
				{
					if (dateUnitContained(assignment, childAssignment))
						return true;

					return assignmentIsSuperseded(task, assignment);
				}
			}

			return false;
		}

		private boolean expenseIsSuperseded(RawObject rawObject, RawObject expense) throws Exception
		{
			ArrayList<RawObject> childTasks = getChildTasks(rawObject);
			for (RawObject task : childTasks)
			{
				ArrayList<RawObject> childExpenses = getExpenses(task);
				for (RawObject childExpense : childExpenses)
				{
					if (dateUnitContained(expense, childExpense))
						return true;

					return expenseIsSuperseded(task, expense);
				}
			}

			return false;
		}

		private boolean dateUnitContained(RawObject parentAssignment, RawObject childAssignment) throws Exception
		{
			DateUnitEffortList parentDateUnitEffortList = getDateUnitEffortList(parentAssignment);
			DateUnitEffortList childDateUnitEffortList = getDateUnitEffortList(childAssignment);

			for (int i = 0; i < parentDateUnitEffortList.size(); i++)
			{
				DateUnitEffort parentDateUnitEffort = parentDateUnitEffortList.getDateUnitEffort(i);
				DateUnit parentDateUnit = parentDateUnitEffort.getDateUnit();

				for (int j = 0; j < childDateUnitEffortList.size(); j++)
				{
					DateUnitEffort childDateUnitEffort = childDateUnitEffortList.getDateUnitEffort(j);
					DateUnit childDateUnit = childDateUnitEffort.getDateUnit();
					if (childDateUnit.contains(parentDateUnit))
						return true;
				}
			}

			return false;
		}

		private void deleteAssignment(RawObject assignmentToDelete)
		{
			// TODO: MRD-6013 - delete the assignment
		}

		private String buildDataLossMessage(RawObject assignmentToDelete, RawObject parent) throws Exception
		{
			String name = safeGetTag(parent, TAG_LABEL);
			return EAM.text("superseded ") + getUserFriendlyObjectName(assignmentToDelete) + " for " + getUserFriendlyObjectName(parent) + " '" + name + EAM.text("'.");
		}

		private String getUserFriendlyObjectName(RawObject rawObject) throws Exception
		{
			switch(rawObject.getObjectType())
			{
				case ObjectType.STRATEGY:
					return EAM.text("Strategy");
				case ObjectType.TASK:
					return EAM.text("Activity");
				case ObjectType.RESOURCE_ASSIGNMENT:
					return EAM.text("Assignment");
				case ObjectType.EXPENSE_ASSIGNMENT:
					return EAM.text("Expense");
			}

			throw new Exception("getUserFriendlyObjectName called for unexpected object type " + rawObject.getObjectType());
		}

		private String getChildTasksTag(RawObject rawObject) throws Exception
		{
			switch(rawObject.getObjectType())
			{
				case ObjectType.STRATEGY:
					return TAG_ACTIVITY_IDS;
				case ObjectType.TASK:
					return TAG_SUBTASK_IDS;
			}

			throw new Exception("getChildTasksTag called for unexpected object type " + rawObject.getObjectType());
		}

		private String safeGetTag(RawObject rawObject, String tag)
		{
			if (rawObject.hasValue(tag))
				return rawObject.getData(tag);

			return "";
		}

		private int type;
		private HashMap<RawObject, RawObject> assignmentsToDelete;
	}

	public static final String TAG_LABEL = "Label";
	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public static final String TAG_SUBTASK_IDS = "SubtaskIds";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public static final String TAG_DATEUNIT_EFFORTS = "Details";

	public static final int VERSION_FROM = 31;
	public static final int VERSION_TO = 32;
}