/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.subTarget;

import org.conservationmeasures.eam.dialogs.base.ObjectListTableModel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class SubTargetListTableModel extends ObjectListTableModel
{
	public SubTargetListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Target.TAG_SUB_TARGET_REFS, SubTarget.getObjectType(), COLUMN_TAGS);
	}

	public static final String[] COLUMN_TAGS = new String[] {
		SubTarget.TAG_LABEL, 
	};
}
