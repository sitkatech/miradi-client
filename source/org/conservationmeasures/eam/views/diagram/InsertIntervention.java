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

public class InsertIntervention extends InsertNode
{
	public InsertIntervention(BaseProject project, Point invocationPoint)
	{
		super(project, invocationPoint);
	}

	public int getTypeToInsert()
	{
		return Node.TYPE_INTERVENTION;
	}

	public String getInitialText()
	{
		return EAM.text("Label|New Intervention");
	}

}
