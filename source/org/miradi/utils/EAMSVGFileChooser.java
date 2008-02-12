/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.utils;

import javax.swing.filechooser.FileFilter;

import org.miradi.main.MainWindow;

public class EAMSVGFileChooser extends EAMFileSaveChooser
{

	public EAMSVGFileChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public String getDialogApproveTitleText()
	{
		return "Title|Save SVG Image File";
	}

	public String getApproveButtonToolTipText()
	{
		return "TT|Save SVG File";
	}

	public String getDialogApprovelButtonText()
	{
		return "Save SVG";
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
		return new FileFilter[] {new SVGFileFilter()};
	}
}
