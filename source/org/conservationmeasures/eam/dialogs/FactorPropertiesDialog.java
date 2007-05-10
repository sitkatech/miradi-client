package org.conservationmeasures.eam.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.Icon;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.swing.UiButton;
import org.martus.swing.Utilities;

public class FactorPropertiesDialog extends ModelessDialogWithClose
{

	public FactorPropertiesDialog(MainWindow parent, FactorPropertiesPanel panel, String headingText)
	{
		super(parent, panel, headingText);
		factorPanel = panel;
		mainWindow = parent;
	}
	
	public void addAdditoinalButtons(Box buttonBar)
	{
		UiButton  help = new UiButton(new ActionDirections("Help",null));
		Component[] components = new Component[] {help};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
	}

	private Class getJumpAction()
	{
		return ((ModelessDialogPanel)factorPanel.tabs.getSelectedComponent()).getJumpActionClass();
	}

	class ActionDirections extends EAMAction
	{

		public ActionDirections(String label, Icon icon)
		{
			super(label, icon);
		}

		public void doAction() throws CommandFailedException
		{
			if (getJumpAction()!=null)
				mainWindow.getActions().get(getJumpAction()).doAction();
			
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				doAction();
			}
			catch(CommandFailedException e1)
			{
				EAM.logException(e1);
			}
		}
	}
	
	FactorPropertiesPanel factorPanel;
	MainWindow mainWindow;
}
