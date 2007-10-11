package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ORefList;

public class ObjectTableWithParent extends ObjectTable
{
	public ObjectTableWithParent(ObjectListTableModel modelToUse)
	{
		super(modelToUse);
	}

	public ORefList getSelectionHierarchy()
	{
		ORefList selectionHierarchyRefs = super.getSelectionHierarchy();
		ObjectListTableModel listModel = (ObjectListTableModel) getObjectTableModel();
		selectionHierarchyRefs.add(listModel.getContainingRef());
		
		return selectionHierarchyRefs;	
	}
}
