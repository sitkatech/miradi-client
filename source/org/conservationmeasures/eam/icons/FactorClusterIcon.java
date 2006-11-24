/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.DiagramConstants;
import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;


public class FactorClusterIcon extends EamIcon
{
	FactorRenderer getRenderer()
	{
		return new RectangleRenderer();
	}

	Color getIconColor()
	{
		return DiagramConstants.COLOR_FACTOR_CLUSTER;
	}

}
