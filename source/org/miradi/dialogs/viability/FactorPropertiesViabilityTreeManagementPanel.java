/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.actions.Actions;
import org.miradi.icons.IndicatorIcon;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class FactorPropertiesViabilityTreeManagementPanel extends
		TargetViabilityTreeManagementPanel
{
	public FactorPropertiesViabilityTreeManagementPanel(MainWindow mainWindowToUse, ORef factorRef, Actions actions) throws Exception
	{
		super(mainWindowToUse, factorRef, actions);
		panelDescription = PANEL_DESCRIPTION_INDICATORS;
		icon = new IndicatorIcon();
	}  
	
	public String getSplitterDescription()
	{
		return PANEL_DESCRIPTION_INDICATORS + SPLITTER_TAG;
	}

	private static String PANEL_DESCRIPTION_INDICATORS = EAM.text("Tab|Indicators"); 

}
