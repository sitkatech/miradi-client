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
		CodeList desiredSequenceCodes = calculateDesiredSequenceCodesForRestoring();
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

	private CodeList calculateDesiredSequenceCodesForRestoring() throws Exception
	{
		final CodeList calculateArrangedColumnCodesToRestore = calculateArrangedColumnCodesToRestore(getSavedColumnCodes(), getDisplayColumnCodes());
		return calculateArrangedColumnCodesToRestore;
	}
	
	protected void moveColumn(int tableColumn, int destination)
	{
		table.getColumnModel().moveColumn(tableColumn, destination);
	}

	protected int getTableColumnCount()
	{
		return table.getColumnCount();
	}
	
	public static CodeList calculateArrangedColumnCodesToRestore(CodeList savedColumnCodes, CodeList displayColumnCodes)
	{
		if (savedColumnCodes == null || hasColumnsThatWereNotExpected(savedColumnCodes, displayColumnCodes))
			savedColumnCodes = displayColumnCodes;

		CodeList storedColumnTags = new CodeList(savedColumnCodes);
		storedColumnTags.retainAll(displayColumnCodes);
		
		return calculateUniqueCodes(storedColumnTags, displayColumnCodes);
	}
	
	private static CodeList calculateUniqueCodes(CodeList savedColumnCodes, CodeList displayColumnCodes)
	{
		CodeList displayColumnCodesClone = new CodeList(displayColumnCodes);
		displayColumnCodesClone.subtract(savedColumnCodes);
		
		CodeList arrangedColumnCodes = new CodeList();
		arrangedColumnCodes.addAll(savedColumnCodes);
		arrangedColumnCodes.addAll(displayColumnCodesClone);
		
		return arrangedColumnCodes.withoutDuplicates();
	}
	
	private static boolean hasColumnsThatWereNotExpected(CodeList savedColumnCodes, CodeList displayColumnCodes)
	{
		CodeList inFirstNotSecond = new CodeList(savedColumnCodes);
		inFirstNotSecond.subtract(displayColumnCodes);
		if(inFirstNotSecond.hasData())
			return true;

		return false;
	}

	protected CodeList getSavedColumnCodes()
	{
		return getSavedColumnCodes(getProject(), uniqueTableIdentifier);
	}

	public static CodeList getSavedColumnCodes(Project project, String uniqueTableIdentifierToUse)
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

	protected CodeList getDisplayColumnCodes()
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
		final String displayColumnCodes = getDisplayColumnCodes().toString();
		CommandSetObjectData setColumnSequence = new CommandSetObjectData(tableSettings.getRef(), TableSettings.TAG_COLUMN_SEQUENCE_CODES, displayColumnCodes);
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
