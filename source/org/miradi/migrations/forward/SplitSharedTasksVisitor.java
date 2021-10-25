/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
import org.miradi.migrations.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;

import java.text.ParseException;
import java.util.HashMap;

public class SplitSharedTasksVisitor extends AbstractMigrationORefVisitor
{
	public SplitSharedTasksVisitor(RawProject rawProjectToUse, int typeToVisit, String taskIdsTagToUse)
	{
		rawProject = rawProjectToUse;
		type = typeToVisit;
		taskIdsTag = taskIdsTagToUse;
	}

	public RawProject getRawProject() { return rawProject; }

	public int getTypeToVisit()
	{
		return type;
	}

	public String getTaskIdsTag()
	{
		return taskIdsTag;
	}

	@Override
	public MigrationResult internalVisit(ORef rawObjectRef) throws Exception
	{
		HashMap<BaseId, RawObject> processedTaskMap = new HashMap<BaseId, RawObject>();

		RawObject objectToMigrate = getRawProject().findObject(rawObjectRef);
		if (objectToMigrate != null && objectToMigrate.containsKey(getTaskIdsTag()))
		{
			IdList taskIdList = new IdList(TaskSchema.getObjectType(), objectToMigrate.get(getTaskIdsTag()));
			taskIdList.sort();
			for (int i = 0; i < taskIdList.size(); i++)
			{
				BaseId taskId = taskIdList.get(i);
				if (!processedTaskMap.containsKey(taskId))
				{
					ORef taskRef = new ORef(ObjectType.TASK, taskId);
					RawObject rawTask = visitTask(rawObjectRef, taskRef);
					processedTaskMap.put(taskId, rawTask);
				}
			}
		}

		return MigrationResult.createSuccess();
	}

	private RawObject visitTask(ORef rawObjectRef, ORef taskRef) throws Exception
	{
		HashMap<BaseId, IdList> taskIdMap = getOrCreateSubTaskIdToParentIdMap();

		BaseId taskId = taskRef.getObjectId();
		RawObject rawTask = getRawProject().findObject(taskRef);

		if (taskIdMap.containsKey(taskId))
		{
			IdList parentIdList = taskIdMap.get(taskId);
			parentIdList.sort();

			int parentCount = parentIdList.toIntArray().length;
			if (parentCount > 1)
			{
				// task referred to by > 1 parent - needs to be migrated

				for (int j = 0; j < parentIdList.size(); j++)
				{
					ORef parentRef = new ORef(getTypeToVisit(), parentIdList.get(j));
					RawObject parent = getRawProject().findObject(parentRef);

					if (!parentRef.equals(rawObjectRef))
					{
						// clone original task and all its children
						ORef newTaskRef = cloneTask(rawTask, parentCount);

						// remove original task Id from / add new cloned task Id to task list for parent
						IdList parentTaskIdList = new IdList(TaskSchema.getObjectType(), parent.get(getTaskIdsTag()));
						parentTaskIdList.removeId(taskId);
						parentTaskIdList.add(newTaskRef.getObjectId());
						parent.setData(getTaskIdsTag(), parentTaskIdList.toJson().toString());

						// for all diagrams on which the new parent strategy appears, check to see if the original task is shown
						// if it is, and the diagram doesn't also contain the old parent strategy, then update the diagram factor ref to the new cloned task
						if (parentRef.getObjectType() == ObjectType.STRATEGY)
						{
							ORefList conceptualModelDiagramsThatReferToOldParent = findDiagramsForFactorRef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, rawObjectRef);
							ORefList conceptualModelDiagramsThatReferToNewParent = findDiagramsForFactorRef(ObjectType.CONCEPTUAL_MODEL_DIAGRAM, parentRef);
							conceptualModelDiagramsThatReferToNewParent.removeAll(conceptualModelDiagramsThatReferToOldParent);
							updateDiagramFactorRefsForTask(conceptualModelDiagramsThatReferToNewParent, taskRef, newTaskRef);

							ORefList resultsChainDiagramsThatReferToOldParent = findDiagramsForFactorRef(ObjectType.RESULTS_CHAIN_DIAGRAM, rawObjectRef);
							ORefList resultsChainDiagramsThatReferToNewParent = findDiagramsForFactorRef(ObjectType.RESULTS_CHAIN_DIAGRAM, parentRef);
							resultsChainDiagramsThatReferToNewParent.removeAll(resultsChainDiagramsThatReferToOldParent);
							updateDiagramFactorRefsForTask(resultsChainDiagramsThatReferToNewParent, taskRef, newTaskRef);
						}
					}
				}

				// now we can proportion the original task and its sub-tasks accordingly
				proportionAssignments(rawTask, parentCount);
				proportionExpenses(rawTask, parentCount);
				proportionSubTasks(rawTask, parentCount);
			}
		}

