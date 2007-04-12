/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.TargetPool;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.project.Project;

public class ViabilityTargetsDataSource implements JRDataSource
{
	public ViabilityTargetsDataSource(Project projectToUse)
	{
		project = projectToUse;
		TargetPool pool = project.getTargetPool();
		count = pool.size();
		list = pool.getORefList();
	}
	
	public JRDataSource getKeyEcologicalAttrubutesDataSource()
	{
		return new KeyEcologicalAttrubutesDataSource(currentTarget);
	}
	
	public Object getFieldValue(JRField field) throws JRException
	{
		return getData(field.getName());
	}

	public boolean next() throws JRException 
	{
		if (--count>=0)
		{
			currentTarget = (Target)project.findObject(list.get(count));
			return true;
		}
		return false;
	}
	
	public String getData(String name)
	{
		return currentTarget.getData(name);
	}
	
	int count;
	ORefList list;
	Project project;
	Target currentTarget;
} 
