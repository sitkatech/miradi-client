/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.ratings.RatingChoice;

public class RatingIcon extends EamIcon
{
	public RatingIcon(RatingChoice option)
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
	
	public static RatingIcon createFromChoice(RatingChoice choice)
	{
		if (choice.getColor() == null)
			return null;
		
		return new RatingIcon(choice);
	}
	
	private Color color;

}
