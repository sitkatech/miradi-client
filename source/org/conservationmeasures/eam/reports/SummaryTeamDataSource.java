/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.summary.TeamModel;

public class SummaryTeamDataSource implements JRDataSource
{
	public SummaryTeamDataSource(Project project)
	{
		teamModel = new TeamModel(project);
		count = teamModel.getRowCount();
	}
	
	public Object getFieldValue(JRField field) throws JRException
	{
		return getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		return (--count>=0);
	}
	
	public String getData(String name)
	{
		if (name.equals(ProjectResource.TAG_NAME))
			return (String)teamModel.getValueAt(count, 0);
		return "";
	}
	
	TeamModel teamModel;
	int count;
} 
