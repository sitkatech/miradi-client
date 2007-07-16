/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.jump.ActionJumpMonitoringWizardDefineIndicatorsStep;
import org.conservationmeasures.eam.icons.IndicatorIcon;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class IndicatorListManagementPanel extends ObjectListManagementPanel
{
	public IndicatorListManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef ref, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, new IndicatorListTablePanel(projectToUse, actions, ref),
				new IndicatorPropertiesPanel(projectToUse, actions));
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
		return new IndicatorIcon();
	}
	
	public Class getJumpActionClass()
	{
		return ActionJumpMonitoringWizardDefineIndicatorsStep.class;
	}
	
	private static String PANEL_DESCRIPTION = EAM.text("Tab|Indicators"); 
}

