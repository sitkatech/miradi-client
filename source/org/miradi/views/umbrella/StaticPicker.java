/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;

public class StaticPicker implements ObjectPicker
{
	public StaticPicker(ORefList selectedRefsToUse)
	{
		selectedRefs = selectedRefsToUse;
	}
	
	public void addSelectionChangeListener(ListSelectionListener listener)
	{
	}

	public void clearSelection()
	{
	}

	public void ensureObjectVisible(ORef ref)
	{
	}

	public ORefList[] getSelectedHierarchies()
	{
		return new ORefList[] {selectedRefs};
	}

	public BaseObject[] getSelectedObjects()
	{
		return null;
	}

	public TreeTableNode[] getSelectedTreeNodes()
	{
		return null;
	}

	public ORefList getSelectionHierarchy()
	{
		return null;
	}

	public void removeSelectionChangeListener(ListSelectionListener listener)
	{
	}

	public void valueChanged(ListSelectionEvent e)
	{
	}
	
	private ORefList selectedRefs;
}
