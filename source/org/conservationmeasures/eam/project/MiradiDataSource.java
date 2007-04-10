/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.project;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objects.ProjectMetadata;

public class MiradiDataSource implements JRDataSource
{
	public MiradiDataSource(Project projectToUse)
	{
		project = projectToUse;
		iterator = new MiradiProjectData(project);
	}
	
	public JRDataSource getDataSource()
	{
		return new MiradiDataSource(project);
	}
	
	public Object getFieldValue(JRField field) throws JRException
	{
		System.out.println(field.getName() + "==" + ((ProjectMetadata)data).getData(field.getName()));
		return ((ProjectMetadata)data).getData(field.getName());
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

	MiradiProjectData iterator;
	Project project;
	Object data;
} 
