/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;
import org.conservationmeasures.eam.views.treeViews.TaskTreeTableModel;
import org.conservationmeasures.eam.views.workplan.WorkPlanRoot;

public class BudgetTreeTableModel extends TaskTreeTableModel
{
	public BudgetTreeTableModel(Project projectToUse)
	{
		super(new WorkPlanRoot(projectToUse));
		project = projectToUse;
	}
	
	public int getColumnCount()
	{
		return COLUMN_TAGS.length;
	}

	public String getColumnName(int column)
	{
		return COLUMN_TAGS[column];
	}
	
	public Object getValueAt(Object rawNode, int column)
	{
		TreeTableNode node = (TreeTableNode)rawNode;
		String totalCost = Double.toString(calculateTotalCost(node));
		return totalCost;
	}
	
	private double calculateTotalCost(TreeTableNode node)
	{
		try
		{
			ORef oRef = node.getObjectReference();
			int type = node.getObjectReference().getObjectType();
			BudgetTotalsCalculator totalCalculator = new BudgetTotalsCalculator(project);
			
			if (type == ObjectType.INDICATOR)
				return totalCalculator.getTotalIndicatorCost(oRef);
			
			if (type == ObjectType.FACTOR)
				return totalCalculator.getTotalFactorCost(getFactor(oRef));

			if (oRef.getObjectType() == ObjectType.TASK)
				return totalCalculator.getTotalTaskCost((TaskId)oRef.getObjectId());
			
			if (oRef.getObjectType() == ObjectType.FAKE)
				return totalCalculator.getTotalFakeCost(node);
				
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		return  0.0;
	}

	private Factor getFactor(ORef oRef)
	{
		return (Factor)project.findObject(oRef.getObjectType(), oRef.getObjectId());
	}

	private Project project;
	private static final String COLUMN_TAGS[] = {"Items", "Cost", };
}
