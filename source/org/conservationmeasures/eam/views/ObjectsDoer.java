/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.ProjectResource;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

abstract public class ObjectsDoer extends ViewDoer
{
	public void setPicker(ObjectPicker pickerToUse)
	{
		picker = pickerToUse;
	}
	
	public EAMObject[] getObjects()
	{
		if(picker == null)
			return new EAMObject[0];
		return picker.getSelectedObjects();
	}
	
	public BaseId[] getSelectedIds()
	{
		EAMObject[] selectedObjects = getObjects();
		if (selectedObjects == null)
		{
			BaseId[] emptyIds  = {BaseId.INVALID};
			return emptyIds;
		}
		return getObjectIds(selectedObjects);
	}
	
	private BaseId[] getObjectIds(EAMObject[] selectedObjects)
	{
		BaseId[] objectIds = new BaseId[selectedObjects.length];
		for (int i = 0; i < selectedObjects.length; i++)
			objectIds[i] = selectedObjects[i].getId();
		
		return objectIds;
	}

	public BaseId getSelectedId()
	{
		ProjectResource selectedResource = (ProjectResource)getObjects()[0];
		if(selectedResource == null)
			return BaseId.INVALID;
		return selectedResource.getId();
	}
	
	public void clearSelection()
	{
		picker.clearSelection();
	}

	private ObjectPicker picker;
}
