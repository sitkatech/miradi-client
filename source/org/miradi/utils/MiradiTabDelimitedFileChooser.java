/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.MainWindow;

public class MiradiTabDelimitedFileChooser extends EAMFileSaveChooser
{
	public MiradiTabDelimitedFileChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public String getDialogApproveTitleText()
	{
		return "Title|Save Tab Delimited File";
	}

	public String getApproveButtonToolTipText()
	{
		return "TT|Save Tab Delimited File";
	}

	public String getDialogApprovelButtonText()
	{
		return "Save Tab Delimited";
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
		return new FileFilter[] {new TabDelimitedFileFilter(),};
	}
}
