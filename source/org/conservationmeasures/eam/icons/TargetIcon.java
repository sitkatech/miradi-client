/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import javax.swing.Icon;

import org.conservationmeasures.eam.diagram.renderers.EllipseRenderer;
import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public class TargetIcon extends AbstractShapeIcon
{
	FactorRenderer getRenderer()
	{
		return new EllipseRenderer();
	}
	
	Color getIconColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_TARGET);
	}
	
	static public Icon createDisabledIcon()
	{
		return new TargetIconDisabledIcon();
	}
	
	private static final class TargetIconDisabledIcon extends TargetIcon
	{
		Color getIconColor()
		{
			return Color.LIGHT_GRAY;
		}
	}
}