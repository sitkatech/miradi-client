/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

abstract public class DiagramPageList extends JList
{
	public DiagramPageList(Project project, int objectType)
	{
		super();
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		setBorder(BorderFactory.createEtchedBorder());
		fillList(project, objectType);
	}
	
	private void fillList(Project project, int objectType)
	{
		EAMObjectPool pool = project.getPool(objectType);
		ORefList refList = pool.getORefList();
		BaseObject[] diagramObjects = project.getObjectManager().findObjects(refList);
		setListData(diagramObjects);
	}
}