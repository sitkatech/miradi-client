/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;

import org.conservationmeasures.eam.main.EAM;

public class ColumnSequenceSaver extends MouseAdapter
{
	public ColumnSequenceSaver(JTable tableToUse, ColumnTagProvider tagProviderToUse, String uniqueTableIdentifierToUse)
	{
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
			table.getColumnModel().moveColumn(currentLocation, codeIndex);
		}
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

	private JTable table;
	private ColumnTagProvider tagProvider;
	private String uniqueTableIdentifier;
	public static final int DEFAULT_NARROW_COLUMN_WIDTH = 75;
	public static final int DEFAULT_WIDE_COLUMN_WIDTH = 200;
}
