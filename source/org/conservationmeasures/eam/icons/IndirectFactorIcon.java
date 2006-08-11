/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.icons;

import java.awt.Color;

import org.conservationmeasures.eam.diagram.renderers.MultilineNodeRenderer;
import org.conservationmeasures.eam.diagram.renderers.RectangleRenderer;
import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public class IndirectFactorIcon extends EamIcon
{
	MultilineNodeRenderer getRenderer()
	{
		return new RectangleRenderer();
	}
	
	Color getIconColor()
	{
		return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_INDIRECT_FACTOR);
	}
}