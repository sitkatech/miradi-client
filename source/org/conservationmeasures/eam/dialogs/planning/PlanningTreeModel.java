/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning;

import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeRootNode;
import org.conservationmeasures.eam.dialogs.planning.treenodes.PlanningTreeTaskNode;
import org.conservationmeasures.eam.dialogs.treetables.GenericTreeTableModel;
import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.IndicatorStatusRatingQuestion;
import org.conservationmeasures.eam.questions.PriorityRatingQuestion;
import org.conservationmeasures.eam.questions.StrategyRatingSummaryQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.views.planning.ColumnManager;

public class PlanningTreeModel extends GenericTreeTableModel
{	
	public PlanningTreeModel(Project projectToUse) throws Exception
	{
		super(new PlanningTreeRootNode(projectToUse));
		project = projectToUse;
		rebuildCodeList();
	}

	public void rebuildCodeList() throws Exception
	{
		columnsToShow = new CodeList();
		columnsToShow.add(DEFAULT_COLUMN);
		columnsToShow.addAll(ColumnManager.getVisibleColumnCodes(project.getCurrentViewData()));
		
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
	
	public Object getValueAt(Object rawNode, int col)
	{
		TreeTableNode treeNode = (TreeTableNode) rawNode;
		String columnTag = getColumnTag(col);
		BaseObject baseObject = treeNode.getObject();
		if(baseObject == null)
			return null;
		
		if (! baseObject.doesFieldExist(columnTag))
			return null;
		
		if (baseObject.getType() == Task.getObjectType() && columnTag.equals(BaseObject.PSEUDO_TAG_BUDGET_TOTAL))
			return getTaskBudgetTotal((PlanningTreeTaskNode) treeNode);
		
		String rawValue = "";
		if (baseObject.isPseudoField(columnTag))
			rawValue = baseObject.getPseudoData(columnTag);
		else
			rawValue = baseObject.getData(columnTag);
		
		
		if(columnTag.equals(Indicator.TAG_PRIORITY))
			return new PriorityRatingQuestion(columnTag).findChoiceByCode(rawValue);
		if(columnTag.equals(Indicator.TAG_STATUS))
			return new IndicatorStatusRatingQuestion(columnTag).findChoiceByCode(rawValue);
		if(columnTag.equals(Strategy.PSEUDO_TAG_RATING_SUMMARY))
			return new StrategyRatingSummaryQuestion(columnTag).findChoiceByCode(rawValue);
		
		return rawValue;
	}

	private Object getTaskBudgetTotal(PlanningTreeTaskNode taskNode)
	{
		return taskNode.getTask().getBudgetCost();
	}
	
	public CodeList getColumnTags()
	{
		return columnsToShow;	
	}
	
	private Project project;
	private CodeList columnsToShow;
}
