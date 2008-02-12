/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.RoundRectangleRenderer;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class ProjectScopeIcon extends AbstractShapeIcon
{
	FactorRenderer getRenderer()
	{
		return new RoundRectangleRenderer();
	}
	
	Color getIconColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_SCOPE);
	}
}
