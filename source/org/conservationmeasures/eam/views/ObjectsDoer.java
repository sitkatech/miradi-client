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
		EAMObject[] selectetObjects = getObjects();
		if (selectetObjects == null)
		{
			BaseId[] emptyIds  = {BaseId.INVALID};
			return emptyIds;
		}
		return getObjectIds(selectetObjects);
	}
	
	private BaseId[] getObjectIds(EAMObject[] selectetObjects)
	{
		BaseId[] objectIds = new BaseId[selectetObjects.length];
		for (int i = 0; i < selectetObjects.length; i++)
			objectIds[i] = selectetObjects[i].getId();
		
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
