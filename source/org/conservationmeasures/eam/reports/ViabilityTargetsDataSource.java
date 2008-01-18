/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

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
		ORefList list = pool.getORefList();
		setObjectList(list);
	}
	
	public JRDataSource getKeyEcologicalAttrubutesDataSource() throws Exception
	{
		return new KeyEcologicalAttributesDataSource((Target)getCurrentObject());
	}
} 
