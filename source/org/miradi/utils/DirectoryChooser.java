/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class DirectoryChooser extends AbstractFileChooser
{
	public DirectoryChooser(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}
	
	@Override
	public File displayChooser()
	{
		JFileChooser dialog = createFileChooserDialog();
		final int result = dialog.showDialog(getMainWindow(), getApproveButtonText());
		if (result != JFileChooser.APPROVE_OPTION)
			return null;

		File selectedFile = dialog.getSelectedFile();
		if (selectedFile == null)
			return null;

		if (!selectedFile.isDirectory())
			return null;
		
		storeCurrentDirectoryForNextTime(selectedFile);

		return selectedFile;
	}

	
	@Override
	protected JFileChooser createFileChooserDialog()
	{
		JFileChooser fileChooser = super.createFileChooserDialog();
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		
		return fileChooser;
	}

	@Override
	protected FileFilter[] getFileFilter()
	{
		return new FileFilter[0];
	}

	@Override
	protected String getApproveButtonText()
	{
		return EAM.text("Button|Chose");
	}

	@Override
	protected String getApproveButtonToolTipText()
	{
		return EAM.text("Button|TT|Choose a directory");
	}

	@Override
	protected String getDialogTitleText()
	{
		return EAM.text("Title|Choose a directory");
	}

	@Override
	protected int getDialogType()
	{
		return JFileChooser.OPEN_DIALOG;
	}

	@Override
	public boolean shouldAllowAllFileFilter()
	{
		return false;
	}
}
