/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Container;

import org.conservationmeasures.eam.actions.ActionTeamAddMember;
import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.strategicplan.ResourceManagementPanel;
import org.martus.swing.UiButton;
import org.martus.swing.UiLabel;

public class PossibleTeamMembersDialog extends FloatingPropertiesDialog
{
	public PossibleTeamMembersDialog(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		
		ResourceManagementPanel resourcePanel = new ResourceManagementPanel(getView());
		
		ObjectsAction addMemberAction = getMainWindow().getActions().getObjectsAction(ActionTeamAddMember.class);
		UiButton[] extraButtons = {new ObjectsActionButton(addMemberAction, resourcePanel), };
		resourcePanel.addButtons(extraButtons);
		
		
		Container contents = getContentPane();
		contents.setLayout(new BorderLayout());
		contents.add(new UiLabel(getOverviewText()), BorderLayout.BEFORE_FIRST_LINE);
		contents.add(resourcePanel, BorderLayout.CENTER);
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
	

}
