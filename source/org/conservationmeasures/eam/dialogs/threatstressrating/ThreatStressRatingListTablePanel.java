/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.dialogs.base.ObjectTablePanel;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingListTablePanel extends ObjectTablePanel
{
	public ThreatStressRatingListTablePanel(Project projectToUse, ThreatStressRatingListTableModel stressRatingListModel)
	{
		super(projectToUse, ObjectType.THREAT_STRESS_RATING, new ThreatStressRatingListTable(stressRatingListModel));
	}
}
