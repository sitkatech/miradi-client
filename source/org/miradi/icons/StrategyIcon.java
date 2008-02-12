/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import javax.swing.Icon;

import org.miradi.diagram.renderers.FactorRenderer;
import org.miradi.diagram.renderers.IconHexagonRenderer;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class StrategyIcon extends AbstractShapeIcon
{
	FactorRenderer getRenderer()
	{
		return new IconHexagonRenderer(false);
	}
	
	Color getIconColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_STRATEGY);
	}
	
	static public Icon createDisabledIcon()
	{
		return new StrategyIconDisabledIcon();
	}
	
	private static final class StrategyIconDisabledIcon extends StrategyIcon
	{
		Color getIconColor()
		{
			return Color.LIGHT_GRAY;
		}
	}
}