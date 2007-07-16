/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.budget;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.utils.TreeExportFileChooser;
import org.conservationmeasures.eam.utils.TreeTableModelExporter;
import org.conservationmeasures.eam.views.MainWindowDoer;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

import com.java.sun.jtreetable.TreeTableModel;

public class ExportBudgetTreeTableDoer extends MainWindowDoer
{

	public boolean isAvailable()
	{
		String currentViewName = getMainWindow().getCurrentView().cardName();
		String budgetViewName = BudgetView.getViewName();
		
		if (currentViewName.equals(budgetViewName))
			return true;
		
		return false;
	}

	public void doIt() throws CommandFailedException
	{
		if (! isAvailable())
			return;
	
		try
		{
			export();
		}
		catch (Exception e)
		{
			throw new CommandFailedException(e);
		}
	}
	
	public void export() throws Exception
	{
		File fileToExportTo = getFileFromUser();
		UmbrellaView currentView = getMainWindow().getCurrentView();
		TreeTableModel modelToExport = currentView.getTaskTreeTablePanel().getModel();
		TreeTableModelExporter treeTableExporter = new TreeTableModelExporter(fileToExportTo, modelToExport);
		treeTableExporter.export();
	}

	private File getFileFromUser() throws Exception
	{
		TreeExportFileChooser csvFileChooser = new TreeExportFileChooser(getMainWindow());
		File chosenFile = csvFileChooser.displayChooser();
		
		return chosenFile; 
	}
}
