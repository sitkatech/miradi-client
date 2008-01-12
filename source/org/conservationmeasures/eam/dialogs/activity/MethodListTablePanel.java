/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.activity;

import org.conservationmeasures.eam.actions.ActionCreateMethod;
import org.conservationmeasures.eam.actions.ActionDeleteMethod;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectListTablePanelWithParent;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class MethodListTablePanel extends ObjectListTablePanelWithParent
{
	public MethodListTablePanel(Project projectToUse, Actions actions, ORef nodeRef)
	{
		super(projectToUse, ObjectType.TASK, new MethodListTableModel(projectToUse, nodeRef),	actions, buttonActionClasses);
	}
	
	static Class[] buttonActionClasses = new Class[] {
		ActionCreateMethod.class,
		ActionDeleteMethod.class,
// TODO: This won't work until we make the doer work in both Planning and Diagram views
//		ActionShareMethod.class,
	};

}
