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
package org.miradi.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.miradi.main.EAM;
import org.miradi.project.Project;

public class ColumnSequenceSaver extends MouseAdapter
{
	public ColumnSequenceSaver(Project projectToUse, JTable tableToUse, ColumnTagProvider tagProviderToUse, String uniqueTableIdentifierToUse)
	{
		project = projectToUse;
		table = tableToUse;
		tagProvider = tagProviderToUse;
		uniqueTableIdentifier = uniqueTableIdentifierToUse;
	}
	
	public void restoreColumnSequences() throws Exception
	{
		CodeList currentColumnTagSequences = getCurrentSequence();		
		CodeList storedColumnTagSequences = new CodeList(EAM.getMainWindow().getAppPreferences().getTaggedString(uniqueTableIdentifier));
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

	public Project getProject()
	{
		return project;
	}

	private int findCurrentTagLocation(String tagToFind)
	{
		for (int tableColumn = 0; tableColumn < table.getColumnCount(); ++tableColumn)
		{
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			String thisTag = tagProvider.getColumnTag(modelColumn);
			if (thisTag.equals(tagToFind))
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
			int modelColumn = table.convertColumnIndexToModel(tableColumn);
			currentColumnTagSequences.add(tagProvider.getColumnTag(modelColumn));
		}
		
		return currentColumnTagSequences;
	}
	
	private void saveColumnSequences()
	{		
		String currentColumnSquenceTags = getCurrentSequence().toString();
		EAM.getMainWindow().getAppPreferences().setTaggedString(uniqueTableIdentifier, currentColumnSquenceTags);
	}

	public void mouseReleased(MouseEvent e)
	{
		saveColumnSequences();
		table.repaint();
	}

	private Project project;
	private JTable table;
	private ColumnTagProvider tagProvider;
	private String uniqueTableIdentifier;
	public static final int DEFAULT_NARROW_COLUMN_WIDTH = 75;
	public static final int DEFAULT_WIDE_COLUMN_WIDTH = 200;
}
