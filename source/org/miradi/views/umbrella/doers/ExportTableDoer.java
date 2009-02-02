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
package org.miradi.views.umbrella.doers;

import java.io.File;

import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.utils.AbstractTableExporter;
import org.miradi.utils.MiradiTabDelimitedFileChooser;
import org.miradi.views.ViewDoer;

public class ExportTableDoer extends ViewDoer
{
	@Override
	public boolean isAvailable()
	{
		Project project = getMainWindow().getProject();
		if(!project.isOpen())
			return false;
		
		return getView().isExportableTableAvailable();
	}

	@Override
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		MiradiTabDelimitedFileChooser eamFileChooser = new MiradiTabDelimitedFileChooser(getMainWindow());
		File destination = eamFileChooser.displayChooser();
		if (destination == null) 
			return;
		
		try
		{
			writeTabDeliminted(destination);
			EAM.notifyDialog(EAM.text("Data was exported as tab delimited."));
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.errorDialog(EAM.text("Error occurred while trying to export to a tab delimited file."));
		}
	}

	private void writeTabDeliminted(File destination) throws Exception
	{
		UnicodeWriter out = new UnicodeWriter(destination);
		try
		{
			AbstractTableExporter table = getView().getTableExporter();
			int maxDepth = table.getMaxDepthCount();
			int columnCount = table.getColumnCount();
			int rowCount = table.getRowCount();
			
			out.writeBOM();
			putHeaders(out, table, maxDepth);
			for (int row = 0; row < rowCount; ++row)
			{
				for (int column = 0; column < columnCount; ++column)
				{
					pad(out, table.getDepth(row, column), column);
					String safeValue = getSafeValue(table, row, column);
					out.write(withoutTabsAndNewlines(safeValue) + "\t");
					
					int postPadCount = maxDepth - table.getDepth(row, column);
					pad(out, postPadCount, column);
				}
				
				out.writeln();
			}	
		}
		finally
		{
			out.close();
		}
	}

	private String getSafeValue(AbstractTableExporter table, int row, int column)
	{
		ChoiceItem choiceItem = table.getChoiceItemAt(row, column);
		String label = choiceItem.getLabel();
		if (label == null)
			return "";
		
		return label;
	}

	private void putHeaders(UnicodeWriter out, AbstractTableExporter table, int maxDepeth) throws Exception
	{
		int columnCount = table.getColumnCount();
		for (int column = 0; column < columnCount; ++column)
		{
			out.write(withoutTabsAndNewlines(table.getHeaderFor(column)) + "\t");
			pad(out, maxDepeth, column);
		}
		
		out.writeln();
	}

	private boolean isTreeColumn(int column)
	{
		return (column == 0);
	}
	
	private void pad(UnicodeWriter out, int padCount, int column) throws Exception
	{
		if (!isTreeColumn(column))
			return; 
		
		for (int i = 0; i < padCount; ++i)
		{
			out.write("\t");
		}
	}
	
	private String withoutTabsAndNewlines(String string)
	{
		final String BLANK_SPACE = " ";
		final String TAB = "\t";
		final String NEW_LINE = "\n";
		
		String tabLessString = string.replaceAll(TAB, BLANK_SPACE);
		return tabLessString.replaceAll(NEW_LINE, BLANK_SPACE);
	}
}
