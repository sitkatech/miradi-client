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
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.*;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.TaskSchema;

import java.util.ArrayList;
import java.util.Vector;

public class MigrationTo33 extends AbstractMigration
{
	public MigrationTo33(RawProject rawProjectToUse)
	{
		super(rawProjectToUse);
	}

	@Override
	protected MigrationResult reverseMigrate() throws Exception
	{
		// decision made not to attempt to remove created monitoring activities, etc.
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
			visitor = new MigrateIndicatorAssignmentsVisitor(typeToVisit);
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
		typesToMigrate.add(ObjectType.INDICATOR);

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
		return EAM.text("This migration moves work plan data from Indicators / Methods to Monitoring Activities.");
	}

	private class MigrateIndicatorAssignmentsVisitor extends AbstractMigrationORefVisitor
	{
		public MigrateIndicatorAssignmentsVisitor(int typeToVisit)
		{
			type = typeToVisit;
			resultsChain = null;
			strategy = null;
		}

		public int getTypeToVisit()
		{
			return type;
		}

		@Override
		protected MigrationResult internalVisit(ORef rawObjectRef) throws Exception
		{
			activity = null;

			RawObject rawIndicatorToMigrate = getRawProject().findObject(rawObjectRef);
			if (rawIndicatorToMigrate != null && shouldVisitIndicator(rawIndicatorToMigrate))
			{
				RawObject activity = getOrCreateMonitoringActivity(rawIndicatorToMigrate);
				visitAssignments(rawIndicatorToMigrate, activity);
				visitExpenses(rawIndicatorToMigrate, activity);
				visitMethods(rawIndicatorToMigrate, activity);
			}

			return MigrationResult.createSuccess();
		}

		private boolean shouldVisitIndicator(RawObject rawIndicatorToMigrate) throws Exception
		{
			ArrayList<ORef> indicatorAssignmentRefs = getAssignments(rawIndicatorToMigrate);
			if (indicatorAssignmentRefs.size() > 0)
				return true;

			ArrayList<ORef> indicatorExpenseRefs = getExpenses(rawIndicatorToMigrate);
			if (indicatorExpenseRefs.size() > 0)
				return true;

			ArrayList<RawObject> indicatorMethods = getMethods(rawIndicatorToMigrate);
			if (indicatorMethods.size() > 0)
				return true;

			return false;
		}

		private void visitAssignments(RawObject currentAssignmentOwner, RawObject newAssignmentOwner) throws Exception
		{
			for (ORef assignmentRef : getAssignments(currentAssignmentOwner))
			{
				moveAssignment(currentAssignmentOwner, newAssignmentOwner, assignmentRef);
			}
		}

		private void visitExpenses(RawObject currentExpenseOwner, RawObject newExpenseOwner) throws Exception
		{
			for (ORef expenseRef : getExpenses(currentExpenseOwner))
			{
				moveExpense(currentExpenseOwner, newExpenseOwner, expenseRef);
			}
		}

		private void visitMethods(RawObject rawIndicatorToVisit, RawObject monitoringActivity) throws Exception
		{
			for (RawObject method : getMethods(rawIndicatorToVisit))
			{
				RawObject task = createTask(method, monitoringActivity);
				visitAssignments(method, task);
				visitExpenses(method, task);
			}
		}

		private ArrayList<ORef> getAssignments(RawObject rawObject) throws Exception
		{
			ArrayList<ORef> assignmentList = new ArrayList<ORef>(){};

			if (rawObject.containsKey(TAG_RESOURCE_ASSIGNMENT_IDS))
			{
				IdList assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), rawObject.getData(TAG_RESOURCE_ASSIGNMENT_IDS));
				for (int i = 0; i < assignmentIdList.size(); i++)
				{
					BaseId assignmentId = assignmentIdList.get(i);
					ORef assignmentRef = new ORef(ObjectType.RESOURCE_ASSIGNMENT, assignmentId);
					assignmentList.add(assignmentRef);
				}
			}

