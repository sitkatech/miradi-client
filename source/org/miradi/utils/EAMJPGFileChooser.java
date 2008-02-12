/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.MainWindow;

public class EAMJPGFileChooser extends EAMFileSaveChooser
{

	public EAMJPGFileChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public String getDialogApproveTitleText()
	{
		return "Title|Save JPEG File";
	}

	public String getApproveButtonToolTipText()
	{
		return "TT|Save JPEG File";
	}

	public String getDialogApprovelButtonText()
	{
		return "Save JPEG";
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
		return new FileFilter[] {new JPEGFileFilter()};
	}
}
