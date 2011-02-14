/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.icons.ObjectiveIcon;
import org.miradi.icons.StrategyIcon;
import org.miradi.main.EAM;

public class StrategyObjectiveTreeOrderQuestion extends StaticChoiceQuestion
{
	public StrategyObjectiveTreeOrderQuestion()
	{
		super(createStaticChoices());
	}

	private static ChoiceItem[] createStaticChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(OBJECTIVE_CONTAINS_STRATEGY_CODE, EAM.text("Objectives contain Strategies"), new ObjectiveIcon()),
				new ChoiceItem(STRATEGY_CONTAINS_OBJECTIVE_CODE, EAM.text("Strategies contain Objectives"), new StrategyIcon()),
		};
	}
	
	@Override
	protected boolean hasReadableAlternativeDefaultCode()
	{
		return true;
	}
	
	@Override
	protected String getReadableAlternativeDefaultCode()
	{
		return "ObjectiveContainsStrategy";
	}
	
	public static final String OBJECTIVE_CONTAINS_STRATEGY_CODE = "";
	public static final String STRATEGY_CONTAINS_OBJECTIVE_CODE = "StrategyContainsObjective";
}
