package org.conservationmeasures.eam.dialogs;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class AnnotationSelectionDlg extends AbstractSelectionDlg
{
	public AnnotationSelectionDlg(MainWindow mainWindow, String title, ObjectTablePanel poolTable)
	{
		super(mainWindow, title, poolTable);
	}

	protected String createCustomButtonLabel()
	{
		return  EAM.text("Clone");
	}
	
	protected String getPanelTitleInstructions()
	{
		return EAM.text("Please select which item should be cloned into this factor, then press the Clone button");
	}
}
