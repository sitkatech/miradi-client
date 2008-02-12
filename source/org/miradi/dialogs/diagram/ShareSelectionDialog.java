/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.miradi.dialogs.diagram;

import org.miradi.dialogs.base.AbstractSelectionDialog;
import org.miradi.dialogs.base.ObjectTablePanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;

public class ShareSelectionDialog extends AbstractSelectionDialog
{
	public ShareSelectionDialog(MainWindow mainWindow, String title, ObjectTablePanel poolTable)
	{
		super(mainWindow, title, poolTable);
	}

	protected String createCustomButtonLabel()
	{
		return EAM.text("Share");
	}

	protected String getPanelTitleInstructions()
	{
		return EAM.text("Please select which item should be shared to this factor, then press the Share button");
	}
}
