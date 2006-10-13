/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import java.awt.BorderLayout;

import javax.swing.Box;
import javax.swing.JPanel;
import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.actions.ActionProperties;
import org.conservationmeasures.eam.actions.ActionTeamRemoveMember;
import org.conservationmeasures.eam.actions.ActionViewPossibleTeamMembers;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.martus.swing.UiButton;
import org.martus.swing.UiScrollPane;
import org.martus.swing.UiTable;

class TeamEditorComponent extends JPanel
{
	public TeamEditorComponent(Project projectToUse, Actions actions)
	{
		super(new BorderLayout());
		project = projectToUse;

		teamModel = new TeamModel(project);
		teamTable = new UiTable(teamModel);
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
		box.add(new UiButton(actions.get(ActionTeamRemoveMember.class)));
		box.add(new UiButton(actions.get(ActionProperties.class)));
		return box;
	}
	
	class TeamModel extends AbstractTableModel
	{
		public TeamModel(Project projectToUse)
		{
			project = projectToUse;
		}
		
		public ProjectMetadata getMetadata()
		{
			return project.getMetadata();
		}

		public int getColumnCount()
		{
			return 1;
		}

		public String getColumnName(int column)
		{
			return EAM.text("Column|Name");
		}

		public int getRowCount()
		{
			return getMetadata().getTeamResourceIdList().size();
		}

		public Object getValueAt(int row, int column)
		{
			BaseId id = getMetadata().getTeamResourceIdList().get(row);
			ProjectResource member = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, id);
			return member.getData(ProjectResource.TAG_NAME);
		}

		Project project;
	}
	
	Project project;
	TeamModel teamModel;
	UiTable teamTable;
}