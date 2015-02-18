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

package org.miradi.questions;

import java.util.Vector;

import javax.swing.Icon;

import org.miradi.icons.ConceptualModelIcon;
import org.miradi.icons.FutureStatusIcon;
import org.miradi.icons.GoalIcon;
import org.miradi.icons.HumanWelfareTargetIcon;
import org.miradi.icons.IconManager;
import org.miradi.icons.IntermediateResultIcon;
import org.miradi.icons.MeasurementIcon;
import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.icons.SubTargetIcon;
import org.miradi.icons.TargetIcon;
import org.miradi.icons.ThreatReductionResultIcon;
import org.miradi.main.EAM;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.FutureStatusSchema;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.IndicatorSchema;
import org.miradi.schemas.IntermediateResultSchema;
import org.miradi.schemas.MeasurementSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.SubTargetSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.ThreatReductionResultSchema;

abstract public class AbstractCustomPlanningRowsQuestion extends ProjectBasedDynamicQuestion
{
	@Override
	public ChoiceItem[] getChoices()
	{
		return getRowChoices().toArray(new ChoiceItem[0]);
	}

	protected Vector<ChoiceItem> getRowChoices()
	{	
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();

		choiceItems.add(createChoiceItem(ConceptualModelDiagramSchema.getObjectType(), ConceptualModelDiagramSchema.OBJECT_NAME, new ConceptualModelIcon()));
		choiceItems.add(createChoiceItem(ResultsChainDiagramSchema.getObjectType(), ResultsChainDiagramSchema.OBJECT_NAME, new ResultsChainIcon()));
		choiceItems.add(createChoiceItem(TargetSchema.getObjectType(), TargetSchema.OBJECT_NAME, new TargetIcon()));
		
		if (shouldIncludeHumanWellbeignTargetRow())
			choiceItems.add(createChoiceItem(HumanWelfareTargetSchema.getObjectType(), HumanWelfareTargetSchema.OBJECT_NAME, new HumanWelfareTargetIcon()));
		
		choiceItems.add(createChoiceItem(SubTargetSchema.getObjectType(), SubTargetSchema.OBJECT_NAME, new SubTargetIcon()));
		choiceItems.add(createChoiceItem(GoalSchema.getObjectType(), GoalSchema.OBJECT_NAME, new GoalIcon()));
		choiceItems.add(createChoiceItem(ObjectiveSchema.getObjectType(), ObjectiveSchema.OBJECT_NAME, new ObjectiveIcon()));
		choiceItems.addAll(createCauseChoiceItems());
		choiceItems.add(createChoiceItem(ThreatReductionResultSchema.getObjectType(), ThreatReductionResultSchema.OBJECT_NAME, new ThreatReductionResultIcon()));
		choiceItems.add(createChoiceItem(IntermediateResultSchema.getObjectType(), IntermediateResultSchema.OBJECT_NAME, new IntermediateResultIcon()));
		choiceItems.add(createChoiceItem(StrategySchema.getObjectType(), StrategySchema.OBJECT_NAME, IconManager.getStrategyIcon()));
		choiceItems.add(createChoiceItem(IndicatorSchema.getObjectType(), IndicatorSchema.OBJECT_NAME, IconManager.getIndicatorIcon()));
		choiceItems.addAll(createTaskChoiceItems());
		choiceItems.add(createChoiceItem(MeasurementSchema.getObjectType(), MeasurementSchema.OBJECT_NAME, new MeasurementIcon()));
		choiceItems.add(createChoiceItem(FutureStatusSchema.getObjectType(), FutureStatusSchema.OBJECT_NAME, new FutureStatusIcon()));
		
		return choiceItems;
	}

	protected static ChoiceItem createChoiceItem(int objectType, String objectName, Icon iconToUse)
	{
		return new ChoiceItem(objectName, EAM.fieldLabel(objectType, objectName), iconToUse);
	}
	
	abstract protected boolean shouldIncludeHumanWellbeignTargetRow();
	
	abstract protected Vector<ChoiceItem> createCauseChoiceItems();
	
	abstract protected Vector<ChoiceItem> createTaskChoiceItems();
}
