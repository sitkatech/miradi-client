/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.planning.propertiesPanel;

import org.conservationmeasures.eam.dialogs.base.DisposablePanelWithDescription;
import org.conservationmeasures.eam.main.AppPreferences;

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