			return assignmentList;
		}

		private ArrayList<ORef> getExpenses(RawObject rawObject) throws Exception
		{
			ArrayList<ORef> expenseList = new ArrayList<ORef>(){};

			if (rawObject.containsKey(TAG_EXPENSE_ASSIGNMENT_REFS))
			{
				ORefList expenseRefList = new ORefList(rawObject.getData(TAG_EXPENSE_ASSIGNMENT_REFS));
				for (ORef expenseRef : expenseRefList)
				{
					expenseList.add(expenseRef);
				}
			}

			return expenseList;
		}

		private ArrayList<RawObject> getMethods(RawObject rawObject) throws Exception
		{
			ArrayList<RawObject> methodList = new ArrayList<RawObject>(){};

			if (rawObject.containsKey(TAG_METHOD_IDS))
			{
				IdList methodIdList = new IdList(TaskSchema.getObjectType(), rawObject.get(TAG_METHOD_IDS));
				for (int i = 0; i < methodIdList.size(); i++)
				{
					BaseId methodId = methodIdList.get(i);
					ORef methodRef = new ORef(ObjectType.TASK, methodId);
					RawObject method = getRawProject().findObject(methodRef);
					methodList.add(method);
				}
			}

			return methodList;
		}

		private void moveAssignment(RawObject currentAssignmentOwner, RawObject newAssignmentOwner, ORef assignmentRefToMove) throws Exception
		{
			BaseId assignmentId = assignmentRefToMove.getObjectId();

			IdList currentOwnerAssignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), currentAssignmentOwner.get(TAG_RESOURCE_ASSIGNMENT_IDS));
			currentOwnerAssignmentIdList.removeId(assignmentId);
			currentAssignmentOwner.setData(TAG_RESOURCE_ASSIGNMENT_IDS, currentOwnerAssignmentIdList.toJson().toString());

			IdList assignmentIdList;
			String assignmentIdListAsString = safeGetTag(newAssignmentOwner, TAG_RESOURCE_ASSIGNMENT_IDS);
			if (!assignmentIdListAsString.isEmpty())
			{
				assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), assignmentIdListAsString);
				assignmentIdList.add(assignmentId);
			}
			else
			{
				assignmentIdList = new IdList(ResourceAssignmentSchema.getObjectType(), new BaseId[] {assignmentId});
			}

			newAssignmentOwner.setData(TAG_RESOURCE_ASSIGNMENT_IDS, assignmentIdList.toJson().toString());
		}

		private void moveExpense(RawObject currentExpenseOwner, RawObject newExpenseOwner, ORef expenseRef) throws Exception
		{
			ORefList indicatorExpenseRefList = new ORefList(currentExpenseOwner.getData(TAG_EXPENSE_ASSIGNMENT_REFS));
			indicatorExpenseRefList.remove(expenseRef);
			currentExpenseOwner.setData(TAG_EXPENSE_ASSIGNMENT_REFS, indicatorExpenseRefList.toJson().toString());

			ORefList expenseRefList;
			String expenseRefListAsString = safeGetTag(newExpenseOwner, TAG_EXPENSE_ASSIGNMENT_REFS);
			if (!expenseRefListAsString.isEmpty())
			{
				expenseRefList = new ORefList(expenseRefListAsString);
				expenseRefList.add(expenseRef);
			}
			else
			{
				expenseRefList = new ORefList(expenseRef);
			}

			newExpenseOwner.setData(TAG_EXPENSE_ASSIGNMENT_REFS, expenseRefList.toJson().toString());
		}

		private RawObject getOrCreateResultsChainForMonitoringActivities()
		{
			if (resultsChain == null)
			{
				getRawProject().ensurePoolExists(ObjectType.RESULTS_CHAIN_DIAGRAM);
				ORef newResultsChainDiagramRef = getRawProject().createObject(ObjectType.RESULTS_CHAIN_DIAGRAM);
				resultsChain = getRawProject().findObject(newResultsChainDiagramRef);
				resultsChain.setData(TAG_LABEL, EAM.text("Monitoring Activities"));
			}

			return resultsChain;
		}

		private RawObject getOrCreateStrategyForMonitoringActivities(RawObject indicator) throws Exception
		{
			if (strategy == null)
			{
				getRawProject().ensurePoolExists(ObjectType.STRATEGY);
				ORef newStrategyRef = getRawProject().createObject(ObjectType.STRATEGY);
				strategy = getRawProject().findObject(newStrategyRef);
				strategy.setData(TAG_LABEL, EAM.text("Monitoring Activities"));

				getRawProject().ensurePoolExists(ObjectType.DIAGRAM_FACTOR);
				ORef newDiagramFactorRef = getRawProject().createObject(ObjectType.DIAGRAM_FACTOR);
				RawObject newDiagramFactor = getRawProject().findObject(newDiagramFactorRef);
				newDiagramFactor.setData(TAG_WRAPPED_REF, newStrategyRef.toJson().toString());

				RawObject resultsChain = getOrCreateResultsChainForMonitoringActivities();
				IdList diagramFactorIdList = new IdList(ObjectType.DIAGRAM_FACTOR);
				diagramFactorIdList.add(newDiagramFactorRef.getObjectId());
				resultsChain.setData(TAG_DIAGRAM_FACTOR_IDS, diagramFactorIdList.toJson().toString());

				String relevancySetAsJsonString = safeGetTag(indicator, TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
				RelevancyOverrideSet relevancySet = new RelevancyOverrideSet(relevancySetAsJsonString);
				relevancySet.add(new RelevancyOverride(newStrategyRef, true));
				indicator.setData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancySet.toString());
			}

			return strategy;
		}

		private RawObject getOrCreateMonitoringActivity(RawObject indicator) throws Exception
		{
			if (activity == null)
			{
				ORef newActivityRef = createTask();
				activity = getRawProject().findObject(newActivityRef);
				activity.setData(TAG_ID, safeGetTag(indicator, TAG_ID));
				activity.setData(TAG_SHORT_LABEL, safeGetTag(indicator, TAG_SHORT_LABEL));
				activity.setData(TAG_LABEL, safeGetTag(indicator, TAG_LABEL));
				activity.setData(TAG_DETAILS, safeGetTag(indicator, TAG_DETAIL));
				activity.setData(TAG_IS_MONITORING_ACTIVITY, BooleanData.BOOLEAN_TRUE);
				activity.setData(TAG_COMMENTS, safeGetTag(indicator, TAG_COMMENTS));
				activity.setData(TAG_TEXT, safeGetTag(indicator, TAG_TEXT));

				ORefList clonedProgressReportRefs = cloneProgressReports(indicator);
				if (!clonedProgressReportRefs.isEmpty())
					activity.setData(TAG_PROGRESS_REPORT_REFS, clonedProgressReportRefs.toJson().toString());

				RawObject strategy = getOrCreateStrategyForMonitoringActivities(indicator);
				BaseId activityId = newActivityRef.getObjectId();

				String activityIdListAsString = safeGetTag(strategy, TAG_ACTIVITY_IDS);
				IdList activityIdList;
				if (!activityIdListAsString.isEmpty())
				{
					activityIdList = new IdList(TaskSchema.getObjectType(), activityIdListAsString);
					activityIdList.add(activityId);
				}
				else
				{
					activityIdList = new IdList(TaskSchema.getObjectType(), new BaseId[] {activityId});
				}

				strategy.setData(TAG_ACTIVITY_IDS, activityIdList.toJson().toString());

				String relevancySetAsJsonString = safeGetTag(indicator, TAG_RELEVANT_STRATEGY_ACTIVITY_SET);
				RelevancyOverrideSet relevancySet = new RelevancyOverrideSet(relevancySetAsJsonString);
				relevancySet.add(new RelevancyOverride(newActivityRef, true));
				indicator.setData(TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevancySet.toString());
			}

			return activity;
		}

		private ORefList cloneProgressReports(RawObject indicatorToCloneFrom) throws Exception
		{
			ORefList progressReportRefs = new ORefList();

			if (indicatorToCloneFrom.hasValue(TAG_PROGRESS_REPORT_REFS))
			{
				ORefList progressReportRefList = new ORefList(indicatorToCloneFrom.getData(TAG_PROGRESS_REPORT_REFS));
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

		private RawObject createTask(RawObject method, RawObject monitoringActivity) throws Exception
		{
			ORef newTaskRef = createTask();
			RawObject task = getRawProject().findObject(newTaskRef);

			task.setData(TAG_ID, safeGetTag(method, TAG_ID));
			task.setData(TAG_SHORT_LABEL, safeGetTag(method, TAG_SHORT_LABEL));
			task.setData(TAG_LABEL, safeGetTag(method, TAG_LABEL));
			task.setData(TAG_DETAILS, safeGetTag(method, TAG_DETAILS));
			task.setData(TAG_COMMENTS, safeGetTag(method, TAG_COMMENTS));
			task.setData(TAG_TEXT, safeGetTag(method, TAG_TEXT));

			task.setData(TAG_PROGRESS_REPORT_REFS, safeGetTag(method, TAG_PROGRESS_REPORT_REFS));
			method.setData(TAG_PROGRESS_REPORT_REFS, "");

			BaseId taskId = newTaskRef.getObjectId();

			String taskIdListAsString = safeGetTag(monitoringActivity, TAG_SUBTASK_IDS);
			IdList taskIdList;
			if (!taskIdListAsString.isEmpty())
			{
				taskIdList = new IdList(TaskSchema.getObjectType(), taskIdListAsString);
				taskIdList.add(taskId);
			}
			else
			{
				taskIdList = new IdList(TaskSchema.getObjectType(), new BaseId[] {taskId});
			}

			if (method.containsKey(TAG_SUBTASK_IDS))
			{
				IdList subTaskIdList = new IdList(TaskSchema.getObjectType(), method.getData(TAG_SUBTASK_IDS));
				taskIdList.addAll(subTaskIdList);
				method.setData(TAG_SUBTASK_IDS, "");
			}

			monitoringActivity.setData(TAG_SUBTASK_IDS, taskIdList.toJson().toString());

			return task;
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
		private RawObject resultsChain;
		private RawObject strategy;
		private RawObject activity;
	}

	public static final String TAG_LABEL = "Label";
	public static final String TAG_ACTIVITY_IDS = "ActivityIds";
	public final static String TAG_METHOD_IDS = "TaskIds";
	public static final String TAG_SUBTASK_IDS = "SubtaskIds";
	public static final String TAG_RESOURCE_ASSIGNMENT_IDS = "AssignmentIds";
	public static final String TAG_EXPENSE_ASSIGNMENT_REFS = "ExpenseRefs";
	public final static String TAG_IS_MONITORING_ACTIVITY = "IsMonitoringActivity";
	public static final String TAG_DIAGRAM_FACTOR_IDS = "DiagramFactorIds";
	public static final String TAG_WRAPPED_REF = "WrappedFactorRef";
	public static final String TAG_RELEVANT_STRATEGY_ACTIVITY_SET = "RelevantStrategySet";

	public static final String TAG_ID = "Id";
	public static final String TAG_DETAIL = "Detail";
	public static final String TAG_DETAILS = "Details";
	public static final String TAG_PROGRESS_REPORT_REFS = "ProgressReportRefs";
	public static final String TAG_COMMENTS = "Comments";
	public static final String TAG_TEXT = "Text";
	public static final String TAG_SHORT_LABEL = "ShortLabel";

	public static final String TAG_PROGRESS_STATUS = "ProgressStatus";
	public static final String TAG_PROGRESS_DATE = "ProgressDate";

	public static final int VERSION_FROM = 32;
	public static final int VERSION_TO = 33;
}