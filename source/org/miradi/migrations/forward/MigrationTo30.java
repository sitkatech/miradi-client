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
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.ResourcePlanSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Vector;

public class MigrationTo30 extends AbstractMigration
{
	public MigrationTo30(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		// decision made not to try and undo split of shared activities
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
			visitor = new SplitSharedActivitiesVisitor(typeToVisit);
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
		return EAM.text("This migration splits shared activities out to separate activity entries.");
	}

	private class SplitSharedActivitiesVisitor extends AbstractMigrationORefVisitor
	{
		public SplitSharedActivitiesVisitor(int typeToVisit)
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
			HashMap<BaseId, RawObject> processedTaskMap = new HashMap<BaseId, RawObject>();

			RawObject strategyToMigrate = getRawProject().findObject(rawObjectRef);
			if (strategyToMigrate != null && strategyToMigrate.containsKey(TAG_ACTIVITY_IDS))
			{
				IdList taskIdList = new IdList(TaskSchema.getObjectType(), strategyToMigrate.get(TAG_ACTIVITY_IDS));
				for (int i = 0; i < taskIdList.size(); i++)
				{
					BaseId taskId = taskIdList.get(i);
					if (!processedTaskMap.containsKey(taskId))
					{
						ORef taskRef = new ORef(ObjectType.TASK, taskId);
						RawObject rawTask = visitTask(strategyToMigrate, taskRef);
						processedTaskMap.put(taskId, rawTask);
					}
				}
			}

			return MigrationResult.createSuccess();
		}

		private RawObject visitTask(RawObject strategyToMigrate, ORef taskRef) throws Exception
		{
			HashMap<BaseId, IdList> taskIdMap = getOrCreateSubTaskIdToStrategyIdMap();

			BaseId taskId = taskRef.getObjectId();
			RawObject rawTask = getRawProject().findObject(taskRef);

			if (taskIdMap.containsKey(taskId))
			{
				IdList strategyIdList = taskIdMap.get(taskId);
				int strategyCount = strategyIdList.toIntArray().length;
				if (strategyCount > 1)
				{
					// task referred to by > 1 strategy - needs to be migrated
					ORefList strategyRefs = new ORefList(ObjectType.STRATEGY, strategyIdList);
					strategyRefs.sort();

					for (int j = 0; j < strategyRefs.size(); j++)
					{
						RawObject strategy = getRawProject().findObject(strategyRefs.get(j));

						if (!strategy.equals(strategyToMigrate))
						{
							// clone original task and all its children
							ORef newTaskRef = cloneTask(rawTask, strategyCount);

							// remove original task Id from / add new cloned task Id to TAG_ACTIVITY_IDS for strategy
							IdList strategyTaskIdList = new IdList(TaskSchema.getObjectType(), strategy.get(TAG_ACTIVITY_IDS));
							strategyTaskIdList.removeId(taskId);
							strategyTaskIdList.add(newTaskRef.getObjectId());
							strategy.setData(TAG_ACTIVITY_IDS, strategyTaskIdList.toJson().toString());
						}
					}

					// now we can proportion the original task and its sub-tasks accordingly
					proportionAssignments(rawTask, strategyCount);
					proportionExpenses(rawTask, strategyCount);
					proportionSubTasks(rawTask, strategyCount);
				}
			}

			return rawTask;
		}

		private void proportionSubTasks(RawObject rawTask, int proportionShare) throws Exception
		{
			if (rawTask.hasValue(TAG_SUBTASK_IDS))
			{
				IdList subTaskIdList = new IdList(TaskSchema.getObjectType(), rawTask.getData(TAG_SUBTASK_IDS));
				for (int i = 0; i < subTaskIdList.size(); i++)
				{
					BaseId subTaskId = subTaskIdList.get(i);
					ORef subTaskRef = new ORef(ObjectType.TASK, subTaskId);
					RawObject subTask = getRawProject().findObject(subTaskRef);

					proportionAssignments(subTask, proportionShare);
					proportionExpenses(subTask, proportionShare);
				}
			}
		}

