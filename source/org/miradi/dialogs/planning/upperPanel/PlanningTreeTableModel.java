/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.upperPanel;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Desire;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.project.Project;
import org.miradi.questions.PriorityRatingQuestion;
import org.miradi.questions.ProgressReportStatusQuestion;
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
		if(Strategy.is(nodeType))
		{
			if (isDetailsColumn(column))
				return Strategy.TAG_TEXT;
			if(getColumnTag(column).equals(Indicator.TAG_PRIORITY))
				return Strategy.PSEUDO_TAG_RATING_SUMMARY;
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
		}
		
		return getColumnTag(column);
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
	
	//TODO the nodes need to implement the content of thie metod
	public Object getValueAt(Object rawNode, int col)
	{
		try
		{
			AbstractPlanningTreeNode treeNode = (AbstractPlanningTreeNode) rawNode;
			String columnTag = getColumnTagForNode(treeNode.getType(), col);
			BaseObject baseObject = treeNode.getObject();
			if(baseObject == null)
				return null;

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
	
	private Project project;
	private CodeList columnsToShow;
}
