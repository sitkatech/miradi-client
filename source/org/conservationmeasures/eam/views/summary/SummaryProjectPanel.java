/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.summary;

import org.conservationmeasures.eam.dialogs.base.ObjectDataInputPanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class SummaryProjectPanel extends ObjectDataInputPanel
{
	public SummaryProjectPanel(Project projectToUse, ORef refToUse)
	{
		super(projectToUse, refToUse);
	}

	public String getPanelDescription()
	{
		return EAM.text("Project");
	}
}
