/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

abstract public class ObjectsDoer extends ViewDoer
{
	public void setPicker(ObjectPicker pickerToUse)
	{
		picker = pickerToUse;
	}
	
	public ObjectPicker getPicker()
	{
		return picker;
	}
	
	public TreeTableNode[] getSelectedTreeNodes()
	{
		if(picker == null)
			return new TreeTableNode[0];
		return picker.getSelectedTreeNodes();
	}
	
	public ORefList getSelectionHierarchy()
	{
		if (picker == null)
			return new ORefList();
		
		return picker.getSelectionHierarchy();
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		if (picker == null)
			return new ORefList[0];
		
		return picker.getSelectedHierarchies();
	}
	
	public BaseObject[] getObjects()
	{
		if(picker == null)
			return new BaseObject[0];
		
		return picker.getSelectedObjects();
	}
	
	public BaseObject getSingleSelected(int type)
	{
		if(picker == null)
			return null;
		
		if (picker.getSelectedHierarchies().length == 0)
			return null;
		
		ORefList selectionHierarchy = picker.getSelectedHierarchies()[0];
		ORefList extractedList = selectionHierarchy.filterByType(type);
		if (extractedList.size() == 0)
			return null;
		
		return getProject().findObject(extractedList.get(0));
	}
	
	public BaseId[] getSelectedIds()
	{
		BaseObject[] selectedObjects = getObjects();
		if (selectedObjects == null)
		{
			BaseId[] emptyIds  = {BaseId.INVALID};
			return emptyIds;
		}
		return getObjectIds(selectedObjects);
	}
	
	private BaseId[] getObjectIds(BaseObject[] selectedObjects)
	{
		BaseId[] objectIds = new BaseId[selectedObjects.length];
		for (int i = 0; i < selectedObjects.length; i++)
			objectIds[i] = selectedObjects[i].getId();
		
		return objectIds;
	}

	
	public int getSelectedObjectType() 
	{
		return getObjects()[0].getType();
	}
	
	public BaseId getSelectedId()
	{
		return getSelectedRef().getObjectId();
	}
	
	public ORef getSelectedRef()
	{
		BaseObject selected = getObjects()[0];
		if(selected == null)
			return ORef.INVALID;
		
		return selected.getRef();
	}
	
	public void clearSelection()
	{
		picker.clearSelection();
	}

	private ObjectPicker picker;
}
