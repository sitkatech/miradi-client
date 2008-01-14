/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.progressReport;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListManagementPanel;
import org.conservationmeasures.eam.icons.ProgressReportIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

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
