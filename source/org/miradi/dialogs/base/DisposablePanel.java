/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.LayoutManager2;
import java.util.Vector;

import javax.swing.JPanel;

import org.miradi.actions.ObjectsAction;
import org.miradi.utils.ObjectsActionButton;
import org.miradi.views.umbrella.ObjectPicker;

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
	
	public Class getJumpActionClass()
	{
		return null;
	}
	
	Vector objectsActionsToRelease;
}
