/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.project.Project;

public class DiagramPanel extends ObjectDataInputPanel
{
	public DiagramPanel(Project projectToUse, ORef orefToUse)
	{
		super(projectToUse, orefToUse);
	}

	public String getPanelDescription()
	{
		return EAM.text("Title|Diagram Panel");
	}

}
