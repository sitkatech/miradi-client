/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.ProjectMetadata;
import org.conservationmeasures.eam.project.Project;

public class SummaryGeneralDataSource extends CommonDataSource
{
	public SummaryGeneralDataSource(Project projectToUse)
	{
		super();
		project = projectToUse;
	}
	
	public JRDataSource getSummaryTeamDataSource()
	{
		return new SummaryTeamDataSource(project);
	}
	
	public Object getFieldValue(JRField field)
	{
		String name = field.getName();
		if (name.startsWith(LABEL_PREFIX))
			return EAM.fieldLabel(ProjectMetadata.getObjectType(), name.substring(LABEL_PREFIX.length()));
		return project.getMetadata().getData(name);
	}
	
	Project project;
	private static String LABEL_PREFIX = "Label:";
} 
