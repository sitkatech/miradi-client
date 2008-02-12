/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.reports;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Goal;
import org.miradi.project.Project;

public class GoalsDataSource extends CommonDataSource
{
	public GoalsDataSource(Project project)
	{
		super(project);
		ORefList list = project.getPool(Goal.getObjectType()).getORefList();
		setObjectList(list);
	}
} 
