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

public class PlanningTreeTargetPositionQuestion extends StaticChoiceQuestion
{
	public PlanningTreeTargetPositionQuestion()
	{
		super(getChoiceItems());
	}

	private static ChoiceItem[] getChoiceItems()
	{
		return new ChoiceItem[] {
				new ChoiceItem(TARGET_NODES_CHILD_OF_DIAGRAM_OBJECTS_CODE, EAM.text("Targets within Diagrams")),
				new ChoiceItem(TARGET_NODES_TOP_OF_PLANNING_TREE_CODE, EAM.text("Targets at top level")),
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
		return "TargetNodesChildOfDiagramObjects";
	}
	
	public static boolean shouldPutTargetsAtTopLevelOfTree(String code)
	{
		return code.equals(TARGET_NODES_TOP_OF_PLANNING_TREE_CODE);
	}
	
	private static final String TARGET_NODES_CHILD_OF_DIAGRAM_OBJECTS_CODE = "";
	public static final String TARGET_NODES_TOP_OF_PLANNING_TREE_CODE = "TargetNodesTopOfPlanningTree";
}
