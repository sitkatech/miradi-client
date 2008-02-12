/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.KeyEcologicalAttributeIcon;
import org.miradi.ids.FactorId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class KeyEcologicalAttributeListManagementPanel extends ObjectListManagementPanel
{
	public KeyEcologicalAttributeListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new KeyEcologicalAttributeListTablePanel(projectToUse, actions, nodeId),
				new KeyEcologicalAttributePropertiesPanel(projectToUse, actions));
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION; 
	}
	
	public Icon getIcon()
	{
		return new KeyEcologicalAttributeIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|KEA"); 
}

