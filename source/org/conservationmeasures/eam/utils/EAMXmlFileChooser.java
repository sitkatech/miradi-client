/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.utils;

import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.main.MainWindow;

public class EAMXmlFileChooser extends EAMFileSaveChooser
{

	public EAMXmlFileChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public String getDialogApproveTitleText()
	{
		return "Title|Save Xml File";
	}

	public String getApproveButtonToolTipText()
	{
		return "TT|Save Xml File";
	}

	public String getDialogApprovelButtonText()
	{
		return "Save Xml";
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
		return new XMLFileFilter();
	}

	public String getFileExtension()
	{
		return XMLFileFilter.EXTENSION;
	}

}
