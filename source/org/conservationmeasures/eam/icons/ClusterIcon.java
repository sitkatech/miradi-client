/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.cells.DiagramConstants;
import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;


public class ClusterIcon extends EamIcon
{
	MultilineNodeRenderer getRenderer()
	{
		return new RectangleRenderer();
	}

	Color getIconColor()
	{
		return DiagramConstants.COLOR_FACTOR_CLUSTER;
	}

}
