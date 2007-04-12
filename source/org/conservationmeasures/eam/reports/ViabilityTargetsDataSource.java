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

public class ViabilityTargetsDataSource extends CommonDataSource
{
	public ViabilityTargetsDataSource(Project projectToUse)
	{
		super(projectToUse);
		project = projectToUse;
		TargetPool pool = project.getTargetPool();
		list = pool.getORefList();
		setRowCount(list.size());
	}
	
	public JRDataSource getKeyEcologicalAttrubutesDataSource()
	{
		return new KeyEcologicalAttrubutesDataSource(currentTarget);
	}
	
	public Object getFieldValue(JRField field)
	{
		return getValue(field, currentTarget);
	}

	public boolean next() throws JRException 
	{
		if (super.next())
		{
			currentTarget = (Target)project.findObject(list.get(getCurrentRow()));
			return true;
		}
		return false;
	}
	
	
	ORefList list;
	Project project;
	Target currentTarget;
} 
