/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import javax.swing.Icon;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectManagementPanel;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ThreatStressRatingManagementPanel extends ObjectManagementPanel
{
	public ThreatStressRatingManagementPanel(Project projectToUse, SplitterPositionSaverAndGetter splitPositionSaverToUse, ORef nodeRef, Actions actions) throws Exception
	{
		super(splitPositionSaverToUse, 
			  new ThreatStressRatingListTablePanel(projectToUse, actions, nodeRef),
			  new ThreatStressRatingPropertiesPanel(projectToUse));
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
		return null;
	}
	
	private static String PANEL_DESCRIPTION = "ThreatStressRating"; 
}
