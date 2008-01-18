/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;


import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.MainWindow;

public class EAMZipFileChooser extends EAMFileSaveChooser
{

	public EAMZipFileChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public String getDialogApproveTitleText()
	{
		return "Title|Save Zip File";
	}

	public String getApproveButtonToolTipText()
	{
		return "TT|Save Zip File";
	}

	public String getDialogApprovelButtonText()
	{
		return "Save Zip";
	}

	public String getDialogOverwriteTitleText()
	{
		return "Title|Overwrite existing file?";
	}

	public String getDialogOverwriteBodyText()
	{
		return "This will replace the existing file.";
	}

	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new ZIPFileFilter(), new MPZFileFilter()};
	}
}
