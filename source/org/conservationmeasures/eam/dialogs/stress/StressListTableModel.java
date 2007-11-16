/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.stress;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class StressListTableModel extends ObjectListTableModel
{
	public StressListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Target.TAG_STRESS_REFS, Stress.getObjectType(), new String[]{Stress.TAG_LABEL});
	}
}
