/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class AnnotationSelectionList extends DisposablePanel
{
	public AnnotationSelectionList(Project projectToUse, ObjectTablePanel poolTable)
	{
		project = projectToUse;
		list = poolTable;
		add(list);
	}
	
	public BaseObject getSelectedAnnotaton()
	{
		return list.getSelectedObject();
	}
	
	public void dispose()
	{
		super.dispose();
		list.dispose();
	}
	
	Project project;
	ObjectTablePanel list;
}

