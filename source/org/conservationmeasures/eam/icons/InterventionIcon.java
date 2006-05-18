/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.DiagramConstants;
import org.conservationmeasures.eam.diagram.renderers.IconHexagonRenderer;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;

public class InterventionIcon extends EamIcon
{
	MultilineNodeRenderer getRenderer()
	{
		return new IconHexagonRenderer(false);
	}
	
	Color getIconColor()
	{
		return DiagramConstants.COLOR_INTERVENTION;
	}
}