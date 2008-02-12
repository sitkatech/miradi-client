/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.slideshow;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.SlideShowIcon;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class SlideListManagementPanel extends ObjectListManagementPanel
{
	public SlideListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new SlideListTablePanel(projectToUse, actions, nodeRef),
				new SlidePropertiesPanel(projectToUse, BaseId.INVALID));
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new SlideShowIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Title|Slide Show"); 
}

