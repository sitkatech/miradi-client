package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Strategy;
import org.conservationmeasures.eam.project.Project;

public class StrategiesDataSource extends CommonDataSource
{
	public StrategiesDataSource(Project project)
	{
		super(project);
		ORefList list = project.getPool(Strategy.getObjectType()).getORefList();
		setObjectList(list);
	}
} 
