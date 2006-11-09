/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;
import org.martus.swing.UiButton;

public class ObjectsActionButton extends UiButton implements ListSelectionListener
{
	public ObjectsActionButton(ObjectsAction action, ObjectPicker picker)
	{
		super(action);
		action.setPicker(picker);
		picker.addListSelectionListener(this);
	}

	public void valueChanged(ListSelectionEvent arg0)
	{
		ObjectsAction action = (ObjectsAction)getAction();
		action.updateEnabledState();
	}
}
