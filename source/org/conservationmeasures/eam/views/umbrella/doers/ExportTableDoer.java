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
		
		try
		{
			writeTabDeliminted(destination);
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
			int columnCount = table.getColumnCount();
			int rowCount = table.getRowCount();
			for (int row = 0; row < rowCount; ++row)
			{
				for (int column = 0; column < columnCount; ++column)
				{
					String value = table.getValueFor(row, column);
					if (value != null)
						out.write(value + "\t");
					else
						out.write("\t");
				}
				
				out.writeln();
			}	
		}
		finally
		{
			out.close();
		}
	}
}
