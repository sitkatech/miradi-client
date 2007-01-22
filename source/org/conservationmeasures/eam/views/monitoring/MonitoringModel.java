/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class MonitoringModel extends GenericTreeTableModel
{
	public MonitoringModel(Project projectToUse) throws Exception
	{
		super(new MonitoringRootNode(projectToUse));
		project = projectToUse;
	}

	public String getColumnName(int column)
	{
		return EAM.fieldLabel(ObjectType.INDICATOR, MonitoringIndicatorNode.COLUMN_TAGS[column]);
	}

	public int getColumnCount()
	{
		return MonitoringIndicatorNode.COLUMN_TAGS.length;
	}
	
	Project project;
}
