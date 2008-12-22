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

import java.util.Collections;
import java.util.Vector;

import javax.swing.JComponent;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.utils.GenericDefaultTableModel;
import org.miradi.utils.IgnoreCaseStringComparator;
import org.miradi.utils.SimpleTableWithInheritedFunctionality;

public class ObjectReadonlyObjectListField extends ObjectDataInputField
{
	public ObjectReadonlyObjectListField(MainWindow mainWindowToUse, int objectTypeToUse, BaseId idToUse, String tagToUse)
	{
		super(mainWindowToUse.getProject(), objectTypeToUse, idToUse, tagToUse);
		model = new GenericDefaultTableModel();
		model.setColumnCount(1);
		table = new SimpleTableWithInheritedFunctionality(mainWindowToUse, model);
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
			
			Vector names = new Vector();
			for (int i = 0; i < orefList.size(); ++i)
			{
				ORef ref = orefList.get(i);
				//TODO these invalid refs (orphaned DF)should get auto-repaired during project open at some point
				if (ref.isInvalid())
					continue;
				
				BaseObject object = project.findObject(ref); 
				if(object == null)
				{
					EAM.logError("Ignored a missing object while in ObjectReadonlyObjectListField.setText(). Ref = " + ref);
				}
				else
				{
					names.add(object.getFullName());
				}
			}
			Collections.sort(names, new IgnoreCaseStringComparator());
			
			model.setRowCount(names.size());
			for(int row = 0; row < names.size(); ++row)
				model.setValueAt(names.get(row), row, 0);
			
			table.resizeTable();
			int ARBITRARY_REASONABLE_WIDTH = 300;
			table.setColumnWidth(0, ARBITRARY_REASONABLE_WIDTH);
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
	
	private GenericDefaultTableModel model;
	private SimpleTableWithInheritedFunctionality table;
}
