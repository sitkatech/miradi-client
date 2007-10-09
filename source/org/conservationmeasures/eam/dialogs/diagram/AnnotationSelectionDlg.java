package org.conservationmeasures.eam.dialogs.diagram;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import org.conservationmeasures.eam.dialogs.AbstractSelectionDlg;
import org.conservationmeasures.eam.dialogs.ObjectTablePanel;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelButton;
import org.conservationmeasures.eam.dialogs.fieldComponents.PanelTitleLabel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;

public class AnnotationSelectionDlg extends AbstractSelectionDlg
{
	public AnnotationSelectionDlg(MainWindow mainWindow, String title, ObjectTablePanel poolTable)
	{
		super(mainWindow, title, poolTable);
	}

	protected PanelButton createCustomButton()
	{
		return  new PanelButton(new CloneAction());
	}
	
	protected PanelTitleLabel getPanelTitleInstructions()
	{
		return new PanelTitleLabel(EAM.text("Please select which item should be cloned into this factor, then press the Clone button"));
	}
	
	class CloneAction extends AbstractAction
	{
		public CloneAction()
		{
			super(EAM.text("Clone"));
		}

		public void actionPerformed(ActionEvent arg0)
		{
			objectSelected = list.getSelectedObject();
			dispose();
		}
	}
}
