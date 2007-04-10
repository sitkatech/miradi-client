/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.project.Project;

public class SummaryTeamDataSource implements JRDataSource
{
	public SummaryTeamDataSource(Project projectToUse)
	{
		project = projectToUse;
		iterator = new SummaryTeamData(project);
	}
	
	public Object getFieldValue(JRField field) throws JRException
	{
		System.out.println("HERE SummaryTeamDataSource:" + field.getName() + " value =:" +((SummaryTeamData)data).getData(field.getName()));
		return ((SummaryTeamData)data).getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		if (iterator.hasNext()) 
		{
			data = iterator.next();
			return true;
		}

		return false;
	}

	SummaryTeamData iterator;
	Project project;
	Object data;
} 