		private ORef cloneTask(RawObject taskToClone, int proportionShare) throws Exception
		{
			ORef newTaskRef = getRawProject().createObject(ObjectType.TASK);
			RawObject newTask = getRawProject().findObject(newTaskRef);

			newTask.setData(TAG_ID, safeGetTag(taskToClone, TAG_ID));

			String taskLabel =  safeGetTag(taskToClone, TAG_LABEL);
			if (taskLabel != null && taskLabel.length() > 0)
				newTask.setData(TAG_LABEL, "Copy of " + taskLabel);

			newTask.setData(TAG_ID, safeGetTag(taskToClone, TAG_ID));
			newTask.setData(TAG_DETAILS, safeGetTag(taskToClone, TAG_DETAILS));
			newTask.setData(TAG_IS_MONITORING_ACTIVITY, safeGetTag(taskToClone, TAG_IS_MONITORING_ACTIVITY));
			newTask.setData(TAG_RELEVANT_INDICATOR_SET, safeGetTag(taskToClone, TAG_RELEVANT_INDICATOR_SET));
			newTask.setData(TAG_PLANNED_LEADER_RESOURCE, safeGetTag(taskToClone, TAG_PLANNED_LEADER_RESOURCE));
			newTask.setData(TAG_ASSIGNED_LEADER_RESOURCE, safeGetTag(taskToClone, TAG_ASSIGNED_LEADER_RESOURCE));
			newTask.setData(TAG_TAXONOMY_CLASSIFICATION_CONTAINER, safeGetTag(taskToClone, TAG_TAXONOMY_CLASSIFICATION_CONTAINER));

			newTask.setData(TAG_COMMENTS, safeGetTag(taskToClone, TAG_COMMENTS));
			newTask.setData(TAG_TEXT, safeGetTag(taskToClone, TAG_TEXT));
			newTask.setData(TAG_SHORT_LABEL, safeGetTag(taskToClone, TAG_SHORT_LABEL));
			newTask.setData(TAG_INDICATOR_IDS, safeGetTag(taskToClone, TAG_INDICATOR_IDS));
			newTask.setData(TAG_OBJECTIVE_IDS, safeGetTag(taskToClone, TAG_OBJECTIVE_IDS));

			ORefList clonedSubTaskRefs = cloneSubTasks(taskToClone, proportionShare);
			if (!clonedSubTaskRefs.isEmpty())
			{
				IdList subTaskIdList = clonedSubTaskRefs.convertToIdList(ObjectType.TASK);
				newTask.setData(TAG_SUBTASK_IDS, subTaskIdList.toJson().toString());
			}

			ORefList clonedResourcePlanRefs = cloneResourcePlans(taskToClone);
			if (!clonedResourcePlanRefs.isEmpty())
			{
				IdList resourcePlanIdList = clonedResourcePlanRefs.convertToIdList(ObjectType.RESOURCE_PLAN);
				newTask.setData(TAG_RESOURCE_PLAN_IDS, resourcePlanIdList.toJson().toString());
			}

			ORefList clonedProgressReportRefs = cloneProgressReports(taskToClone);
			if (!clonedProgressReportRefs.isEmpty())
				newTask.setData(TAG_PROGRESS_REPORT_REFS, clonedProgressReportRefs.toJson().toString());

			ORefList clonedAssignmentRefs = cloneAssignments(taskToClone, proportionShare);
			if (!clonedAssignmentRefs.isEmpty())
			{
				IdList assignmentIdList = clonedAssignmentRefs.convertToIdList(ObjectType.RESOURCE_ASSIGNMENT);
				newTask.setData(TAG_RESOURCE_ASSIGNMENT_IDS, assignmentIdList.toJson().toString());
			}

			ORefList clonedExpenseRefs = cloneExpenses(taskToClone, proportionShare);
			if (!clonedExpenseRefs.isEmpty())
				newTask.setData(TAG_EXPENSE_ASSIGNMENT_REFS, clonedExpenseRefs.toJson().toString());

			return newTaskRef;
		}

		private ORefList cloneSubTasks(RawObject taskToClone, int proportionShare) throws Exception
		{
			ORefList subTaskRefs = new ORefList();

			if (taskToClone.hasValue(TAG_SUBTASK_IDS))
			{
				IdList subTaskIdList = new IdList(TaskSchema.getObjectType(), taskToClone.getData(TAG_SUBTASK_IDS));
				for (int i = 0; i < subTaskIdList.size(); i++)
				{
					BaseId subTaskId = subTaskIdList.get(i);
					ORef subTaskRef = new ORef(ObjectType.TASK, subTaskId);
					RawObject subTaskToClone = getRawProject().findObject(subTaskRef);

					ORef newSubTaskRef = cloneTask(subTaskToClone, proportionShare);
					subTaskRefs.add(newSubTaskRef);
				}
			}

			return subTaskRefs;
		}

