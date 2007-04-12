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
		super();
		teamModel = new TeamModel(project);
		rowCount = teamModel.getRowCount();
	}
	
	public Object getFieldValue(JRField field)
	{
		return getData(field.getName());
	}

	public String getData(String name)
	{
		if (name.equals(ProjectResource.TAG_NAME))
			return (String)teamModel.getValueAt(rowCount, 0);
		return "";
	}
	
	TeamModel teamModel;
} 
