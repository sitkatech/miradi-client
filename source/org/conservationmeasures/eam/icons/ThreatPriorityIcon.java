/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.objects.ThreatRatingValueOption;

public class ThreatPriorityIcon extends EamIcon 
{
	public ThreatPriorityIcon(ThreatRatingValueOption option)
	{
		color = option.getColor();
	}
	
	MultilineNodeRenderer getRenderer() 
	{
		return new RectangleRenderer();
	}

	Color getIconColor() 
	{
		return color;
	}
	
	private Color color;
}

