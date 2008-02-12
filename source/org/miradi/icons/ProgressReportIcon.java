/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import org.miradi.diagram.DiagramConstants;
import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.RectangleRenderer;

public class ProgressReportIcon extends AbstractShapeIcon
{
	FactorRenderer getRenderer() 
	{
		return new RectangleRenderer();
	}

	Color getIconColor() 
	{
		return DiagramConstants.COLOR_STRESS;
	}
}
