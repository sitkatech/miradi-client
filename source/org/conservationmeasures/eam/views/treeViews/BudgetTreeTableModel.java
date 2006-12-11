/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.treeViews;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.budget.BudgetTotalsCalculator;
import org.conservationmeasures.eam.views.workplan.WorkPlanRoot;
import org.conservationmeasures.eam.views.TreeTableNode;

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
		String totalCost = Double.toString(calculateTotalCost(node.getObjectReference()));
		return totalCost;
	}
	
	private double calculateTotalCost(ORef oRef)
	{
		if (oRef.getObjectType() != ObjectType.TASK)
			return 0.0;
		
		try
		{
			EAMObject foundObject = project.findObject(oRef.getObjectType(), oRef.getObjectId());
			BudgetTotalsCalculator totalCalculator = new BudgetTotalsCalculator(project);
			return totalCalculator.getTotalCost((Task)foundObject);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	
		return  0.0;
	}

	private Project project;
	private static final String COLUMN_TAGS[] = {"Items", "Cost", };
}
