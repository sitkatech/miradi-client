/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

abstract public class MiradiFileOpenChooser extends AbstractFileChooser
{
	MiradiFileOpenChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	public File displayChooser()
	{
		
		JFileChooser dlg = new JFileChooser(currentDirectory);

		dlg.setDialogTitle(getDialogTitleText());
		FileFilter[] filters = getFileFilter();
		for (int i=0; i<filters.length; ++i)
		{
			dlg.addChoosableFileFilter(filters[i]);
		}
		
		dlg.setDialogType(JFileChooser.OPEN_DIALOG);
		dlg.setApproveButtonToolTipText(getApproveButtonToolTipText());
		if (dlg.showDialog(getMainWindow(), getApproveButtonText()) != JFileChooser.APPROVE_OPTION)
			return null;

		File chosen = dlg.getSelectedFile();
		chosen = getFileWithExtension(dlg, chosen);

		currentDirectory = chosen.getParent();
		return chosen;

	}
}
