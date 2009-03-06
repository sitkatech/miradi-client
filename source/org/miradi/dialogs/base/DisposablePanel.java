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
			action.addPicker(null);
		}
	}
	
	public Class getJumpActionClass()
	{
		return null;
	}
	
	Vector objectsActionsToRelease;
}
