/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import javax.swing.Icon;

import org.conservationmeasures.eam.diagram.renderers.FactorRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public class ThreatReductionResultIcon extends AbstractShapeIcon
{
	FactorRenderer getRenderer()
	{
		return new RectangleRenderer();
	}
	
	Color getIconColor()
	{
		return EAM.getMainWindow().getColorPreference(AppPreferences.TAG_COLOR_THREAT_REDUCTION_RESULT); 
	}
	
	static public Icon createDisabledIcon()
	{
		return new ThreatReductionResultIconDisabledIcon();
	}
	
	private static final class ThreatReductionResultIconDisabledIcon extends TargetIcon
	{
		Color getIconColor()
		{
			return Color.LIGHT_GRAY;
		}
	}

}
