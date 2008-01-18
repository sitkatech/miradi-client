/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.utils;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;


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

		dlg.setDialogTitle(EAM.text(getDialogApproveTitleText()));
		FileFilter[] filters = getFileFilter();
		for (int i=0; i<filters.length; ++i)
			dlg.addChoosableFileFilter(filters[i]);
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText(EAM.text(getApproveButtonToolTipText()));
		if (dlg.showDialog(mainWindow, EAM.text(getDialogApprovelButtonText())) != JFileChooser.APPROVE_OPTION)
			return null;

		File chosen = dlg.getSelectedFile();
		String ext = ((MiradiFileFilter)dlg.getFileFilter()).getFileExtension();
		if (!chosen.getName().toLowerCase().endsWith(ext))
			chosen = new File(chosen.getAbsolutePath() + ext);

		if (chosen.exists())
		{
			String title = EAM.text(getDialogOverwriteTitleText());
			String[] body = { EAM.text(getDialogOverwriteBodyText()) };
			if (!EAM.confirmDialog(title, body))
				return null;
			chosen.delete();
		}

		currentDirectory = chosen.getParent();
		return chosen;

	}

	public abstract String getDialogApproveTitleText();

	public abstract String getApproveButtonToolTipText();

	public abstract String getDialogApprovelButtonText();

	public abstract String getDialogOverwriteTitleText();

	public abstract String getDialogOverwriteBodyText();

	public abstract FileFilter[] getFileFilter();
	
	private static String currentDirectory;

}
