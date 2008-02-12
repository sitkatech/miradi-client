/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.reports;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Strategy;
import org.miradi.project.Project;

public class StrategiesDataSource extends CommonDataSource
{
	public StrategiesDataSource(Project project)
	{
		super(project);
		ORefList list = project.getPool(Strategy.getObjectType()).getORefList();
		setObjectList(list);
	}
} 
