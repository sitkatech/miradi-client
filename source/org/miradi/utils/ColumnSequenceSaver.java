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
package org.miradi.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objects.TableSettings;
import org.miradi.project.Project;

public class ColumnSequenceSaver extends MouseAdapter
{
	public ColumnSequenceSaver(Project projectToUse, TableWithColumnManagement tableToUse, String uniqueTableIdentifierToUse)
	{
		project = projectToUse;
		table = tableToUse;
		uniqueTableIdentifier = uniqueTableIdentifierToUse;
	}
	
	public void restoreColumnSequences() throws Exception
	{
		CodeList currentColumnTagSequences = getCurrentSequence();		
		CodeList storedColumnTagSequences = new CodeList(getStoredColumnSequenceCodes());
		storedColumnTagSequences.retainAll(currentColumnTagSequences);
		currentColumnTagSequences.subtract(storedColumnTagSequences);
		
		CodeList newSequenceList = new CodeList();
		newSequenceList.addAll(storedColumnTagSequences);
		newSequenceList.addAll(currentColumnTagSequences);
		for (int codeIndex = 0; codeIndex < newSequenceList.size(); ++codeIndex)
		{			
			int currentLocation = findCurrentTagLocation(newSequenceList.get(codeIndex));
			if(currentLocation < 0)
				continue;
			int destination = Math.min(codeIndex, table.getColumnModel().getColumnCount() - 1);
			if(currentLocation != destination)
				table.getColumnModel().moveColumn(currentLocation, destination);
		}
	}

	private CodeList getStoredColumnSequenceCodes() throws Exception
	{
		TableSettings tableSettings = TableSettings.find(getProject(), uniqueTableIdentifier);
		if (tableSettings == null)
			return new CodeList();
		
		CodeList storedColumnSequences = tableSettings.getCodeList(TableSettings.TAG_COLUMN_SEQUENCE_CODES);
		if (storedColumnSequences.size() == 0)
			return getDefaultSequence();

		return storedColumnSequences;
	}

	public Project getProject()
	{
		return project;
	}

	private int findCurrentTagLocation(String keyToFind)
	{
		for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{
			String thisTag = getColumnSequenceKey(tableColumn);
			if (thisTag.equals(keyToFind))
			{
				return tableColumn;
			}
		}
		
		return -1;
	}

	private CodeList getCurrentSequence()
	{
		CodeList currentColumnTagSequences = new CodeList();
		for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{	
			currentColumnTagSequences.add(getColumnSequenceKey(tableColumn));
		}
		
		return currentColumnTagSequences;
	}
	
	private CodeList getDefaultSequence()
	{
		CodeList defaultColumnTagSequence = new CodeList();
		for (int column = 0; column < table.getColumnCount(); ++column)
		{
			String columnTag = getColumnSequenceKey(column);
			defaultColumnTagSequence.add(columnTag);
		}
		
		return defaultColumnTagSequence;
	}
	
	private void saveColumnSequences() throws Exception
	{		
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), uniqueTableIdentifier);
		CommandSetObjectData setColumnSequence = new CommandSetObjectData(tableSettings.getRef(), TableSettings.TAG_COLUMN_SEQUENCE_CODES, getCurrentSequence().toString());
		getProject().executeCommand(setColumnSequence);
	}

	private String getColumnSequenceKey(int tableColumn)
	{
		return table.getTableColumnSequenceKey(tableColumn);
	}
	
	public void mouseReleased(MouseEvent event)
	{
		try
		{
			saveColumnSequences();
			table.repaint();
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	private Project project;
	private TableWithColumnManagement table;
	private String uniqueTableIdentifier;
	public static final int DEFAULT_NARROW_COLUMN_WIDTH = 75;
	public static final int DEFAULT_WIDE_COLUMN_WIDTH = 200;
}
