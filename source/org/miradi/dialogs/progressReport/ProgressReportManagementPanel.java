/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.progressReport;

import javax.swing.Icon;

import org.miradi.actions.Actions;
import org.miradi.dialogs.base.ObjectListManagementPanel;
import org.miradi.icons.ProgressReportIcon;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.SplitterPositionSaverAndGetter;

public class ProgressReportManagementPanel extends ObjectListManagementPanel
{
	public ProgressReportManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, String annotationTag, Actions actions, Class[] editButtonClasses) throws Exception
	{
		super(splitPositionSaverToUse, new ProgressReportListTablePanel(projectToUse, actions, nodeRef, annotationTag, editButtonClasses), new ProgressReportPropertiesPanel(projectToUse));
	}

	public String getPanelDescription()
	{
		return PANEL_DESCRIPTION;
	}
	
	public Icon getIcon()
	{
		return new ProgressReportIcon();
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Progress Reports"); 
}
