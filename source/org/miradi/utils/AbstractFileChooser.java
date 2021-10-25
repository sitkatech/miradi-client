/* 
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
		JFileChooser dialog = createFileChooserDialog();
		final int result = dialog.showDialog(getMainWindow(), getApproveButtonText());
		if (result != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = dialog.getSelectedFile();
		if (selectedFile == null)
			return null;
		
		selectedFile = getFileWithExtension(dialog, selectedFile);
		selectedFile = doCustomWork(selectedFile);
		if (selectedFile == null)
			return null;

		storeCurrentDirectoryForNextTime(selectedFile);

		return selectedFile;
	}

	protected void storeCurrentDirectoryForNextTime(File selectedFile)
	{
		currentDirectory = selectedFile.getParent();
	}

    protected JFileChooser createFileChooserDialog()
    {
        JFileChooser dialog = new JFileChooser(currentDirectory);
        dialog.setAcceptAllFileFilterUsed(shouldAllowAllFileFilter());
        dialog.setDialogTitle(getDialogTitleText());
        addFileFilters(dialog);
        dialog.setDialogType(getDialogType());
        dialog.setApproveButtonToolTipText(getApproveButtonToolTipText());
        if(getOptionalSelectedFile() != null)
        {
            dialog.setSelectedFile(getOptionalSelectedFile());
        }
        return dialog;
    }

	private void addFileFilters(JFileChooser dialog)
	{
		FileFilter[] filters = getFileFilter();
		addFileFilters(dialog, filters);
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

	public static File getFileNameWithExtension(File chosen, String fileExtension)
	{
		if (!chosen.getName().toLowerCase().endsWith(fileExtension.toLowerCase()))
			chosen = new File(chosen.getAbsolutePath() + fileExtension);
		
		return chosen;
	}
	
	protected File doCustomWork(final File file)
	{
		return file;
	}
	
	protected MainWindow getMainWindow()
	{
		return mainWindow;
	}

    protected File getOptionalSelectedFile()
    {
        return null;
    }

	public static void addFileFilters(JFileChooser fileChooser, FileFilter[] filters)
	{
		for (int i = 0; i < filters.length; ++i)
		{
			fileChooser.addChoosableFileFilter(filters[i]);
			fileChooser.setFileFilter(filters[i]);
		}
	}
	
	abstract protected FileFilter[] getFileFilter();
	
	abstract protected String getApproveButtonText();
	
	abstract protected String getApproveButtonToolTipText();
	
	abstract protected String getDialogTitleText();
	
	abstract protected int getDialogType();
	
	abstract public boolean shouldAllowAllFileFilter();

	private MainWindow mainWindow;
	
	protected static String currentDirectory;
}
