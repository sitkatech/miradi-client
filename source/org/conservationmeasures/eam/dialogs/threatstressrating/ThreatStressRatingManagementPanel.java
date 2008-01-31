/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import javax.swing.Icon;

import org.conservationmeasures.eam.dialogs.base.ObjectManagementPanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.properties.ThreatStressRatingPropertiesPanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel.ThreatStressRatingListTablePanel;
import org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel.ThreatStressRatingMultiTablePanel;
import org.conservationmeasures.eam.icons.StressIcon;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.SplitterPositionSaverAndGetter;

public class ThreatStressRatingManagementPanel extends ObjectManagementPanel
{
	public static ThreatStressRatingManagementPanel create(MainWindow mainWindowToUse) throws Exception
	{
		Project project = mainWindowToUse.getProject();
		ThreatStressRatingMultiTablePanel multiTablePanel = new ThreatStressRatingMultiTablePanel(project);
		ThreatStressRatingPropertiesPanel propertiesPanel = new ThreatStressRatingPropertiesPanel(mainWindowToUse, multiTablePanel);
		
		ThreatStressRatingListTablePanel tablePanel =  ThreatStressRatingListTablePanel.createThreatStressRatingListTablePanel(
				project, multiTablePanel, propertiesPanel);
		
		return new ThreatStressRatingManagementPanel(mainWindowToUse, tablePanel, propertiesPanel);
	}

	public ThreatStressRatingManagementPanel(SplitterPositionSaverAndGetter splitPositionSaverToUse, ThreatStressRatingListTablePanel listTablePanel, ThreatStressRatingPropertiesPanel propertiesPanel) throws Exception
	{
		super(splitPositionSaverToUse,  listTablePanel, propertiesPanel);
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
		//FIXME add new icon
		return new StressIcon();
	}
	
	public Class getJumpActionClass()
	{
		return null;
	}
	
	private static String PANEL_DESCRIPTION = "ThreatStressRating"; 
}
