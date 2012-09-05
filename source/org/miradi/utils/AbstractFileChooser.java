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
import java.util.Vector;

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
		selectedFile = getFileWithExtension(dialog, selectedFile);
		selectedFile = doCustomWork(selectedFile);
		if (selectedFile == null)
			return null;

		storeCurrentDirectoryForNextTime(selectedFile);

		return selectedFile;
	}

	private void storeCurrentDirectoryForNextTime(File selectedFile)
	{
		currentDirectory = selectedFile.getParent();
	}

	private JFileChooser createFileChooserDialog()
	{
		JFileChooser dialog = new JFileChooser(currentDirectory);
		dialog.setDialogTitle(getDialogTitleText());
		addFileFilters(dialog);
		dialog.setDialogType(getDialogType());
		dialog.setApproveButtonToolTipText(getApproveButtonToolTipText());
		
		return dialog;
	}

	private void addFileFilters(JFileChooser dialog)
	{
		FileFilter[] filters = getFileFilter();
		addFileFilters(dialog, filters);
	}
	
	public static File getFileWithExtension(JFileChooser fileChooser, File file)
	{
		Vector<MiradiFileFilter> nonAcceptAllFileFilters = getNonAcceptAllFileFilters(fileChooser);
		if (endsWithAnyOfExtensions(file, nonAcceptAllFileFilters))
			return file;
		
		final FileFilter rawFileFilter = fileChooser.getFileFilter();
		if (!fileChooser.getAcceptAllFileFilter().equals(rawFileFilter))
		{
			final MiradiFileFilter castedFileFilter = (MiradiFileFilter) rawFileFilter;
			return new File(file.getAbsolutePath() + castedFileFilter.getFileExtension());
		}
		
		return createNewFileWithExtension(file, nonAcceptAllFileFilters);
	}
	
	private static File createNewFileWithExtension(File fileWithoutExtension, Vector<MiradiFileFilter> nonAllFileFilters)
	{
		if (nonAllFileFilters.size() == 0)
			return fileWithoutExtension;
		
		final String fileExtension = nonAllFileFilters.get(0).getFileExtension();
		return new File(fileWithoutExtension + fileExtension);
	}

	private static boolean endsWithAnyOfExtensions(File file, Vector<MiradiFileFilter> nonAcceptAllFileFilters)
	{
		for (MiradiFileFilter miradiFileFilter : nonAcceptAllFileFilters)
		{
			if (file.getName().endsWith(miradiFileFilter.getFileExtension()))
				return true;
		}
		
		return false;
	}

	private static Vector<MiradiFileFilter> getNonAcceptAllFileFilters(JFileChooser fileChooser)
	{
		final FileFilter[] fileChooserFileFilters = fileChooser.getChoosableFileFilters();
		Vector<MiradiFileFilter> nonAllFilters = new Vector<MiradiFileFilter>();
		for(FileFilter fileFilter : fileChooserFileFilters)
		{
			if (!fileChooser.getAcceptAllFileFilter().equals(fileFilter))
				nonAllFilters.add((MiradiFileFilter) fileFilter);
		}
		
		return nonAllFilters;
	}
	
	protected File doCustomWork(final File file)
	{
		return file;
	}
	
	private MainWindow getMainWindow()
	{
		return mainWindow;
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

	private MainWindow mainWindow;
	
	protected static String currentDirectory;
}
