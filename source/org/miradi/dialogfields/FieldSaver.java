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

package org.miradi.dialogfields;

import org.miradi.utils.TableWithHelperMethods;

public class FieldSaver
{
	private FieldSaver()
	{
	}
	
	public static FieldSaver getSingletonInstance()
	{
		if (instance == null)
			instance = new FieldSaver();
		
		return instance;
	}
	
	public void setSavableField(SavableField savableFieldToUse)
	{
		savableField = savableFieldToUse;
	}
	
	public void setEditingTable(TableWithHelperMethods tableBeingEditedToUse)
	{
		tableBeingEdited = tableBeingEditedToUse;
	}
	
	public static void saveFocusedFieldPendingEdits()
	{
		if(savableField != null)
			savableField.saveIfNeeded();
		
		if (tableBeingEdited != null)
			tableBeingEdited.stopCellEditing();
	}

	private static SavableField savableField;
	private static TableWithHelperMethods tableBeingEdited;
	private static FieldSaver instance;
}
