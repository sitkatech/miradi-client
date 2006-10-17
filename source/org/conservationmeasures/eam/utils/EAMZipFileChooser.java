/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;


import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.MainWindow;

public class EAMZipFileChooser extends EAMFileChooser
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

	public FileFilter getFileFilter()
	{
		return new ZIPFileFilter();
	}

	public String getFileExtension()
	{
		return ZIPFileFilter.ZIP_EXTENSION;
	}

}
