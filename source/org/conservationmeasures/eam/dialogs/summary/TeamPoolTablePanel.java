/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.summary;

import org.conservationmeasures.eam.actions.ActionTeamCreateMember;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanelWithCreateAndDelete;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;

public class TeamPoolTablePanel extends ObjectTablePanelWithCreateAndDelete
{
	public TeamPoolTablePanel(Project project, Actions actions)
	{
		super(project, ObjectType.PROJECT_RESOURCE, 
				new TeamPoolTable(new TeamPoolTableModel(project)),
				actions, buttons);
	}
	
	static Class[] buttons = new Class[] {
		ActionTeamCreateMember.class,
		ActionTeamRemoveMember.class,
	};
}
