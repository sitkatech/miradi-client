/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.icons;

import java.awt.Color;

import org.miradi.main.AppPreferences;

public class MethodIcon extends TaskIcon
{
	protected Color getIconColor()
	{
		return AppPreferences.INDICATOR_COLOR;
	}
}
