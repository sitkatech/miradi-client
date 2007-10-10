package org.conservationmeasures.eam.dialogs.diagram;

import org.conservationmeasures.eam.dialogs.AbstractSelectionDialog;
import org.conservationmeasures.eam.dialogs.ObjectTablePanel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

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
