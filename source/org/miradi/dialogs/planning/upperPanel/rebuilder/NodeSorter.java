/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import java.util.Comparator;

import org.miradi.dialogs.planning.treenodes.AbstractPlanningTreeNode;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Measurement;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.KeyEcologicalAttributeSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.schemas.ThreatReductionResultSchema;

public class NodeSorter implements Comparator<AbstractPlanningTreeNode>
{
	public NodeSorter()
	{
	}
	
	public int compare(AbstractPlanningTreeNode nodeA, AbstractPlanningTreeNode nodeB)
	{
		int typeSortLocationA = getTypeSortLocation(nodeA.getType());
		int typeSortLocationB = getTypeSortLocation(nodeB.getType());
		int diff = typeSortLocationA - typeSortLocationB;
		if(diff != 0)
			return diff;

		ORef refA = nodeA.getObjectReference();
		ORef refB = nodeB.getObjectReference();
		if(refA.isValid() && refB.isInvalid())
			return -1;
		
		if(refA.isInvalid() && refB.isValid())
			return 1;

		if (!shouldSortChildren(refA) || !shouldSortChildren(refB))
			return compareTasks(nodeA, nodeB);
		
		return compareNodes(nodeA, nodeB);
	}
	
	public boolean shouldSortChildren(ORef childRef)
	{		
		return true;
	}
	
	private int compareTasks(AbstractPlanningTreeNode nodeA, AbstractPlanningTreeNode nodeB)
	{
		try
		{
			Integer indexOfA = getIndexofChild(nodeA);
			Integer indexOfB = getIndexofChild(nodeB);
			
			return indexOfA.compareTo(indexOfB);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog();
			return 0;
		}
	}
	
	private int getIndexofChild(TreeTableNode childNode) throws Exception
	{
		TreeTableNode parentNode = childNode.getParentNode();
		for (int index = 0; index < parentNode.getChildCount(); ++index)
		{
			if (parentNode.getChild(index).equals(childNode))
				return index;
		}
		
		return -1;
	}



	public int compareNodes(AbstractPlanningTreeNode nodeA, AbstractPlanningTreeNode nodeB)
	{
		String labelA = nodeA.toString();
		String labelB = nodeB.toString();
		int comparisonResult = labelA.compareToIgnoreCase(labelB);
		if (shouldReverseSort(nodeA, nodeB))
			return -comparisonResult;
		
		return comparisonResult;
	}

	public boolean shouldReverseSort(AbstractPlanningTreeNode nodeA, AbstractPlanningTreeNode nodeB)
	{
		if (Measurement.is(nodeA.getType()) && Measurement.is(nodeB.getType()))
			return true;
		
		return false;
	}
			
	private int getTypeSortLocation(int type)
	{
		int[] sortOrder = getNodeSortOrder();
		for(int index = 0; index < sortOrder.length; ++index)
		{
			if(type == sortOrder[index])
			{
				return index;
			}
		}
		
		EAM.logError("NodeSorter unknown type: " + type);
		return sortOrder.length;
	}
	
	protected int[] getNodeSortOrder()
	{
		return new int[] {
			TargetSchema.getObjectType(),
			HumanWelfareTargetSchema.getObjectType(),
			KeyEcologicalAttributeSchema.getObjectType(),
			ResultsChainDiagramSchema.getObjectType(),
			ConceptualModelDiagramSchema.getObjectType(),
			MeasurementSchema.getObjectType(),
			GoalSchema.getObjectType(),
			SubTargetSchema.getObjectType(),
			CauseSchema.getObjectType(),
			ThreatReductionResultSchema.getObjectType(),
			IntermediateResultSchema.getObjectType(),
			ObjectiveSchema.getObjectType(),
			StrategySchema.getObjectType(),
			IndicatorSchema.getObjectType(),
			ProjectResourceSchema.getObjectType(),
			AccountingCodeSchema.getObjectType(),
			FundingSourceSchema.getObjectType(),
			BudgetCategoryOneSchema.getObjectType(),
			BudgetCategoryTwoSchema.getObjectType(),
			TaskSchema.getObjectType(),
			ResourceAssignmentSchema.getObjectType(),
			ExpenseAssignmentSchema.getObjectType(),
		};
	}
}