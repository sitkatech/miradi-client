/*
 * Copyright 2005, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;

import java.io.File;

import javax.swing.JFileChooser;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public abstract class EAMFileChooser {

	MainWindow mainWindow;
	
	EAMFileChooser(MainWindow mainWindow){
		this.mainWindow = mainWindow;
	}
	
	public File displayChooser()
			throws CommandFailedException {
		JFileChooser dlg = new JFileChooser();
		dlg.setDialogTitle(EAM.text(getDialogTitleText()));
		dlg.setFileFilter(new ZIPFileFilter());
		dlg.setDialogType(JFileChooser.CUSTOM_DIALOG);
		dlg.setApproveButtonToolTipText(EAM.text(getApproveButtonToolTipText()));
		if (dlg.showDialog(mainWindow, EAM.text(getDialogButtonText())) != JFileChooser.APPROVE_OPTION)
			return null;

		File chosen = dlg.getSelectedFile();
		if (!chosen.getName().toLowerCase().endsWith(
				ZIPFileFilter.ZIP_EXTENSION))
			chosen = new File(chosen.getAbsolutePath()
					+ ZIPFileFilter.ZIP_EXTENSION);

		if (chosen.exists()) {
			String title = EAM.text(getDialogOverideTitleText());
			String[] body = { EAM.text(getDialogOverideBodyText()) };
			if (!EAM.confirmDialog(title, body))
				return null;
			chosen.delete();
		}

		return chosen;

	}

	
	public abstract String getDialogTitleText();
	public abstract String getApproveButtonToolTipText();
	public abstract String getDialogButtonText();
	public abstract String getDialogOverideTitleText();
	public abstract String getDialogOverideBodyText();
	
	
}
