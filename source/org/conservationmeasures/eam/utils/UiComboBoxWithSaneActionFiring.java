/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import javax.swing.event.EventListenerList;

import org.conservationmeasures.eam.dialogs.fieldComponents.PanelComboBox;

public class UiComboBoxWithSaneActionFiring extends PanelComboBox
{
	public UiComboBoxWithSaneActionFiring()
	{
		super();
	}
	
	public UiComboBoxWithSaneActionFiring(Object[] choices)
	{
		super(choices);
	}

	public void setSelectedItemWithoutFiring(Object anObject)
	{
		EventListenerList listeners = listenerList;
		try
		{
			listenerList = new EventListenerList();
			setSelectedItem(anObject);
		}
		finally
		{
			listenerList = listeners;
		}
	}
}
