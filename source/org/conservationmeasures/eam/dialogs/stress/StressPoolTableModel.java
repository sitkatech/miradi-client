/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogs.base.ObjectPoolTableModel;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.project.Project;

public class StressPoolTableModel extends ObjectPoolTableModel
{
	public StressPoolTableModel(Project projectToUse)
	{
		super(projectToUse, Stress.getObjectType(), COLUMN_TAGS);
	}
	
	private static final String[] COLUMN_TAGS = new String[] {
		Stress.TAG_LABEL,
	};
}
