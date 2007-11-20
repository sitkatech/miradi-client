/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.threatstressrating;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;

public class ThreatStressRatingListTableModel extends ObjectListTableModel
{
	public ThreatStressRatingListTableModel(Project projectToUse, ORef nodeRef)
	{
		//FIXME add correct parent list tag
		super(projectToUse, nodeRef, "Replace me", ThreatStressRating.getObjectType(), getColumnTags());
	}

	private static String[] getColumnTags()
	{
		return new String[]{};
	}
}
