package org.conservationmeasures.eam.dialogs;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.Box;
import javax.swing.Icon;
import javax.swing.JFrame;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.martus.swing.UiButton;
import org.martus.swing.Utilities;

public class FactorPropertiesDialog extends ModelessDialogWithClose
{

	public FactorPropertiesDialog(JFrame parent, FactorPropertiesPanel panel, String headingText)
	{
		super(parent, panel, headingText);
		factorPanel = panel;
	}
	
	public Box createButtonBar()
	{
		Box buttonBar =  super.createButtonBar();
		UiButton  help = new UiButton(new JumpAction("Help",null));
		Component[] components = new Component[] {help};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}

	private Class getJumpAction()
	{
		return ((ObjectManagementPanel)factorPanel.tabs.getSelectedComponent()).getJumpActionClass();
	}

	class JumpAction extends EAMAction
	{

		public JumpAction(String label, Icon icon)
		{
			super(label, icon);
		}

		public void doAction() throws CommandFailedException
		{
			if (getJumpAction()!=null)
				EAM.mainWindow.getActions().get(getJumpAction()).doAction();
			
		}

		public void actionPerformed(ActionEvent e)
		{
			try
			{
				doAction();
			}
			catch(CommandFailedException e1)
			{
				e1.printStackTrace();
			}
		}
	}
	
	FactorPropertiesPanel factorPanel;
}
