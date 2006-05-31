/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.Component;

import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.objects.IdList;
import org.martus.swing.UiList;

public class MultiSelectDialogField extends DialogField
{
	public MultiSelectDialogField(String tagToUse, String labelToUse, EAMObject[] availableObjects, IdList selectedObjects)
	{
		super(tagToUse, labelToUse);
		listComponent = new UiList(availableObjects);
		listComponent.setVisibleRowCount(5);
		for(int i = 0; i < availableObjects.length; ++i)
		{
			if(selectedObjects.contains(availableObjects[i].getId()))
			{
				listComponent.addSelectionInterval(i, i);
			}
		}
	}
	
	public UiList getListComponent()
	{
		return listComponent;
	}

	public Component getComponent()
	{
		return getListComponent();
	}

	public String getText()
	{
		IdList selectedIds = new IdList();
		Object[] selectedObjects = listComponent.getSelectedValues();
		for(int i = 0; i< selectedObjects.length; ++i)
		{
			EAMObject thisObject = (EAMObject)selectedObjects[i];
			selectedIds.add(thisObject.getId());
		}
		
		return selectedIds.toString();
	}

	UiList listComponent;
}
