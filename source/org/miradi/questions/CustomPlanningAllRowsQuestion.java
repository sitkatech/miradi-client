/* 
Copyright 2005-2013, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.questions;

import java.util.Vector;

import org.miradi.icons.DirectThreatIcon;
import org.miradi.icons.TaskIcon;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.TaskSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

public class CustomPlanningAllRowsQuestion extends AbstractCustomPlanningRowsQuestion
{
	@Override
	protected boolean shouldIncludeHumanWellbeignTargetRow()
	{
		return true;
	}
	
	@Override
	protected Vector<ChoiceItem> createCauseChoiceItems()
	{
		Vector<ChoiceItem> causeChoiceItem = new Vector<ChoiceItem>();
		causeChoiceItem.add(createChoiceItem(CauseSchema.getObjectType(), CauseSchema.OBJECT_NAME, new DirectThreatIcon()));
		
		return causeChoiceItem;
	}

	@Override
	protected Vector<ChoiceItem> createTaskChoiceItems()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		choiceItems.add(createChoiceItem(TaskSchema.getObjectType(), TaskSchema.OBJECT_NAME, new TaskIcon()));
		
		return choiceItems;
	}
	
	@Override
	public String convertToReadableCode(String code)
	{
		if (code.equals(ConceptualModelDiagramSchema.OBJECT_NAME))
			return Xmpz2XmlConstants.CONCEPTUAL_MODEL;
		
		if (code.equals(ResultsChainDiagramSchema.OBJECT_NAME))
			return Xmpz2XmlConstants.RESULTS_CHAIN;
		
		if (code.equals(TargetSchema.OBJECT_NAME))
			return Xmpz2XmlConstants.BIODIVERSITY_TARGET;
		
		if (code.equals(HumanWelfareTargetSchema.OBJECT_NAME))
			return HumanWelfareTargetSchema.HUMAN_WELLBEING_TARGET;
			
		return super.convertToReadableCode(code);
	}
	
	@Override
	public String convertToInternalCode(String code)
	{
		if (code.equals(Xmpz2XmlConstants.CONCEPTUAL_MODEL))
			return ConceptualModelDiagramSchema.OBJECT_NAME;
		
		if (code.equals(Xmpz2XmlConstants.RESULTS_CHAIN))
			return ResultsChainDiagramSchema.OBJECT_NAME;
		
		if (code.equals(Xmpz2XmlConstants.BIODIVERSITY_TARGET))
			return TargetSchema.OBJECT_NAME;
		
		if (code.equals(HumanWelfareTargetSchema.HUMAN_WELLBEING_TARGET))
			return HumanWelfareTargetSchema.OBJECT_NAME;

		return super.convertToInternalCode(code);
	}

}
