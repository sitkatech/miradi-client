/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.planning.propertiesPanel;

import org.miradi.dialogs.base.DisposablePanelWithDescription;
import org.miradi.main.AppPreferences;

public class BlankPropertiesPanel extends DisposablePanelWithDescription
{
	public BlankPropertiesPanel()
	{
		setBackground(AppPreferences.getDataPanelBackgroundColor());
	}

	public String getPanelDescription()
	{
		return "";
	}
}
