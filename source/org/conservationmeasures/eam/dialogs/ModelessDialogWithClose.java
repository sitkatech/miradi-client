/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs; 

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JPanel;

import org.conservationmeasures.eam.actions.EAMAction;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.FastScrollPane;
import org.martus.swing.UiButton;
import org.martus.swing.Utilities;

public class ModelessDialogWithClose extends EAMDialog
{
	public ModelessDialogWithClose(MainWindow parent, DisposablePanel panel, String headingText)
	{
		super(parent);
		setModal(false);
		setTitle(headingText);
		mainWindow = parent;
		wrappedPanel = panel;
	
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(wrappedPanel, BorderLayout.CENTER);
		mainPanel.add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);
		getContentPane().add(new FastScrollPane(mainPanel));
		pack();
		Utilities.fitInScreen(this);
	}
	
	public JPanel getWrappedPanel()
	{
		return wrappedPanel;
	}
	
	private Box createButtonBar()
	{
		UiButton closeButton = new UiButton(EAM.text("Button|Close"));
		closeButton.setSelected(true);
		closeButton.addActionListener(new DialogCloseListener());
		
		getRootPane().setDefaultButton(closeButton);
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), closeButton};
		addAdditoinalButtons(buttonBar);
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	public void addAdditoinalButtons(Box buttonBar)
	{
	}
	
	
	private final class DialogCloseListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			dispose();
		}
	}

	public void dispose()
	{
		if(wrappedPanel == null)
			return;
		
		wrappedPanel.dispose();
		wrappedPanel = null;
		super.dispose();
	}
	
	protected void createDirectionsButton(Box buttonBar)
	{
		UiButton  help = new UiButton(new ActionDirections(EAM.text("Directions")));
		Component[] components = new Component[] {help};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
	}
	
	protected Class getJumpAction()
	{
		return null;
	}
	
	
	
	protected class ActionDirections extends EAMAction
	{

		public ActionDirections(String label)
		{
			super(label, "icons/directions.png");
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

	MainWindow mainWindow;
	DisposablePanel wrappedPanel;
}
