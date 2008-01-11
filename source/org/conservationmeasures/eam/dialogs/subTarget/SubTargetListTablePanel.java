/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.subTarget;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanel;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.SubTarget;
import org.conservationmeasures.eam.project.Project;

public class SubTargetListTablePanel extends ObjectListTablePanel
{
	public SubTargetListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, SubTarget.getObjectType(), new SubTargetListTableModel(projectToUse, nodeRef), 
				actions, buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
	};
}
