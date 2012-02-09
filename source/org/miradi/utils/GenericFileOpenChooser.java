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

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class GenericFileOpenChooser extends MiradiFileOpenChooser
{
	public GenericFileOpenChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	protected FileFilter[] getFileFilter()
	{
		return new FileFilter[0];
	}

	@Override
	protected String getApproveButtonText()
	{
		return EAM.text("Open");
	}

	@Override
	protected String getApproveButtonToolTipText()
	{
		return EAM.text("Open Selected File");
	}

	@Override
	protected String getDialogTitleText()
	{
		return EAM.text("Open");
	}
}
