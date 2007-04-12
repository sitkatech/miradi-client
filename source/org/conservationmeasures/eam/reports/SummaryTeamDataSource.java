/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.summary.TeamModel;

public class SummaryTeamDataSource extends CommonDataSource
{
	public SummaryTeamDataSource(Project project)
	{
		super(project);
		teamModel = new TeamModel(project);
		setRowCount(teamModel.getRowCount());
	}
	
	public Object getFieldValue(JRField field)
	{
		if (field.getName().equals(ProjectResource.TAG_NAME))
			return teamModel.getValueAt(getCurrentRow(), 0);
		return "";
	}
	
	TeamModel teamModel;
} 
