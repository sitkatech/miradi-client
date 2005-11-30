/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.DiagramIntervention;
import org.conservationmeasures.eam.diagram.renderers.HexagonRenderer;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;

public class InsertInterventionIcon extends EamIcon
{
	MultilineNodeRenderer getRenderer()
	{
		return new HexagonRenderer();
	}
	
	Color getIconColor()
	{
		return DiagramIntervention.COLOR_INTERVENTION;
	}
}