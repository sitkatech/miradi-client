/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella.doers;

import java.io.File;
import java.io.IOException;

import org.martus.util.UnicodeWriter;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.utils.ExportableTableInterface;
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
		
		//FIXME for dev only
		//File destination = new File("C:/Users/Nima/Documents/some.txt");
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
			ExportableTableInterface table = getView().getExportableTable();
			int maxDepth = table.getMaxDepthCount();
			int columnCount = table.getColumnCount();
			int rowCount = table.getRowCount();
			
			putHeaders(out, table, maxDepth);
			for (int row = 0; row < rowCount; ++row)
			{
				for (int column = 0; column < columnCount; ++column)
				{
					pad(out, table.getDepth(row), column);
					out.write(getSafeValue(table, row, column) + "\t");
					
					int postPadCount = maxDepth - table.getDepth(row);
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

	private String getSafeValue(ExportableTableInterface table, int row, int column)
	{
		Object value = table.getValueAt(row, column);
		if (value == null)
			return "";
		
		return value.toString();
	}

	private void putHeaders(UnicodeWriter out, ExportableTableInterface table, int maxDepeth) throws Exception
	{
		int columnCount = table.getColumnCount();
		for (int column = 0; column < columnCount; ++column)
		{
			out.write(table.getHeaderFor(column) + "\t");
			pad(out, maxDepeth, column);
		}
		
		out.writeln();
	}

	private boolean isTreeColumn(int column)
	{
		return (column == 0);
	}
	
	private void pad(UnicodeWriter out, int padCount, int column) throws IOException
	{
		if (!isTreeColumn(column))
			return; 
		
		for (int i = 0; i < padCount; ++i)
		{
			out.write("\t");
		}
	}
}
