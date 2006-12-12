/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.io.File;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.views.ViewDoer;
import org.martus.swing.UiFileChooser;

public class ImportAccountingCodesDoer extends ViewDoer
{

	public boolean isAvailable() 
	{
		return true;
	}

	public void doIt() throws CommandFailedException 
	{
		File startingDirectory = UiFileChooser.getHomeDirectoryFile();
		String windowTitle = EAM.text("Import Accounting Codes");
		UiFileChooser.FileDialogResults results = UiFileChooser.displayFileOpenDialog(
				getMainWindow(), windowTitle, UiFileChooser.NO_FILE_SELECTED, startingDirectory, null, null);
		
		if (results.wasCancelChoosen())
			return;
		
		File fileToImport = results.getChosenFile();
		String projectName = withoutExtension(fileToImport.getName());
		File finalProjectDirectory = new File(EAM.getHomeDirectory(), projectName);
		finalProjectDirectory.canRead();
		EAM.notifyDialog(EAM.text("Import Competed"));
	}

	private String withoutExtension(String fileName)
	{
		int lastDotAt = fileName.lastIndexOf('.');
		if(lastDotAt < 0)
			return fileName;
		return fileName.substring(0, lastDotAt);
	}

}
