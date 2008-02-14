/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.actions.Actions;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class ViabilityViewTreeManagementPanel extends
		TargetViabilityTreeManagementPanel
{
	public ViabilityViewTreeManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, Actions actions) throws Exception
	{
		super(projectToUse, splitPositionSaverToUse, actions);
		panelDescription = PANEL_DESCRIPTION_VIABILITY;
		icon = new KeyEcologicalAttributeIcon();

	}
	
	public String getSplitterDescription()
	{
		return PANEL_DESCRIPTION_VIABILITY + SPLITTER_TAG;
	}

	private static String PANEL_DESCRIPTION_VIABILITY = EAM.text("Tab|Viability"); 

}
