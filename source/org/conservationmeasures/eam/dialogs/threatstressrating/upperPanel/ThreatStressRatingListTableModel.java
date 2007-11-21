/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating.upperPanel;

import org.conservationmeasures.eam.dialogs.base.ObjectTableModel;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingListTableModel extends ObjectTableModel
{
	public ThreatStressRatingListTableModel(Project projectToUse)
	{
		super(projectToUse, ThreatStressRating.getObjectType(), getColumnTags());
	}

	public int getRowCount()
	{
		return 0;
	}
	
	private static String[] getColumnTags()
	{
		return new String[]{};
	}

	public ORefList getLatestRefListFromProject()
	{
		return null;
	}
}
