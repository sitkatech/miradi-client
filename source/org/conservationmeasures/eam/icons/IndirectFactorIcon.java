/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.nodes.DiagramConstants;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;

public class IndirectFactorIcon extends EamIcon
{
	MultilineNodeRenderer getRenderer()
	{
		return new RectangleRenderer();
	}
	
	Color getIconColor()
	{
		return DiagramConstants.COLOR_INDIRECT_FACTOR;
	}
}