/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.EAM;

public class InsertGoal extends InsertNode
{
	public int getTypeToInsert()
	{
		return Node.TYPE_GOAL;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Goal");
	}

}
