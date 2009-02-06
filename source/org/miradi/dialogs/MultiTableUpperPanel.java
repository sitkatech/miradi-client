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
package org.miradi.dialogs;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.ObjectCollectionPanel;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;
import org.miradi.views.umbrella.ObjectPicker;

public class MultiTableUpperPanel extends ObjectCollectionPanel implements ListSelectionListener
{
	public MultiTableUpperPanel(MainWindow mainWindowToUse, ObjectPicker pickerToUse)
	{
		super(mainWindowToUse, pickerToUse);
		pickerToUse.addSelectionChangeListener(this);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
	}

	public BaseObject getSelectedObject()
	{
		return null;
	}

	public void valueChanged(ListSelectionEvent event)
	{
	}
}
