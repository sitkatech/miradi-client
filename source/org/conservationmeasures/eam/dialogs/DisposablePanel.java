/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;
import java.util.Vector;

import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.ObjectsAction;
import org.conservationmeasures.eam.utils.ObjectsActionButton;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class DisposablePanel extends JPanel
{
	public DisposablePanel()
	{
		this(new BorderLayout());
	}
	
	public DisposablePanel(LayoutManager2 layoutToUse)
	{
		super(layoutToUse);
		objectsActionsToRelease = new Vector();
	}
	
	public ObjectsActionButton createObjectsActionButton(ObjectsAction action, ObjectPicker picker)
	{
		objectsActionsToRelease.add(action);
		return new ObjectsActionButton(action, picker);
	}
	
	public void dispose()
	{
		for(int i = 0; i < objectsActionsToRelease.size(); ++i)
		{
			ObjectsAction action = (ObjectsAction)objectsActionsToRelease.get(i);
			action.setPicker(null);
		}
	}
	
	Vector objectsActionsToRelease;
}
