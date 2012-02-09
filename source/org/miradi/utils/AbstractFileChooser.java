/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.miradi.main.MainWindow;

abstract public class AbstractFileChooser
{
	public AbstractFileChooser(MainWindow mainWindowToUse)
	{
		mainWindow = mainWindowToUse;
	}
	
	public File displayChooser()
	{
		JFileChooser dialog = new JFileChooser(currentDirectory);
		dialog.setDialogTitle(getDialogTitleText());
		FileFilter[] filters = getFileFilter();
		for (int i=0; i<filters.length; ++i)
		{
			dialog.addChoosableFileFilter(filters[i]);
		}
		
		dialog.setDialogType(getDialogType());
		dialog.setApproveButtonToolTipText(getApproveButtonToolTipText());
		if (dialog.showDialog(getMainWindow(), getApproveButtonText()) != JFileChooser.APPROVE_OPTION)
			return null;

		File chosen = dialog.getSelectedFile();
		chosen = getFileWithExtension(dialog, chosen);
		chosen = doCustomWork(chosen);
		if (chosen == null)
			return null;

		currentDirectory = chosen.getParent();

		return chosen;
	}
	
	public static File getFileWithExtension(JFileChooser fileChooser, File chosen)
	{
		FileFilter rawFileFilter = fileChooser.getFileFilter();
		if (!fileChooser.getAcceptAllFileFilter().equals(rawFileFilter))
		{
			MiradiFileFilter fileFilter = (MiradiFileFilter)rawFileFilter;
			chosen = getFileNameWithExtension(chosen, fileFilter.getFileExtension());
		}
		
		return chosen;
	}
	
	private static File getFileNameWithExtension(File chosen, String fileExtension)
	{
		if (!chosen.getName().toLowerCase().endsWith(fileExtension.toLowerCase()))
			chosen = new File(chosen.getAbsolutePath() + fileExtension);
		
		return chosen;
	}
	
	protected File doCustomWork(final File chosen)
	{
		return chosen;
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	abstract protected FileFilter[] getFileFilter();
	
	abstract protected String getApproveButtonText();
	
	abstract protected String getApproveButtonToolTipText();
	
	abstract protected String getDialogTitleText();
	
	abstract protected int getDialogType();

	private MainWindow mainWindow;
	
	protected static String currentDirectory;
}
