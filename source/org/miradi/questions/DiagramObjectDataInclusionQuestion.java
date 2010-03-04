/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.icons.ConceptualModelIcon;
import org.miradi.icons.ResultsChainIcon;
import org.miradi.main.EAM;


public class DiagramObjectDataInclusionQuestion extends StaticChoiceQuestion
{
	public DiagramObjectDataInclusionQuestion()
	{
		super(createChoiceItems());
	}
	
	private static ChoiceItem[] createChoiceItems()
	{
		return new ChoiceItem[]{
				new ChoiceItem(INCLUDE_BOTH_DIAGRAM_DATA_CODE, EAM.text("Both")),
				new ChoiceItem(INCLUDE_RESULTS_CHAIN_DATA_CODE, EAM.text("Result Chain Data"), new ConceptualModelIcon()),
				new ChoiceItem(INCLUDE_CONCEPTUAL_MODEL_DATA_CODE, EAM.text("Conceptual Model Data"), new ResultsChainIcon()),
		};
	}
	
	public static boolean isIncludeBoth(String code)
	{
		return code.equals(INCLUDE_BOTH_DIAGRAM_DATA_CODE);
	}
	
	public static boolean isIncludeConceptualModelOnly(String code)
	{
		return code.equals(INCLUDE_CONCEPTUAL_MODEL_DATA_CODE);
	}
	
	public static boolean isIncludeResultsChainOnly(String code)
	{
		return code.equals(INCLUDE_RESULTS_CHAIN_DATA_CODE);
	}
	
	private static final String INCLUDE_BOTH_DIAGRAM_DATA_CODE = "";
	private static final String INCLUDE_RESULTS_CHAIN_DATA_CODE = "IncludeResultsChainData";
	private static final String INCLUDE_CONCEPTUAL_MODEL_DATA_CODE = "IncludeConceptualModelData";
}
