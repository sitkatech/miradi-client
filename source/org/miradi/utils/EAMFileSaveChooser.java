/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;


public abstract class EAMFileSaveChooser
{

	MainWindow mainWindow;

	EAMFileSaveChooser(MainWindow mainWindow)
	{
		this.mainWindow = mainWindow;
	}

	public File displayChooser() throws CommandFailedException
	{
		
		JFileChooser dlg = new JFileChooser(currentDirectory);

		dlg.setDialogTitle(getDialogApproveTitleText());
		FileFilter[] filters = getFileFilter();
		for (int i=0; i<filters.length; ++i)
		{
			dlg.addChoosableFileFilter(filters[i]);
		}
		
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText(getApproveButtonToolTipText());
		if (dlg.showDialog(mainWindow, getDialogApprovelButtonText()) != JFileChooser.APPROVE_OPTION)
			return null;

		File chosen = dlg.getSelectedFile();
		chosen = getFileWithExtension(dlg, chosen);

		if (chosen.exists())
		{
			String title = getDialogOverwriteTitleText();
			String[] body = { getDialogOverwriteBodyText() };
			if (!EAM.confirmDialog(title, body))
				return null;
			chosen.delete();
		}

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

	public static File getFileNameWithExtension(File chosen, String fileExtension)
	{
		if (!chosen.getName().toLowerCase().endsWith(fileExtension.toLowerCase()))
			chosen = new File(chosen.getAbsolutePath() + fileExtension);
		
		return chosen;
	}
	
	public String getDialogApproveTitleText()
	{
		return EAM.substitute(EAM.text("Title|Save %s File"), getUiExtensionTag());
	}

	public String getApproveButtonToolTipText()
	{
		return EAM.substitute(EAM.text("TT|Save %s File"), getUiExtensionTag());
	}

	public String getDialogApprovelButtonText()
	{
		return EAM.substitute(EAM.text("Save %s"), getUiExtensionTag());
	}

	public String getDialogOverwriteTitleText()
	{
		return EAM.text("Title|Overwrite existing file?");
	}

	public String getDialogOverwriteBodyText()
	{
		return EAM.text("This will replace the existing file.");
	}
	
	public abstract FileFilter[] getFileFilter();
	
	public abstract String getUiExtensionTag();
	
	private static String currentDirectory;
	
	public static final String INVALID_PROJECT_FILE_NAME_MESSAGE = EAM.text("File name must contain only alpha numeric and/or '_' characters.");
	public static final String PROJECT_FILE_NOT_FOUND = EAM.text("File could not be created, this can be due to bad characters in file name.");
}
