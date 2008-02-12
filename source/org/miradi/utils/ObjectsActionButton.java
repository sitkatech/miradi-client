/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import org.miradi.actions.ObjectsAction;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.views.umbrella.ObjectPicker;

public class ObjectsActionButton extends PanelButton 
{
	public ObjectsActionButton(ObjectsAction action, ObjectPicker picker)
	{
		super(action);
		action.setPicker(picker);
	}
}
