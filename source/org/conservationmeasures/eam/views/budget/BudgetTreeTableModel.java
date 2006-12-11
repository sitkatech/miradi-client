/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.budget;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.Task;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.workplan.WorkPlanTreeTableModel;
import org.conservationmeasures.eam.views.workplan.WorkPlanTreeTableNode;

public class BudgetTreeTableModel extends WorkPlanTreeTableModel
{
	public BudgetTreeTableModel(Project projectToUse)
	{
		super(projectToUse);
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
		WorkPlanTreeTableNode node = (WorkPlanTreeTableNode)rawNode;
		String totalCost = Double.toString(calculateTotalCost(node.getObjectReference()));
		return totalCost;
	}
	
	private double calculateTotalCost(ORef oRef)
	{
		try{
			EAMObject foundObject = project.findObject(ObjectType.TASK, oRef.getObjectId());
			if (foundObject == null)
				return 0.0;

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
