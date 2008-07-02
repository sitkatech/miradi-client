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
package org.miradi.dialogfields;

import javax.swing.JComponent;

import org.miradi.dialogs.base.ObjectListTable;
import org.miradi.dialogs.base.ObjectListTableModel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;

public class ObjectReadonlyObjectListTableField extends ObjectDataInputField
{
	public ObjectReadonlyObjectListTableField(Project projectToUse, ORef refToUse, String listFieldTag, int listedType, String[] columnTags)
	{
		super(projectToUse, refToUse.getObjectType(), refToUse.getObjectId(), listFieldTag);
		
		model = new ObjectListTableModel(projectToUse, refToUse, listFieldTag, listedType, columnTags);
	    table = new ObjectListTable(model);
	    
		setDefaultFieldBorder();
		table.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		table.setBackground(EAM.READONLY_BACKGROUND_COLOR);
	}

	public String getText()
	{
		return null;
	}

	public void setText(String newValue)
	{
		try
		{
			ORefList orefList = new ORefList(newValue);
			model.setRowObjectRefs(orefList);
			model.fireTableStructureChanged();			
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public boolean allowEdits()
	{
		return false;
	}

	public JComponent getComponent()
	{
		return table;
	}
	
	private ObjectListTableModel model;
	private ObjectListTable table;
}
