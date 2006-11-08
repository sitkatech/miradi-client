/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ActionModifyResource;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;

public class TeamEditorComponent extends JPanel
{
	public TeamEditorComponent(Project projectToUse, Actions actions)
	{
		super(new BorderLayout());
		project = projectToUse;

		teamModel = new TeamModel(project);
		teamTable = new TeamTable(teamModel);
		teamTable.resizeTable(10);
		rebuild();
		
		add(new UiScrollPane(teamTable), BorderLayout.CENTER);
		add(createButtonBar(actions), BorderLayout.AFTER_LINE_ENDS);
	}
	
	public void rebuild()
	{
		teamModel.fireTableDataChanged();
	}
	
	Box createButtonBar(Actions actions)
	{
		Box box = Box.createVerticalBox();
		box.add(new UiButton(actions.get(ActionViewPossibleTeamMembers.class)));
		box.add(new ObjectsActionButton(actions.getObjectsAction(ActionTeamRemoveMember.class), teamTable));
		box.add(new ObjectsActionButton(actions.getObjectsAction(ActionModifyResource.class), teamTable));
		return box;
	}
	
	Project project;
	TeamModel teamModel;
	TeamTable teamTable;
}