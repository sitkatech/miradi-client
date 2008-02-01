/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella.doers;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.ExportableTableInterface;
import org.conservationmeasures.eam.utils.MiradiTabDelimitedFileChooser;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.util.UnicodeWriter;

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
					prePadWithTabs(out, column, table.getDepth(row));
					out.write(table.getValueFor(row, column) + "\t");
					postPadWithTabs(out, column, table.getDepth(row), maxDepth);
				}
				
				out.writeln();
			}	
		}
		finally
		{
			out.close();
		}
	}

	private void putHeaders(UnicodeWriter out, ExportableTableInterface table, int maxDepeth) throws Exception
	{
		int columnCount = table.getColumnCount();
		
		for (int column = 0; column < columnCount; ++column)
		{
			out.write(table.getHeaderFor(column) + "\t");
			prePadWithTabs(out, column, maxDepeth);
		}
		
		out.writeln();
	}

	private boolean isTreeColumn(int column)
	{
		return (column == 0);
	}
	
	private void prePadWithTabs(UnicodeWriter out, int column, int depth) throws Exception
	{	
		if (!isTreeColumn(column))
			return; 
		
		for (int depthCount = 0; depthCount < depth; ++depthCount)
		{
			out.write("\t");
		}
	}
	
	private void postPadWithTabs(UnicodeWriter out, int column, int depth, int maxDepth) throws Exception
	{
		if (!isTreeColumn(column))
			return; 

		int postPadCount = maxDepth - depth;
		for (int depthCount = 0; depthCount < postPadCount; ++depthCount)
		{
			out.write("\t");
		}
	}
}
