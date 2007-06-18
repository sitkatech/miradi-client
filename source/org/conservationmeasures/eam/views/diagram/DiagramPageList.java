/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import javax.swing.BorderFactory;
import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.project.Project;

abstract public class DiagramPageList extends JList implements ListSelectionListener
{
	public DiagramPageList()
	{
	}
	
	public DiagramPageList(Project project, int objectType)
	{
		super();
		setBorder(BorderFactory.createEtchedBorder());
		fillList(project, objectType);
	}
	
	private void fillList(Project project, int objectType)
	{
		//FIXME nima add real data to list instead of ids
		EAMObjectPool pool = project.getPool(objectType);
		setListData(pool.getIds());
	}

	public void valueChanged(ListSelectionEvent event)
	{
	
	}
}