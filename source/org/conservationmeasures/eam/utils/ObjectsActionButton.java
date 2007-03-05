/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiButton;

public class ObjectsActionButton extends UiButton 
{
	public ObjectsActionButton(ObjectsAction action, ObjectPicker picker)
	{
		super(action);
		action.setPicker(picker);
	}
}
