/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.util.Collections;
import java.util.Vector;

import javax.swing.JComponent;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.GenericDefaultTableModel;
import org.conservationmeasures.eam.utils.IgnoreCaseStringComparator;
import org.conservationmeasures.eam.utils.SimpleTableWithInheritedFunctionality;

public class ObjectReadonlyObjectList extends ObjectDataInputField
{
	public ObjectReadonlyObjectList(Project projectToUse, int objectTypeToUse, BaseId idToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, idToUse, tagToUse);
		model = new GenericDefaultTableModel();
		model.setColumnCount(1);
		table = new SimpleTableWithInheritedFunctionality(model);
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
				names.add(object.toString());
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
