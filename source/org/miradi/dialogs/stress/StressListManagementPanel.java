/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.stress;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpTargetStressesStep;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.StressIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class StressListManagementPanel extends ObjectListManagementPanel
{
	public StressListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new StressListTablePanel(projectToUse, actions, nodeRef),
				new StressPropertiesPanel(projectToUse));
	}

	public String getSplitterDescription()
	{
		return getPanelDescription() + SPLITTER_TAG;
	}
	
	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new StressIcon();
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpTargetStressesStep.class;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Stresses"); 
}
