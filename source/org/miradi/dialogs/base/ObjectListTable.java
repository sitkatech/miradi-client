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

import java.text.ParseException;

import javax.swing.ListSelectionModel;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.ids.BaseId;
import org.miradi.ids.IdList;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;

public class ObjectListTable extends ObjectTable
{
	public ObjectListTable(MainWindow mainWindowToUse, ObjectTableModel modelToUse)
	{
		super(mainWindowToUse, modelToUse, getUniqueTableIdentifier((ObjectListTableModel) modelToUse));
		
		setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		resizeTable(4);
	}
	
	public ObjectListTable(MainWindow mainWindowToUse, ObjectTableModel modelToUse, int sortColumn)
	{
		this(mainWindowToUse, modelToUse);
		sort(sortColumn);
	}
	
	private static String getUniqueTableIdentifier(ObjectListTableModel objectListTableModel)
	{
		return UNIQUE_IDENTIFIER + "." + objectListTableModel.getContainingObjectType() + "." + objectListTableModel.getRowObjectType();
	}
	
	public ObjectListTableModel getObjectListTableModel()
	{
		return (ObjectListTableModel)getModel();
	}
	
	public ORefList[] getSelectedHierarchies()
	{
		ORefList[] superHierarchies = super.getSelectedHierarchies();
		ORef containingRef = getObjectListTableModel().getContainingRef();
		if (superHierarchies.length == 0)
			return new ORefList[]{new ORefList(containingRef)};

		for (int i = 0; i < superHierarchies.length; ++i)
		{
			superHierarchies[i].add(containingRef);
		}
		
		return superHierarchies;
	}
	
	public void updateTableAfterCommand(CommandSetObjectData cmd)
	{
		super.updateTableAfterCommand(cmd);
		
		String oldData = cmd.getPreviousDataValue();
		String newData = cmd.getDataValue();
		updateTableIfRowWasAddedOrDeleted(cmd.getObjectType(), cmd.getObjectId(), cmd.getFieldTag(), oldData, newData);
	}
	
	void updateTableAfterUndo(CommandSetObjectData cmd)
	{
		super.updateTableAfterUndo(cmd);
		
		String oldData = cmd.getDataValue();
		String newData = cmd.getPreviousDataValue();
		updateTableIfRowWasAddedOrDeleted(cmd.getObjectType(), cmd.getObjectId(), cmd.getFieldTag(), oldData, newData);
	}
	
	void updateTableIfRowWasAddedOrDeleted(int type, BaseId id, String tag, String oldData, String newData)
	{
		if(type != getObjectListTableModel().getContainingObjectType())
			return;
		
		if(!id.equals(getObjectListTableModel().getContainingObjectId()))
			return;
		
		if(!tag.equals(getObjectListTableModel().getFieldTag()))
			return;
		
		int desiredSelectionRow = getSelectedRow();
		try
		{
			IdList oldList = new IdList(getObjectTableModel().getRowObjectType(), oldData);
			IdList newList = new IdList(getObjectTableModel().getRowObjectType(), newData);
			if(newList.size() > oldList.size())
				desiredSelectionRow = newList.size() - 1;
		}
		catch(ParseException nothingWeCanDoAboutIt)
		{
			EAM.logException(nothingWeCanDoAboutIt);
		}

		getObjectListTableModel().rowsWereAddedOrRemoved();
		desiredSelectionRow = Math.min(desiredSelectionRow, getRowCount() - 1);
		if(desiredSelectionRow >= 0)
			setRowSelectionInterval(desiredSelectionRow, desiredSelectionRow);
	}
	
	private static final String UNIQUE_IDENTIFIER = "ObjectListTable";
}