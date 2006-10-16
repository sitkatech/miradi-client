/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */

package org.conservationmeasures.eam.utils;

import org.conservationmeasures.eam.main.MainWindow;

public class EAMZipFileChooser extends EAMFileChooser {

	public EAMZipFileChooser(MainWindow mainWindow) {
		super(mainWindow);
	}

public String getDialogTitleText() {return "Title|Save Zip File";}	

public String getApproveButtonToolTipText(){return "TT|Save Zip File";}	

public String getDialogButtonText(){return "Save Zip";}	

public String getDialogOverideTitleText(){return "Title|Overwrite existing file?";}	

public String getDialogOverideBodyText(){return "This will replace the existing file.";}	

}
