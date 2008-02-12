/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.subTarget;

import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.SubTarget;
import org.miradi.objects.Target;
import org.miradi.project.Project;

public class SubTargetListTableModel extends ObjectListTableModel
{
	public SubTargetListTableModel(Project projectToUse, ORef nodeRef)
	{
		super(projectToUse, nodeRef, Target.TAG_SUB_TARGET_REFS, SubTarget.getObjectType(), COLUMN_TAGS);
	}

	public static final String[] COLUMN_TAGS = new String[] {
		SubTarget.TAG_LABEL,
		SubTarget.TAG_SHORT_LABEL,
	};
}
