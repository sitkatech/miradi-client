package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;

public class ObjectTableWithParent extends ObjectTable
{
	public ObjectTableWithParent(ObjectTableModel modelToUse, ORef parentRefToUse)
	{
		super(modelToUse);
		parentRef = parentRefToUse;
	}

	public ORefList getSelectionHierarchy()
	{
		ORefList selectionHierarchyRefs = getSelectionHierarchy();
		selectionHierarchyRefs.add(parentRef);
		
		return selectionHierarchyRefs;	
	}
	
	private ORef parentRef;
}
