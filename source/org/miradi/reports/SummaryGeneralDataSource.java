/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.miradi.project.Project;

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
} 
