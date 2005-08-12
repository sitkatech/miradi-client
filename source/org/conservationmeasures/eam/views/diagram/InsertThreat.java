/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.diagram;

import java.awt.Point;

import org.conservationmeasures.eam.diagram.nodes.Node;
import org.conservationmeasures.eam.main.BaseProject;
import org.conservationmeasures.eam.main.EAM;

public class InsertThreat extends InsertNode
{
	public InsertThreat(BaseProject project, Point invocationPoint)
	{
		super(project, invocationPoint);
	}

	public int getTypeToInsert()
	{
		return Node.TYPE_THREAT;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Threat");
	}

}
