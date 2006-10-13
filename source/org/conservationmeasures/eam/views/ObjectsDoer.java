/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.views.strategicplan.ObjectPicker;

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
	
	private ObjectPicker picker;
}
