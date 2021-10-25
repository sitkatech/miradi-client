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

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;


abstract public class MiradiFileSaveChooser extends AbstractFileChooser
{
    public MiradiFileSaveChooser(MainWindow mainWindowToUse)
    {
        super(mainWindowToUse);
    }

	@Override
	protected File doCustomWork(final File file)
	{
		if (file.exists())
		{
			String title = getDialogOverwriteTitleText();
			if (!EAM.confirmOverwriteDialog(title, getDialogOverwriteBodyText()))
				return null;
		
			try
			{
				FileUtilities.deleteExistingWithRetries(file);
			}
			catch (Exception e)
			{
				EAM.alertUserOfNonFatalException(e);
			}
		}
		
		return file;
	}

	@Override
	protected int getDialogType()
	{
		return JFileChooser.CUSTOM_DIALOG;
	}

	@Override
	protected String getDialogTitleText()
	{
		return EAM.substituteSingleString(EAM.text("Title|Save %s File"), getFileExtensionLabel());
	}

	@Override
	protected String getApproveButtonToolTipText()
	{
		return EAM.substituteSingleString(EAM.text("TT|Save %s File"), getFileExtensionLabel());
	}

	@Override
	protected String getApproveButtonText()
	{
		return EAM.substituteSingleString(EAM.text("Button|Save %s"), getFileExtensionLabel());
	}

    @Override
    protected File getOptionalSelectedFile()
    {
        return optionalSelectedFile;
    }

    protected void setOptionalSelectedFile(File file)
    {
        optionalSelectedFile = file;
    }

    protected File getDefaultProjectExportSelectedFile()
    {
        String fileName = getMainWindow().getProject().getFilename();
        return new File(fileName + "." + getFileExtensionLabel());
    }

	private String getDialogOverwriteTitleText()
	{
		return EAM.text("Title|Overwrite existing file?");
	}

	private String getDialogOverwriteBodyText()
	{
		return EAM.text("This will replace the existing file.");
	}
	
	@Override
	public boolean shouldAllowAllFileFilter()
	{
		return false;
	}

    protected abstract String getFileExtensionLabel();
    private File optionalSelectedFile;
	
	public static final String INVALID_PROJECT_FILE_NAME_MESSAGE = EAM.text("File name must contain only alpha numeric and/or '_' characters.");
	public static final String PROJECT_FILE_NOT_FOUND = EAM.text("File could not be created, this can be due to bad characters in file name.");
}