		private ORefList cloneResourcePlans(RawObject taskToClone) throws Exception
		{
			ORefList resourcePlanRefs = new ORefList();

			if (taskToClone.hasValue(TAG_RESOURCE_PLAN_IDS))
			{
				IdList resourcePlanIdList = new IdList(ResourcePlanSchema.getObjectType(), taskToClone.getData(TAG_RESOURCE_PLAN_IDS));
				for (int i = 0; i < resourcePlanIdList.size(); i++)
				{
					BaseId resourcePlanId = resourcePlanIdList.get(i);
					ORef resourcePlanRef = new ORef(ObjectType.RESOURCE_PLAN, resourcePlanId);
					RawObject resourcePlanToClone = getRawProject().findObject(resourcePlanRef);

					ORef newResourcePlanRef = cloneResourcePlan(resourcePlanToClone);
					resourcePlanRefs.add(newResourcePlanRef);
				}
			}

			return resourcePlanRefs;
		}

		private ORef cloneResourcePlan(RawObject resourcePlanToClone) throws Exception
		{
			ORef newResourcePlanRef = getRawProject().createObject(ObjectType.RESOURCE_PLAN);
			RawObject newResourcePlan = getRawProject().findObject(newResourcePlanRef);

			newResourcePlan.setData(TAG_RESOURCE_ID, safeGetTag(resourcePlanToClone, TAG_RESOURCE_ID));
			newResourcePlan.setData(TAG_DATEUNIT_EFFORTS, safeGetTag(resourcePlanToClone, TAG_DATEUNIT_EFFORTS));
			
			return newResourcePlanRef;
		}
		
		private ORefList cloneProgressReports(RawObject taskToClone) throws Exception
		{
			ORefList progressReportRefs = new ORefList();

			if (taskToClone.hasValue(TAG_PROGRESS_REPORT_REFS))
			{
				ORefList progressReportRefList = new ORefList(taskToClone.getData(TAG_PROGRESS_REPORT_REFS));
				for (ORef progressReportRef : progressReportRefList)
				{
					RawObject progressReportToClone = getRawProject().findObject(progressReportRef);

					ORef newProgressReportRef = cloneProgressReport(progressReportToClone);
					progressReportRefs.add(newProgressReportRef);
				}
			}

			return progressReportRefs;
		}

		private ORef cloneProgressReport(RawObject progressReportToClone) throws Exception
		{
			ORef newProgressReportRef = getRawProject().createObject(ObjectType.PROGRESS_REPORT);
			RawObject newProgressReport = getRawProject().findObject(newProgressReportRef);

			newProgressReport.setData(TAG_DETAILS, safeGetTag(progressReportToClone, TAG_DETAILS));
			newProgressReport.setData(TAG_PROGRESS_STATUS, safeGetTag(progressReportToClone, TAG_PROGRESS_STATUS));
			newProgressReport.setData(TAG_PROGRESS_DATE, safeGetTag(progressReportToClone, TAG_PROGRESS_DATE));

			return newProgressReportRef;
		}
		
