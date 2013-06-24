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

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.Indicator;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;
import org.miradi.schemas.AccountingCodeSchema;
import org.miradi.schemas.BudgetCategoryOneSchema;
import org.miradi.schemas.BudgetCategoryTwoSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.ExpenseAssignmentSchema;
import org.miradi.schemas.FundingSourceSchema;
import org.miradi.schemas.FutureStatusSchema;
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

public class TreeRebuilderNodeSorter extends NodeSorter
{
	public TreeRebuilderNodeSorter(ORef parentRefToUse)
	{
		super();
		
		parentRef = parentRefToUse;
	}

	@Override
	public boolean shouldSortChildren(ORef childRef)
	{
		if (!Task.is(childRef))
			return true;

		if(Task.is(parentRef) || Strategy.is(parentRef) || Indicator.is(parentRef))
			return false;
		
		return true;
	}
	
	@Override
	protected String[] getNodeSortOrder()
	{
		return new String[] {
			TargetSchema.OBJECT_NAME,
			HumanWelfareTargetSchema.OBJECT_NAME,
			KeyEcologicalAttributeSchema.OBJECT_NAME,
			ResultsChainDiagramSchema.OBJECT_NAME,
			ConceptualModelDiagramSchema.OBJECT_NAME,
			MeasurementSchema.OBJECT_NAME,
			FutureStatusSchema.OBJECT_NAME,
			GoalSchema.OBJECT_NAME,
			SubTargetSchema.OBJECT_NAME,
			Cause.OBJECT_NAME_THREAT,
			Cause.OBJECT_NAME_CONTRIBUTING_FACTOR,
			ThreatReductionResultSchema.OBJECT_NAME,
			IntermediateResultSchema.OBJECT_NAME,
			ObjectiveSchema.OBJECT_NAME,
			StrategySchema.OBJECT_NAME,
			IndicatorSchema.OBJECT_NAME,
			ProjectResourceSchema.OBJECT_NAME,
			AccountingCodeSchema.OBJECT_NAME,
			FundingSourceSchema.OBJECT_NAME,
			BudgetCategoryOneSchema.OBJECT_NAME,
			BudgetCategoryTwoSchema.OBJECT_NAME,
			TaskSchema.ACTIVITY_NAME,
			TaskSchema.METHOD_NAME,
			TaskSchema.OBJECT_NAME,
			ResourceAssignmentSchema.OBJECT_NAME,
			ExpenseAssignmentSchema.OBJECT_NAME,
		};
	}
	
	private ORef parentRef;
}
