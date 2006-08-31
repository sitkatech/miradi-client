/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.event.EventListenerList;

import org.martus.swing.UiComboBox;

public class UiComboBoxWithSaneActionFiring extends UiComboBox
{
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
