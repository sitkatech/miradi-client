/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.dialogs.base.ObjectCollectionPanel;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingListTablePanel extends ObjectCollectionPanel
{
	public static ThreatStressRatingListTablePanel createThreatStressRatingListTablePanel(Project projectToUse) throws Exception
	{
		return new ThreatStressRatingListTablePanel(projectToUse, new ThreatStressRatingMultiTablePanel(projectToUse));
	}
	
	private ThreatStressRatingListTablePanel(Project projectToUse, ThreatStressRatingMultiTablePanel multiTablePanel)
	{
		super(projectToUse, multiTablePanel.getObjectPicker());
		
		add(multiTablePanel);
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
	}

	public BaseObject getSelectedObject()
	{
		return null;
	}
}
