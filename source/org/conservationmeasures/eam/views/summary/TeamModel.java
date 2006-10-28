/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.summary;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class TeamModel extends AbstractTableModel
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
		ProjectResource member = getObjectFromRow(row);
		if(member == null)
			return "(Error)";
		
		return member.getData(ProjectResource.TAG_NAME);
	}

	public ProjectResource getObjectFromRow(int row)
	{
		BaseId id = getMetadata().getTeamResourceIdList().get(row);
		ProjectResource member = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, id);
		return member;
	}

	Project project;
}