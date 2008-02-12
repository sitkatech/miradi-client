/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.activity;

import org.miradi.dialogs.base.ObjectPoolTablePanel;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.Project;

public class ShareableActivityPoolTablePanel extends ObjectPoolTablePanel
{
	public ShareableActivityPoolTablePanel(Project project, ORef parentRef)
	{
		super(project, ObjectType.TASK, new ShareableActivityPoolTableModel(project, parentRef));
	}
}
