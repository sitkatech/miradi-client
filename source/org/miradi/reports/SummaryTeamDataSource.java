/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.reports;

import net.sf.jasperreports.engine.JRField;

import org.miradi.dialogs.summary.TeamPoolTableModel;
import org.miradi.objects.ProjectResource;
import org.miradi.project.Project;

public class SummaryTeamDataSource extends CommonDataSource
{
	public SummaryTeamDataSource(Project project)
	{
		super(project);
		teamModel = new TeamPoolTableModel(project);
		setRowCount(teamModel.getRowCount());
	}
	
	public Object getFieldValue(JRField field)
	{
		if (field.getName().equals(ProjectResource.TAG_GIVEN_NAME))
			return teamModel.getValueAt(getCurrentRow(), 0);
		return "";
	}
	
	private TeamPoolTableModel teamModel;
} 