		private ORefList cloneAssignments(RawObject taskToClone, int proportionShare) throws Exception
		{
			ORefList assignmentRefs = new ORefList();

			if (taskToClone.hasValue(TAG_RESOURCE_ASSIGNMENT_IDS))
			{
				IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), taskToClone.getData(TAG_RESOURCE_ASSIGNMENT_IDS));
				for (int i = 0; i < assignmentIdList.size(); i++)
				{
					BaseId assignmentId = assignmentIdList.get(i);
					ORef assignmentRef = new ORef(ObjectType.RESOURCE_ASSIGNMENT, assignmentId);
					RawObject assignmentToClone = getRawProject().findObject(assignmentRef);

					ORef newAssignmentRef = cloneAssignment(assignmentToClone, proportionShare);
					assignmentRefs.add(newAssignmentRef);
				}
			}

			return assignmentRefs;
		}

		private ORef cloneAssignment(RawObject assignmentToClone, int proportionShare) throws Exception
		{
			ORef newAssignmentRef = getRawProject().createObject(ObjectType.RESOURCE_ASSIGNMENT);
			RawObject newAssignment = getRawProject().findObject(newAssignmentRef);

			newAssignment.setData(TAG_RESOURCE_ID, safeGetTag(assignmentToClone, TAG_RESOURCE_ID));

			newAssignment.setData(TAG_DATEUNIT_EFFORTS, safeGetTag(assignmentToClone, TAG_DATEUNIT_EFFORTS));
			proportionDateUnitEfforts(newAssignment, proportionShare);

			newAssignment.setData(TAG_ACCOUNTING_CODE_ID, safeGetTag(assignmentToClone, TAG_ACCOUNTING_CODE_ID));
			newAssignment.setData(TAG_FUNDING_SOURCE_ID, safeGetTag(assignmentToClone, TAG_FUNDING_SOURCE_ID));
			newAssignment.setData(TAG_CATEGORY_ONE_REF, safeGetTag(assignmentToClone, TAG_CATEGORY_ONE_REF));
			newAssignment.setData(TAG_CATEGORY_TWO_REF, safeGetTag(assignmentToClone, TAG_CATEGORY_TWO_REF));
			newAssignment.setData(TAG_TAXONOMY_CLASSIFICATION_CONTAINER, safeGetTag(assignmentToClone, TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
			newAssignment.setData(TAG_ACCOUNTING_CLASSIFICATION_CONTAINER, safeGetTag(assignmentToClone, TAG_ACCOUNTING_CLASSIFICATION_CONTAINER));

			return newAssignmentRef;
		}

		private ORefList proportionAssignments(RawObject task, int proportionShare) throws Exception
		{
			ORefList assignmentRefs = new ORefList();

			if (task.hasValue(TAG_RESOURCE_ASSIGNMENT_IDS))
			{
				IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), task.getData(TAG_RESOURCE_ASSIGNMENT_IDS));
				for (int i = 0; i < assignmentIdList.size(); i++)
				{
					BaseId assignmentId = assignmentIdList.get(i);
					ORef assignmentRef = new ORef(ObjectType.RESOURCE_ASSIGNMENT, assignmentId);
					RawObject assignment = getRawProject().findObject(assignmentRef);

					proportionDateUnitEfforts(assignment, proportionShare);
				}
			}

			return assignmentRefs;
		}

		private ORefList cloneExpenses(RawObject taskToClone, int proportionShare) throws Exception
		{
			ORefList expenseRefs = new ORefList();

			if (taskToClone.hasValue(TAG_EXPENSE_ASSIGNMENT_REFS))
			{
				ORefList expenseRefList = new ORefList(taskToClone.getData(TAG_EXPENSE_ASSIGNMENT_REFS));
				for (ORef expenseRef : expenseRefList)
				{
					RawObject expenseToClone = getRawProject().findObject(expenseRef);

					ORef newExpenseRef = cloneExpense(expenseToClone, proportionShare);
					expenseRefs.add(newExpenseRef);
				}
			}

			return expenseRefs;
		}

		private ORef cloneExpense(RawObject expenseToClone, int proportionShare) throws Exception
		{
			ORef newExpenseRef = getRawProject().createObject(ObjectType.EXPENSE_ASSIGNMENT);
			RawObject newExpense = getRawProject().findObject(newExpenseRef);

			newExpense.setData(TAG_DATEUNIT_EFFORTS, safeGetTag(expenseToClone, TAG_DATEUNIT_EFFORTS));
			proportionDateUnitEfforts(newExpense, proportionShare);

			newExpense.setData(TAG_CATEGORY_ONE_REF, safeGetTag(expenseToClone, TAG_CATEGORY_ONE_REF));
			newExpense.setData(TAG_CATEGORY_TWO_REF, safeGetTag(expenseToClone, TAG_CATEGORY_TWO_REF));
			newExpense.setData(TAG_ACCOUNTING_CODE_REF, safeGetTag(expenseToClone, TAG_ACCOUNTING_CODE_REF));
			newExpense.setData(TAG_FUNDING_SOURCE_REF, safeGetTag(expenseToClone, TAG_FUNDING_SOURCE_REF));
			newExpense.setData(TAG_TAXONOMY_CLASSIFICATION_CONTAINER, safeGetTag(expenseToClone, TAG_TAXONOMY_CLASSIFICATION_CONTAINER));
			newExpense.setData(TAG_ACCOUNTING_CLASSIFICATION_CONTAINER, safeGetTag(expenseToClone, TAG_ACCOUNTING_CLASSIFICATION_CONTAINER));

			return newExpenseRef;
		}

		private ORefList proportionExpenses(RawObject task, int proportionShare) throws Exception
		{
			ORefList expenseRefs = new ORefList();

			if (task.hasValue(TAG_EXPENSE_ASSIGNMENT_REFS))
			{
				ORefList expenseRefList = new ORefList(task.getData(TAG_EXPENSE_ASSIGNMENT_REFS));
				for (ORef expenseRef : expenseRefList)
				{
					RawObject expense = getRawProject().findObject(expenseRef);
					proportionDateUnitEfforts(expense, proportionShare);
				}
			}

			return expenseRefs;
		}

		private void proportionDateUnitEfforts(RawObject rawObject, int proportionShare) throws Exception
		{
			String dateUnitEffortAsString =  safeGetTag(rawObject, TAG_DATEUNIT_EFFORTS);
			if (dateUnitEffortAsString != null && dateUnitEffortAsString.length() > 0)
			{
				DateUnitEffortList dateUnitEffortList = new DateUnitEffortList(dateUnitEffortAsString);
				for (int i = 0; i < dateUnitEffortList.size(); i++)
				{
					DateUnitEffort dateUnitEffort = dateUnitEffortList.getDateUnitEffort(i);
					double numberOfUnits = dateUnitEffort.getQuantity();
					dateUnitEffort.setUnitQuantity(numberOfUnits / proportionShare);
				}
				rawObject.setData(TAG_DATEUNIT_EFFORTS, dateUnitEffortList.toJson().toString());
			}
		}

		private String safeGetTag(RawObject rawObject, String tag)
		{
			if (rawObject.hasValue(tag))
				return rawObject.getData(tag);

			return "";
		}

		private HashMap<BaseId, IdList> getOrCreateSubTaskIdToStrategyIdMap() throws ParseException
		{
			if (taskIdToStrategyIdListMap == null)
				taskIdToStrategyIdListMap = createTaskIdToStrategyIdListMap();

			return taskIdToStrategyIdListMap;
		}

		private HashMap<BaseId, IdList> createTaskIdToStrategyIdListMap() throws ParseException
		{
			HashMap<BaseId, IdList> taskIdMap = new HashMap<BaseId, IdList>();

			ORefList strategyRefs = getRawProject().getAllRefsForType(ObjectType.STRATEGY);
			for (ORef strategyRef : strategyRefs)
			{
				RawObject rawStrategy = getRawProject().findObject(strategyRef);
				if (rawStrategy != null && rawStrategy.containsKey(TAG_ACTIVITY_IDS))
				{
					BaseId strategyId = strategyRef.getObjectId();
					IdList taskIdList = new IdList(TaskSchema.getObjectType(), rawStrategy.get(TAG_ACTIVITY_IDS));
					for (int i = 0; i < taskIdList.size(); ++i)
					{
						BaseId taskId = taskIdList.get(i);
						if (taskIdMap.containsKey(taskId))
						{
							IdList currentStrategyIdList = taskIdMap.get(taskId);
							currentStrategyIdList.add(strategyId);
							taskIdMap.put(taskId, currentStrategyIdList);
						}
						else
						{
							taskIdMap.put(taskId, new IdList(ObjectType.STRATEGY, new BaseId[]{strategyId}));
						}
					}
				}
			}

			return taskIdMap;
		}

		private int type;
		private HashMap<BaseId, IdList> taskIdToStrategyIdListMap;
	}

	public static final String TAG_ACTIVITY_IDS = "ActivityIds";

	// task fields
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	public static final String TAG_SUBTASK_IDS = "SubtaskIds";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_IS_MONITORING_ACTIVITY = "IsMonitoringActivity";
	public static final String TAG_RELEVANT_INDICATOR_SET = "RelevantIndicatorSet";
	public static final String TAG_RESOURCE_PLAN_IDS = "PlanIds";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public static final String TAG_PLANNED_LEADER_RESOURCE = "PlannedLeaderResource";
	public static final String TAG_ASSIGNED_LEADER_RESOURCE = "AssignedLeaderResource";
	public static final String TAG_TAXONOMY_CLASSIFICATION_CONTAINER = "TaxonomyClassificationContainer";

	public static final String TAG_COMMENTS = "Comments";
	public static final String TAG_TEXT = "Text";
	public static final String TAG_SHORT_LABEL = "ShortLabel";
	public static final String TAG_INDICATOR_IDS = "IndicatorIds";
	public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";

	// resource assignment specific fields
	public static final String TAG_RESOURCE_ID = "ResourceId";
	public static final String TAG_DATEUNIT_EFFORTS = "Details";
	public static final String TAG_ACCOUNTING_CODE_ID = "AccountingCode";
	public static final String TAG_FUNDING_SOURCE_ID = "FundingSource";
	public static final String TAG_CATEGORY_ONE_REF = "CategoryOneRef";
	public static final String TAG_CATEGORY_TWO_REF = "CategoryTwoRef";
	public static final String TAG_ACCOUNTING_CLASSIFICATION_CONTAINER = "AccountingClassificationContainer";

	// expense assignment specific fields
	public static final String TAG_ACCOUNTING_CODE_REF = "AccountingCodeRef";
	public static final String TAG_FUNDING_SOURCE_REF = "FundingSourceRef";

	// progress report specific fields
	public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
	public static final String TAG_PROGRESS_DATE = "ProgressDate";

	public static final int VERSION_FROM = 29;
	public static final int VERSION_TO = 30;
}