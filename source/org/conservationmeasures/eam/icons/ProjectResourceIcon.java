/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;

public class ProjectResourceIcon extends EamIcon
{
	Color getIconColor()
	{
		return LITE_BROWN;
	}

	FactorRenderer getRenderer()
	{
		return new RectangleRenderer();
	}

	final private static Color LITE_BROWN = new Color(193,142,23);
}
