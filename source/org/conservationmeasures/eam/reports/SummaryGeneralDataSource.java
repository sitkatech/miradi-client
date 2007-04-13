/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.project.Project;

public class SummaryGeneralDataSource extends CommonDataSource
{
	public SummaryGeneralDataSource(Project project)
	{
		super(project);
		setSingleObject(project.getMetadata().getRef());
	}
	
	public JRDataSource getSummaryTeamDataSource()
	{
		return new SummaryTeamDataSource(project);
	}
	
	public Object getFieldValue(JRField field)
	{
		return getValue(field, getCurrentObject());
	}
} 
