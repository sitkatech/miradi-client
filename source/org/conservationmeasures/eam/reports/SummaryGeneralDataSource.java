/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.project.Project;

public class SummaryGeneralDataSource extends SingleRowDataSource
{
	public SummaryGeneralDataSource(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public JRDataSource getSummaryTeamDataSource()
	{
		return new SummaryTeamDataSource(project);
	}
	
	public Object getFieldValue(JRField field)
	{
		return project.getMetadata().getData(field.getName());
	}

	Project project;
} 
