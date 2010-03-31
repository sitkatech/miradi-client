/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.main.EAM;

public class TargetGoalNodeTreePositionQuestion extends StaticChoiceQuestion
{
	public TargetGoalNodeTreePositionQuestion()
	{
		super(getChoiceItems());
	}

	private static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem(UNDER_DIAGRAM_OBJECTS_CODE, EAM.text("Targets and Goals should appear under Results Chains")),
				new ChoiceItem(SAME_LEVEL_AS_DIAGRAM_OBJECTS_CODE, EAM.text("Targets and Goals should not appear under Results Chains")),
		};
	}
	
	public static boolean shouldTargetsBeOnDiagramLevel(String code)
	{
		return code.equals(SAME_LEVEL_AS_DIAGRAM_OBJECTS_CODE);
	}
	
	public static final String UNDER_DIAGRAM_OBJECTS_CODE = "";
	public static final String SAME_LEVEL_AS_DIAGRAM_OBJECTS_CODE = "SameLevelAsDiagramObjectsCode";
}
