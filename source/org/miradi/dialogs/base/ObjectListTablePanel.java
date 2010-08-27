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

import org.miradi.main.MainWindow;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class ObjectListTablePanel extends ObjectTablePanel
{
	public ObjectListTablePanel(MainWindow mainWindowToUse, ObjectTableModel model, ObjectPicker parentPickerToUse)
	{
		this(mainWindowToUse, new ObjectListTable(mainWindowToUse, model), parentPickerToUse);
	}
	
	public ObjectListTablePanel(MainWindow mainWindowToUse, ObjectTableModel model, ObjectPicker parentPickerToUse, int sortColumn)
	{
		this(mainWindowToUse, new ObjectListTable(mainWindowToUse, model, sortColumn), parentPickerToUse);
	}
	
	public ObjectListTablePanel(MainWindow mainWindowToUse, ObjectListTable table, ObjectPicker parentPickerToUse)
	{
		super(mainWindowToUse, table);
		
		parentPicker = parentPickerToUse;
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		if (getParentPicker() != null)
			getParentPicker().becomeActive();
	}
	
	@Override
	public void becomeInactive()
	{
		if (getParentPicker() != null)
			getParentPicker().becomeInactive();
		
		super.becomeInactive();
	}
	
	protected ObjectPicker getParentPicker()
	{
		return parentPicker;
	}
	
	private ObjectPicker parentPicker;
}
