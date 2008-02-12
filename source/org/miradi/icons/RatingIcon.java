/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.RectangleRenderer;
import org.miradi.questions.ChoiceItem;

public class RatingIcon extends AbstractShapeIcon
{
	public RatingIcon(ChoiceItem option)
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
	
	public static RatingIcon createFromChoice(ChoiceItem choice)
	{
		if (choice.getColor() == null)
			return null;
		
		return new RatingIcon(choice);
	}
	
	private Color color;

}
