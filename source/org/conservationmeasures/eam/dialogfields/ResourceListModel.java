/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import javax.swing.table.AbstractTableModel;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.IdList;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;

public class ResourceListModel extends AbstractTableModel
{
	public ResourceListModel(Project projectToUse)
	{
		project = projectToUse;
		idList = new IdList();
	}

	public void setList(IdList idListToUse)
	{
		idList = idListToUse;
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
		return idList.size();
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
		BaseId id = idList.get(row);
		ProjectResource member = (ProjectResource)project.findObject(ObjectType.PROJECT_RESOURCE, id);
		return member;
	}

	Project project;
	IdList idList;
}