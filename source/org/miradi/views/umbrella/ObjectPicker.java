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

import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;

public interface ObjectPicker extends ListSelectionListener
{
	public ORefList[] getSelectedHierarchies();
	
	//NOTE: No longer use the below methods they are deprecated.  Use getSelectedHierarchies instead
	public ORefList getSelectionHierarchy();
	public BaseObject[] getSelectedObjects();
	//TODO: to be extracted to its own interface (TreeObjectPicker) later
	public TreeTableNode[] getSelectedTreeNodes();

	public void clearSelection();
	public void ensureObjectVisible(ORef ref);
	
	public void addSelectionChangeListener(ListSelectionListener listener);
	public void removeSelectionChangeListener(ListSelectionListener listener);
	
	public void expandAll() throws Exception;
	public void collapseAll() throws Exception;
	
	public boolean isActive();
	public void becomeActive();
	public void becomeInactive();
}
