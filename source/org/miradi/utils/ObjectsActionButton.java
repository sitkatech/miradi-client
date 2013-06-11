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
package org.miradi.utils;

import java.util.HashSet;

import org.miradi.actions.ObjectsAction;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.views.umbrella.ObjectPicker;

public class ObjectsActionButton extends PanelButton 
{
	public ObjectsActionButton(ObjectsAction action, ObjectPicker picker)
	{
		super(action);
		pickers = new HashSet<ObjectPicker>();
		setPicker(picker);
	}
	
	public void setPicker(ObjectPicker pickerToUse)
	{
		if(pickerToUse == null)
			return;
		
		getObjectsAction().addPicker(pickerToUse);
		pickers.add(pickerToUse);
	}

	public void dispose()
	{
		for(ObjectPicker picker : pickers)
		{
			getObjectsAction().removePicker(picker);
		}
	}
	
	private ObjectsAction getObjectsAction()
	{
		return ((ObjectsAction)getAction());
	}
	
	private HashSet<ObjectPicker> pickers;
}
