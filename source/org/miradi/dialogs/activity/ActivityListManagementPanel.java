/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.activity;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.actions.jump.ActionJumpEditAllStrategiesStep;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.dialogs.task.TaskPropertiesPanel;
import org.miradi.icons.ActivityIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class ActivityListManagementPanel extends ObjectListManagementPanel
{
	public ActivityListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new ActivityListTablePanel(projectToUse, actions, nodeRef),
				new TaskPropertiesPanel(projectToUse, actions));
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
		return new ActivityIcon();
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpEditAllStrategiesStep.class;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Activities"); 
}
