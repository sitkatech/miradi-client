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
			String thisTag = getColumnGroupCode(tableColumn);	
			if (!thisTag.equals(desiredSequenceCode))
				continue;
			
			if(tableColumn != destination)
				moveColumn(tableColumn, destination + foundCount);
				
			++foundCount;
		}
		
		return foundCount;
	}

	private CodeList calculateDesiredSequenceCodes() throws Exception
	{
		CodeList currentColumnTagSequences = getCurrentSequence();		
		CodeList storedColumnSequenceCodes = getDesiredColumnSequenceCodes();
		if (storedColumnSequenceCodes == null)
			storedColumnSequenceCodes = getCurrentSequence();
		
		return calculateArrangedColumnCodes(storedColumnSequenceCodes, currentColumnTagSequences);
	}

	protected void moveColumn(int tableColumn, int destination)
	{
		table.getColumnModel().moveColumn(tableColumn, destination);
	}

	protected int getTableColumnCount()
	{
		return table.getColumnCount();
	}

	public static CodeList calculateArrangedColumnCodes(CodeList desiredColumnCodes, CodeList currentColumnTagSequences)
	{
		CodeList storedColumnTags = new CodeList(desiredColumnCodes);
		storedColumnTags.retainAll(currentColumnTagSequences);
		currentColumnTagSequences.subtract(storedColumnTags);
		
		CodeList arrangedColumnCodes = new CodeList();
		arrangedColumnCodes.addAll(storedColumnTags);
		arrangedColumnCodes.addAll(currentColumnTagSequences);
		
		return arrangedColumnCodes.withoutDuplicates();
	}
	
	protected CodeList getDesiredColumnSequenceCodes()
	{
		return getDesiredColumnCodes(getProject(), uniqueTableIdentifier);
	}

	public static CodeList getDesiredColumnCodes(Project project, String uniqueTableIdentifierToUse)
	{
		TableSettings tableSettings = TableSettings.find(project, uniqueTableIdentifierToUse);
		if (tableSettings == null)
			return new CodeList();
		
		CodeList storedColumnSequences = tableSettings.getColumnSequenceCodes();
		if (storedColumnSequences.size() == 0)
			return null;

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
			currentColumnTagSequences.add(getColumnGroupCode(tableColumn));
		}
		
		return currentColumnTagSequences;
	}
	
	public void saveColumnSequence() throws Exception
	{		
		TableSettings tableSettings = TableSettings.findOrCreate(getProject(), uniqueTableIdentifier);
		CommandSetObjectData setColumnSequence = new CommandSetObjectData(tableSettings.getRef(), TableSettings.TAG_COLUMN_SEQUENCE_CODES, getCurrentSequence().toString());
		getProject().executeCommand(setColumnSequence);
	}

	protected String getColumnGroupCode(int tableColumn)
	{
		return table.getColumnGroupCode(tableColumn);
	}
	
	@Override
	public void mouseReleased(MouseEvent event)
	{
		try
		{
			saveColumnSequence();
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
