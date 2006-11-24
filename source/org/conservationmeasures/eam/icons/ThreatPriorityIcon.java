/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.objects.ValueOption;

public class ThreatPriorityIcon extends EamIcon 
{
	public ThreatPriorityIcon(ValueOption option)
	{
		color = option.getColor();
	}
	
	FactorRenderer getRenderer() 
	{
		return new RectangleRenderer();
	}

	Color getIconColor() 
	{
		return color;
	}
	
	private Color color;
}

