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
		int destination = 0;
		CodeList desiredSequenceCodes = calculateDesiredSequenceCodes();
		for (int codeIndex = 0; codeIndex < desiredSequenceCodes.size(); ++codeIndex)
		{	
			String desiredSequenceCode = desiredSequenceCodes.get(codeIndex);
			destination += findAndMoveColumn(destination, desiredSequenceCode);
		}
	}

	private int findAndMoveColumn(final int destination, String desiredSequenceCode)
	{
		int foundCount = 0;
		for (int tableColumn = 0; tableColumn < getTableColumnCount(); ++tableColumn)
		{
			String thisTag = getColumnSequenceKey(tableColumn);	
			if (thisTag.equals(desiredSequenceCode))
			{
				if(tableColumn != destination)
				{
					moveColumn(tableColumn, destination + foundCount);
				}
				
				++foundCount;
			}
		}
		
		return foundCount;
	}

	private CodeList calculateDesiredSequenceCodes() throws Exception
	{
		CodeList currentColumnTagSequences = getCurrentSequence();		
		CodeList storedColumnSequenceCodes = getStoredColumnSequenceCodes();
		
		return calculateDesiredSequenceCodes(storedColumnSequenceCodes, currentColumnTagSequences);
	}

	protected void moveColumn(int tableColumn, int destination)
	{
		table.getColumnModel().moveColumn(tableColumn, destination);
	}

	protected int getColumnModelColumnCount()
	{
		return table.getColumnModel().getColumnCount();
	}

	protected int getTableColumnCount()
	{
		return table.getColumnCount();
	}

	public CodeList calculateDesiredSequenceCodes(CodeList storedColumnSequenceCodes, CodeList currentColumnTagSequences)
	{
		CodeList storedColumnTagSequences = new CodeList(storedColumnSequenceCodes);
		storedColumnTagSequences.retainAll(currentColumnTagSequences);
		currentColumnTagSequences.subtract(storedColumnTagSequences);
		
		CodeList desiredSequenceList = new CodeList();
		desiredSequenceList.addAll(storedColumnTagSequences);
		desiredSequenceList.addAll(currentColumnTagSequences);
		
		return desiredSequenceList.withoutDuplicates();
	}

	protected CodeList getStoredColumnSequenceCodes() throws Exception
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

	protected CodeList getCurrentSequence()
	{
		CodeList currentColumnTagSequences = new CodeList();
		for (int tableColumn = 0; tableColumn < getTableColumnCount(); ++tableColumn)
		{	
			currentColumnTagSequences.add(getColumnSequenceKey(tableColumn));
		}
		
		return currentColumnTagSequences;
	}
	
	private CodeList getDefaultSequence()
	{
		CodeList defaultColumnTagSequence = new CodeList();
		for (int column = 0; column < getTableColumnCount(); ++column)
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

	protected String getColumnSequenceKey(int tableColumn)
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
