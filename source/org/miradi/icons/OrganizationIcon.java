/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.RectangleRenderer;

public class OrganizationIcon extends AbstractShapeIcon
{
	Color getIconColor()
	{
		return Color.gray.brighter();
	}

	FactorRenderer getRenderer()
	{
		return new RectangleRenderer();
	}
	
	public int getIconWidth()
	{
		return 6;
	}
}
