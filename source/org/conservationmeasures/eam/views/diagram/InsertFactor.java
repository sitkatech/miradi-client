/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.diagram.nodes.DiagramNode;
import org.conservationmeasures.eam.main.EAM;

public class InsertFactor extends InsertNode
{
	public int getTypeToInsert()
	{
		return DiagramNode.TYPE_FACTOR;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Factor");
	}

}
