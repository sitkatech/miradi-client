/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.monitoring;

import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.GenericTreeTableModel;
import org.conservationmeasures.eam.views.TreeTableWithStateSaving;

public class MonitoringTreeTable extends TreeTableWithStateSaving
{
	public MonitoringTreeTable(Project projectToUse, GenericTreeTableModel monitoringModelToUse)
	{
		super(projectToUse, monitoringModelToUse);
	}
}