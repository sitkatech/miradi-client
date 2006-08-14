/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.diagram.renderers;

import java.awt.Color;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;

public class ProjectScopeRenderer extends MultilineCellRenderer
{
	Color getFillColor()
	{
		return EAM.mainWindow.getColorPreference(AppPreferences.TAG_COLOR_SCOPE);
	}

}
