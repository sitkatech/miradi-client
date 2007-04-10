/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import java.util.Iterator;

import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.summary.TeamModel;

public class SummaryTeamData implements Iterator
{
	public SummaryTeamData(Project projectToUse)
	{
		project = projectToUse;
		teamModel = new TeamModel(project);
		count = teamModel.getRowCount();
	}
	
	public boolean hasNext() 
	{
		return (count>0);
	}

	public Object next() 
	{
		--count;
		return this;
	}

	public void remove() 
	{
	}
	
	public String getData(String name)
	{
		if (name.equals(ProjectResource.TAG_NAME))
			return (String)teamModel.getValueAt(count, 0);
		return "";
	}
	
	int count;
	TeamModel teamModel;
	Project project;
}
