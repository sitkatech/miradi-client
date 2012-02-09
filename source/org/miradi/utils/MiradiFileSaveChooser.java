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

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;


abstract public class MiradiFileSaveChooser extends AbstractFileChooser
{
	public MiradiFileSaveChooser(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	protected File doCustomWork(final File chosen)
	{
		if (chosen.exists())
		{
			String title = getDialogOverwriteTitleText();
			String[] body = { getDialogOverwriteBodyText() };
			if (!EAM.confirmOverwriteDialog(title, body))
				return null;
		
			chosen.delete();
		}
		
		return chosen;
	}

	@Override
	protected int getDialogType()
	{
		return JFileChooser.CUSTOM_DIALOG;
	}

	@Override
	public String getDialogTitleText()
	{
		return EAM.substitute(EAM.text("Title|Save %s File"), getUiExtensionTag());
	}

	@Override
	public String getApproveButtonToolTipText()
	{
		return EAM.substitute(EAM.text("TT|Save %s File"), getUiExtensionTag());
	}

	@Override
	public String getApproveButtonText()
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
	
	public abstract String getUiExtensionTag();
	
	public static final String INVALID_PROJECT_FILE_NAME_MESSAGE = EAM.text("File name must contain only alpha numeric and/or '_' characters.");
	public static final String PROJECT_FILE_NOT_FOUND = EAM.text("File could not be created, this can be due to bad characters in file name.");
}
