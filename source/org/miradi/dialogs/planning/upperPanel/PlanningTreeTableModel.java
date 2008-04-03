/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.Desire;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Objective;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.objects.Task;
import org.miradi.objects.ThreatReductionResult;
import org.miradi.project.Project;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.StrategyClassificationQuestion;
import org.miradi.questions.StrategyRatingSummaryQuestion;
import org.miradi.utils.CodeList;
import org.miradi.views.planning.ColumnManager;
import org.miradi.views.planning.RowManager;

public class PlanningTreeTableModel extends GenericTreeTableModel
{	
	public PlanningTreeTableModel(Project projectToUse) throws Exception
	{
		this(projectToUse, getVisibleRowCodes(projectToUse), getVisibleColumnCodes(projectToUse));
	}
	
	public PlanningTreeTableModel(Project projectToUse, CodeList visibleRowCodesToUse, CodeList visibleColumnCodesToUse) throws Exception
	{
		super(new PlanningTreeRootNode(projectToUse, visibleRowCodesToUse));
		
		project = projectToUse;
		updateColumnsToShow(visibleColumnCodesToUse);
	}

	private static CodeList getVisibleColumnCodes(Project projectToUse) throws Exception
	{
		return ColumnManager.getVisibleColumnCodes(projectToUse.getCurrentViewData());
	}
	
	public static CodeList getVisibleRowCodes(Project projectToUse) throws Exception
	{
		return RowManager.getVisibleRowCodes(projectToUse.getCurrentViewData());
	}

	public void updateColumnsToShow() throws Exception
	{
		CodeList visibleColumnCodes = getVisibleColumnCodes(project);
		updateColumnsToShow(visibleColumnCodes);
	}

	private void updateColumnsToShow(CodeList visibleColumnCodes)
	{
		columnsToShow = new CodeList();
		columnsToShow.add(DEFAULT_COLUMN);
		columnsToShow.addAll(visibleColumnCodes);
		
		omitColumnTagsRepresentedByColumnTables();
	}

	private void omitColumnTagsRepresentedByColumnTables()
	{
		if (columnsToShow.contains(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL))
			columnsToShow.removeCode(Task.PSEUDO_TAG_TASK_BUDGET_DETAIL);
		
		if (columnsToShow.contains(Measurement.META_COLUMN_TAG))
			columnsToShow.removeCode(Measurement.META_COLUMN_TAG);
		
		if (columnsToShow.contains(Indicator.META_COLUMN_TAG))
			columnsToShow.removeCode(Indicator.META_COLUMN_TAG);
	}

	public int getColumnCount()
	{
		return getColumnTags().size();
	}
	
	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.FAKE, getColumnTag(column));
	}
	
	public String getColumnTag(int column)
	{
		return getColumnTags().get(column);
	}
	
	public String getColumnTagForNode(int nodeType, int column)
	{
		String columnTag = getColumnTag(column);

		if(ConceptualModelDiagram.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ConceptualModelDiagram.TAG_DETAIL;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
				
		if(ResultsChainDiagram.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ResultsChainDiagram.TAG_DETAIL;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(Target.is(nodeType))
		{
			if(isDetailsColumn(column))
				return Target.TAG_TEXT;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(Goal.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(Cause.is(nodeType))
		{
			if(isDetailsColumn(column))
				return Cause.TAG_TEXT;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(ThreatReductionResult.is(nodeType))
		{
			if(isDetailsColumn(column))
				return ThreatReductionResult.TAG_TEXT;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(Objective.is(nodeType))
		{
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}

		if(Strategy.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Strategy.TAG_TEXT;
			if(columnTag.equals(Indicator.TAG_PRIORITY))
				return Strategy.PSEUDO_TAG_RATING_SUMMARY;
		}
		
		if(Task.is(nodeType))
		{
			if(isDetailsColumn(column))
				return Task.TAG_DETAILS;
		}

		if(Indicator.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Indicator.TAG_DETAIL;
		}
		
		if(Measurement.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Measurement.TAG_DETAIL;
			if (columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return "";
		}
		
		return columnTag;
	}

	private boolean isDetailsColumn(int column)
	{
		return getColumnTag(column).equals(Desire.TAG_FULL_TEXT);
	}
	
	protected void rebuildNode()
	{
		try
		{
			((AbstractPlanningTreeNode) getRootNode()).setVisibleRowCodes(getVisibleRowCodes(project));
			super.rebuildNode();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	//TODO the nodes need to implement the content of this method
	public Object getValueAt(Object rawNode, int col)
	{
		AbstractPlanningTreeNode treeNode = (AbstractPlanningTreeNode) rawNode;
		BaseObject baseObject = treeNode.getObject();
		return getValueAt(baseObject, col);
	}

	public Object getValueAt(BaseObject baseObject, int col)
	{
		try
		{	
			if(baseObject == null)
				return null;

			String columnTag = getColumnTagForNode(baseObject.getType(), col);
			if (! baseObject.doesFieldExist(columnTag))
				return null;

			String rawValue = "";
			if (baseObject.isPseudoField(columnTag))
				rawValue = baseObject.getPseudoData(columnTag);
			else
				rawValue = baseObject.getData(columnTag);
			
			if(columnTag.equals(Indicator.TAG_PRIORITY))
				return new PriorityRatingQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
				return new ProgressReportStatusQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
				return new StrategyRatingSummaryQuestion().findChoiceByCode(rawValue);
			
			if(columnTag.equals(Strategy.TAG_TAXONOMY_CODE))
				return new StrategyClassificationQuestion().findChoiceByCode(rawValue);

			return rawValue;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "[Error]";
		}
	}

	public CodeList getColumnTags()
	{
		return columnsToShow;	
	}
	
	protected Project getProject()
	{
		return project;
	}
	
	private Project project;
	private CodeList columnsToShow;
}
