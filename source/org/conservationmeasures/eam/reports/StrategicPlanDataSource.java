package org.conservationmeasures.eam.reports;

import net.sf.jasperreports.engine.JRDataSource;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.project.Project;

public class StrategicPlanDataSource extends CommonDataSource
{
	public StrategicPlanDataSource(Project project)
	{
		super(project);
		ORefList list = project.getPool(Goal.getObjectType()).getORefList();
		setObjectList(list);
	}
	
	public JRDataSource getRelatedObjectDataSource(int relatedObjectTypeToFind)
	{
		return new RelatedObjectDataSource(getCurrentObject(), relatedObjectTypeToFind);
	}
} 