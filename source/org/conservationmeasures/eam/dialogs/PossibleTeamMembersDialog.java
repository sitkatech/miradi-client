/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;

import org.conservationmeasures.eam.actions.ActionCreateResource;
import org.conservationmeasures.eam.actions.ActionDeleteResource;
import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamAddMember;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.strategicplan.ResourceManagementPanel;
import org.conservationmeasures.eam.views.summary.TeamAddMember;
import org.martus.swing.UiLabel;

public class PossibleTeamMembersDialog extends FloatingPropertiesDialog
{
	public PossibleTeamMembersDialog(MainWindow mainWindowToUse, TeamAddMember addMemberDoer)
	{
		super(mainWindowToUse);
		
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		
		contents.add(new UiLabel(getOverviewText()), BorderLayout.BEFORE_FIRST_LINE);
		
		ResourceManagementPanel resourcePanel = new ResourceManagementPanel(getView(), buttonActionClasses);
		contents.add(resourcePanel, BorderLayout.CENTER);
		addMemberDoer.setPicker(resourcePanel);
		
		pack();
	}

	public EAMObject getObject()
	{
		return null;
	}
	
	String getOverviewText()
	{
		return "<html>" +
				"<p>" +
				"This table lists all the Resources that have been created within this project. " +
				"</p>" +
				"<p>" +
				"You can select existing resources and add them to the team, " +
				"or you can create new resources." +
				"</p>" +
				"</html";
	}
	


	static final Class[] buttonActionClasses = {
		ActionCreateResource.class, 
		ActionModifyResource.class, 
		ActionDeleteResource.class, 
		ActionTeamAddMember.class,
		};
}
