/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import javax.swing.ListSelectionModel;

import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;

public class ObjectPoolTable extends ObjectTable
{
	public ObjectPoolTable(MainWindow mainWindowToUse, ObjectPoolTableModel modelToUse, String uniqueTableIdentifier)
	{
		super(mainWindowToUse, modelToUse, uniqueTableIdentifier);
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resizeTable(4);
	}
	
	public ObjectPoolTable(MainWindow mainWindowToUse, ObjectPoolTableModel modelToUse, int sortColumn, String uniqueTableIdentifier)
	{
		this(mainWindowToUse, modelToUse, uniqueTableIdentifier);
		sort(sortColumn);
	}
	
	public ObjectPoolTableModel getObjectPoolTableModel()
	{
		return (ObjectPoolTableModel)getModel();
	}
	
	public void updateTableAfterObjectCreated(ORef newObjectRef)
	{
		super.updateTableAfterObjectCreated(newObjectRef);
		getObjectPoolTableModel().rowsWereAddedOrRemoved();
	}
	
	public void updateTableAfterObjectDeleted(ORef deletedObjectRef)
	{
		super.updateTableAfterObjectDeleted(deletedObjectRef);
		getObjectPoolTableModel().rowsWereAddedOrRemoved();
	}
}