		return rawTask;
	}

	private ORefList findDiagramsForFactorRef(int diagramObjectType, ORef factorRef) throws Exception
	{
		ORefList diagramRefs = new ORefList();

		getRawProject().ensurePoolExists(diagramObjectType);
		ORefList diagramRefsToCheck = getRawProject().getAllRefsForType(diagramObjectType);

		for (ORef diagramRef : diagramRefsToCheck)
		{
			RawObject diagram = getRawProject().findObject(diagramRef);
			String diagramFactorIdsAsString = safeGetTag(diagram, TAG_DIAGRAM_FACTOR_IDS);
			if (!diagramFactorIdsAsString.isEmpty())
			{
				IdList diagramFactorIdList = new IdList(ObjectType.DIAGRAM_FACTOR, diagramFactorIdsAsString);
				for (int i = 0; i < diagramFactorIdList.size(); i++)
				{
					BaseId diagramFactorId = diagramFactorIdList.get(i);
					ORef diagramFactorRef = new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
					RawObject diagramFactor = getRawProject().findObject(diagramFactorRef);

					String wrappedFactorRefAsString = safeGetTag(diagramFactor, TAG_WRAPPED_REF);
					ORef wrappedFactorRef = ORef.createFromString(wrappedFactorRefAsString);
					if (wrappedFactorRef.equals(factorRef) && !diagramRefs.contains(diagramRef))
					{
						diagramRefs.add(diagramRef);
						break;
					}
				}
			}
		}

		return diagramRefs;
	}

	private void updateDiagramFactorRefsForTask(ORefList diagramRefs, ORef oldTaskRef, ORef newTaskRef) throws Exception
	{
		for (ORef diagramRef : diagramRefs)
		{
			RawObject diagram = getRawProject().findObject(diagramRef);
			String diagramFactorIdsAsString = safeGetTag(diagram, TAG_DIAGRAM_FACTOR_IDS);
			if (!diagramFactorIdsAsString.isEmpty())
			{
				IdList diagramFactorIdList = new IdList(ObjectType.DIAGRAM_FACTOR, diagramFactorIdsAsString);
				for (int i = 0; i < diagramFactorIdList.size(); i++)
				{
					BaseId diagramFactorId = diagramFactorIdList.get(i);
					ORef diagramFactorRef = new ORef(ObjectType.DIAGRAM_FACTOR, diagramFactorId);
					RawObject diagramFactor = getRawProject().findObject(diagramFactorRef);

					String wrappedFactorRefAsString = safeGetTag(diagramFactor, TAG_WRAPPED_REF);
					ORef wrappedFactorRef = ORef.createFromString(wrappedFactorRefAsString);
					if (wrappedFactorRef.equals(oldTaskRef))
					{
						diagramFactor.setData(TAG_WRAPPED_REF, newTaskRef.toJson().toString());
					}
				}
			}
		}
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

		ORefList clonedTimeframeRefs = cloneTimeframes(taskToClone);
		if (!clonedTimeframeRefs.isEmpty())
		{
			IdList timeframeIdList = clonedTimeframeRefs.convertToIdList(ObjectType.TIMEFRAME);
			newTask.setData(TAG_TIMEFRAME_IDS, timeframeIdList.toJson().toString());
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

	private ORefList cloneTimeframes(RawObject taskToClone) throws Exception
	{
		ORefList timeframeRefs = new ORefList();

		if (taskToClone.hasValue(TAG_TIMEFRAME_IDS))
		{
			IdList timeframeIdList = new IdList(TimeframeSchema.getObjectType(), taskToClone.getData(TAG_TIMEFRAME_IDS));
			for (int i = 0; i < timeframeIdList.size(); i++)
			{
				BaseId timeframeId = timeframeIdList.get(i);
				ORef timeframeRef = new ORef(ObjectType.TIMEFRAME, timeframeId);
				RawObject timeframeToClone = getRawProject().findObject(timeframeRef);

				ORef newTimeframeRef = cloneTimeframe(timeframeToClone);
				timeframeRefs.add(newTimeframeRef);
			}
		}

		return timeframeRefs;
	}

	private ORef cloneTimeframe(RawObject timeframeToClone) throws Exception
	{
		ORef timeframePlanRef = getRawProject().createObject(ObjectType.TIMEFRAME);
		RawObject newTimeframe = getRawProject().findObject(timeframePlanRef);

		newTimeframe.setData(TAG_DATEUNIT_EFFORTS, safeGetTag(timeframeToClone, TAG_DATEUNIT_EFFORTS));

		return timeframePlanRef;
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

		newExpense.setData(TAG_LABEL, safeGetTag(expenseToClone, TAG_LABEL));
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

	private HashMap<BaseId, IdList> getOrCreateSubTaskIdToParentIdMap() throws ParseException
	{
		if (taskIdToParentIdListMap == null)
			taskIdToParentIdListMap = createTaskIdToParentIdListMap();

		return taskIdToParentIdListMap;
	}

	private HashMap<BaseId, IdList> createTaskIdToParentIdListMap() throws ParseException
	{
		HashMap<BaseId, IdList> taskIdMap = new HashMap<BaseId, IdList>();

		ORefList parentRefs = getRawProject().getAllRefsForType(getTypeToVisit());
		for (ORef parentRef : parentRefs)
		{
			RawObject rawParent = getRawProject().findObject(parentRef);
			if (rawParent != null && rawParent.containsKey(getTaskIdsTag()))
			{
				BaseId parentId = parentRef.getObjectId();
				IdList taskIdList = new IdList(TaskSchema.getObjectType(), rawParent.get(getTaskIdsTag()));
				for (int i = 0; i < taskIdList.size(); ++i)
				{
					BaseId taskId = taskIdList.get(i);
					if (taskIdMap.containsKey(taskId))
					{
						IdList currentParentIdList = taskIdMap.get(taskId);
						currentParentIdList.add(parentId);
						taskIdMap.put(taskId, currentParentIdList);
					}
					else
					{
						taskIdMap.put(taskId, new IdList(getTypeToVisit(), new BaseId[]{parentId}));
					}
				}
			}
		}

		return taskIdMap;
	}

	private RawProject rawProject;
	private int type;
	private String taskIdsTag;
	private HashMap<BaseId, IdList> taskIdToParentIdListMap;

	// task fields
	public static final String TAG_ID = "Id";
	public static final String TAG_LABEL = "Label";
	public static final String TAG_SUBTASK_IDS = "SubtaskIds";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_IS_MONITORING_ACTIVITY = "IsMonitoringActivity";
	public static final String TAG_TIMEFRAME_IDS = "TimeframeIds";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
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

	// diagram / diagram factor fields
	public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
}