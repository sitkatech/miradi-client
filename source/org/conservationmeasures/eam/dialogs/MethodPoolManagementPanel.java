/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.icons.MethodIcon;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class MethodPoolManagementPanel extends ObjectPoolManagementPanel
{
	public MethodPoolManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse) throws Exception
	{
		super(splitPositionSaverToUse, new MethodPoolTablePanel(projectToUse),
				new TaskPropertiesPanel(projectToUse, BaseId.INVALID));
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new MethodIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Methods");
}