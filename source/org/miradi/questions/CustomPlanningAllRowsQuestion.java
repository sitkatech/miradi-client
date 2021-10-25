/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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

import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.HumanWelfareTargetSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

public class CustomPlanningAllRowsQuestion extends AbstractCustomPlanningRowsQuestion
{
	@Override
	protected boolean shouldIncludeHumanWellbeingTargetRow()
	{
		return true;
	}

	@Override
	protected boolean shouldIncludeBiophysicalFactorRow()
	{
		return true;
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
