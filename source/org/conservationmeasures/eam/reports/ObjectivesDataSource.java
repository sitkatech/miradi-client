/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.reports;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.project.Project;

public class ObjectivesDataSource extends CommonDataSource
{
	public ObjectivesDataSource(Project project)
	{
		super(project);
		ORefList list = project.getPool(Objective.getObjectType()).getORefList();
		setObjectList(list);
	}
} 
