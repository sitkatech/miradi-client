/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import javax.swing.Icon;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.diagram.renderers.IconHexagonRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

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