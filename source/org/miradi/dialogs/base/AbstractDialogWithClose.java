/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.martus.swing.UiButton;
import org.martus.swing.Utilities;
import org.miradi.actions.EAMAction;
import org.miradi.actions.MainWindowAction;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;

abstract public class AbstractDialogWithClose extends EAMDialog implements WindowListener
{
	protected AbstractDialogWithClose(MainWindow parent, DisposablePanel panel, String headingText)
	{
		super(parent);
		
		setTitle(headingText);
		mainWindow = parent;
		wrappedPanel = panel;
	
		getContentPane().add(createMainPanel());
		getContentPane().add(createButtonBar(), BorderLayout.AFTER_LAST_LINE);
		pack();
		Utilities.fitInScreen(this);
		getContentPane().setBackground(AppPreferences.getDarkPanelBackgroundColor());
		addWindowListener(this);
	}
	
	@Override
	public void setVisible(boolean b)
	{
		if(b)
			updateDirectionsEnabledState();
		super.setVisible(b);
	}

	protected JComponent createMainPanel()
	{
		JPanel mainPanel = new JPanel(new BorderLayout());
		mainPanel.add(wrappedPanel, BorderLayout.CENTER);
		JComponent mainScrollPane = possiblyWrapInScrollPane(mainPanel);
		return mainScrollPane;
	}

	// TODO: Should probably have AbstractDialogWithClose and also 
	// AbstractDialogWithScrollingAndClose as separate classes
	protected JComponent possiblyWrapInScrollPane(JPanel mainPanel)
	{
		MiradiScrollPane mainScrollPane = new MiradiScrollPane(mainPanel);
		return mainScrollPane;
	}
	
	public JPanel getWrappedPanel()
	{
		return wrappedPanel;
	}
	
	private Box createButtonBar()
	{
		UiButton closeButton = new PanelButton(EAM.text("Button|Close"));
		closeButton.setSelected(true);
		closeButton.addActionListener(new DialogCloseListener());
		
		getRootPane().setDefaultButton(closeButton);
		
		Box buttonBar = Box.createHorizontalBox();
		Component[] components = new Component[] {Box.createHorizontalGlue(), closeButton};
		addAdditionalButtons(buttonBar);
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
		return buttonBar;
	}
	
	public void addAdditionalButtons(Box buttonBar)
	{
		createDirectionsButton(buttonBar);
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
	
	protected void createDirectionsButton(Box buttonBarToUse)
	{
		actionDirections = new ActionDirections(mainWindow);
		UiButton  help = new PanelButton(actionDirections);
		Component[] components = new Component[] {help};
		Utilities.addComponentsRespectingOrientation(buttonBarToUse, components);
	}
	
	public void updateDirectionsEnabledState()
	{
		actionDirections.updateEnabledState();
	}

	protected Class getJumpAction()
	{
		return null;
	}
	
	
	
	protected class ActionDirections extends MainWindowAction
	{

		public ActionDirections(MainWindow mainWindowToUse)
		{
			super(mainWindowToUse, EAM.text("Instructions"), "icons/directions.png");
		}
		
		public void doAction() throws CommandFailedException
		{
			EAMAction action = getRealJumpAction();
			if(action == null)
				return;
			
			action.doAction();
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
		
		@Override
		public boolean shouldBeEnabled()
		{
			EAMAction action = getRealJumpAction();
			if(action == null)
				return false;
			
			return action.shouldBeEnabled();
		}
		
		private EAMAction getRealJumpAction()
		{
			Class jumpActionClass = getJumpAction();
			if (jumpActionClass == null)
				return null;
			
			return mainWindow.getActions().get(jumpActionClass);
		}
	}

	public void windowActivated(WindowEvent arg0)
	{
		mainWindow.updateActionStates();
	}

	public void windowClosed(WindowEvent arg0)
	{
	}

	public void windowClosing(WindowEvent arg0)
	{
		dispose();
	}

	public void windowDeactivated(WindowEvent arg0)
	{
	}

	public void windowDeiconified(WindowEvent arg0)
	{
	}

	public void windowIconified(WindowEvent arg0)
	{
	}

	public void windowOpened(WindowEvent arg0)
	{
		mainWindow.updateActionStates();
	}
	

	private MainWindow mainWindow;
	private DisposablePanel wrappedPanel;
	private ActionDirections actionDirections;
}
