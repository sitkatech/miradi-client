/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.Dimension;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.icons.KeyEcologicalAttributeIcon;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class TargetViabilityTreeManagementPanel extends ObjectListManagementPanel
{
	public TargetViabilityTreeManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, FactorId nodeId, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, TargetViabililtyTreePanel.createTargetViabilityPanel(EAM.mainWindow, projectToUse, nodeId),
				new TargetViabilityTreePropertiesPanel(projectToUse, actions));
		//FIXME: This is a crude way to avoid having the Target Properties dlg
		// be too tall to fit on a WXGA screen.
		setPreferredSize(new Dimension(900, 150));

	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new KeyEcologicalAttributeIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Viability"); 
}
