/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingListTablePanel extends ObjectListTablePanel
{
	public ThreatStressRatingListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, ObjectType.THREAT_STRESS_RATING, 
				new ThreatStressRatingListTableModel(projectToUse, nodeRef), 
				actions, buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
	};

}
