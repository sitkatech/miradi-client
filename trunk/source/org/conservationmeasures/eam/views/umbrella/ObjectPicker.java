/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.TreeTableNode;

public interface ObjectPicker
{
	public BaseObject[] getSelectedObjects();
	public void clearSelection();
	public void ensureObjectVisible(ORef ref);
	//TODO: to be extracted to its own interface (TreeObjectPicker) later
	public TreeTableNode[] getSelectedTreeNodes();
	
	public void addSelectionChangeListener(SelectionChangeListener listener);
	public void removeSelectionChangeListener(SelectionChangeListener listener);
	
	public interface SelectionChangeListener
	{
		public void selectionHasChanged();
	}
}
