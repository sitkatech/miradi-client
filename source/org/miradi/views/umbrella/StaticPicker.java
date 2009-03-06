/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.views.umbrella;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ViewData;

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

	public void expandAll(ViewData viewData) throws Exception
	{
	}
	
	public void collapseAll(ViewData viewData) throws Exception
	{	
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void becomeActive()
	{
		isActive = true;
	}

	public void becomeInactive()
	{
		isActive = false;
	}

	public void valueChanged(ListSelectionEvent e)
	{
	}
	
	private ORefList selectedRefs;
	private boolean isActive;
}
