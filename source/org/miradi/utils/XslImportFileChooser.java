/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

import javax.swing.filechooser.FileFilter;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class XslImportFileChooser extends MiradiFileOpenChooser
{
	public XslImportFileChooser(MainWindow mainWindow)
	{
		super(mainWindow);
	}

	@Override
	public FileFilter[] getFileFilter()
	{
		return new FileFilter[] {new XsltFileFilter(), new XslFileFilter(), };
	}

	@Override
	protected String getApproveButtonText()
	{
		return EAM.text("Import");
	}

	@Override
	protected String getApproveButtonToolTipText()
	{
		return EAM.text("TT|Import Xsl File");
	}

	@Override
	protected String getDialogTitleText()
	{
		return EAM.text("Select File to Import");
	}
}

