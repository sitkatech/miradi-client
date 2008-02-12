/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.Doer;

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
		addWindowListener(this);
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
		UiButton  help = new PanelButton(new ActionDirections());
		Component[] components = new Component[] {help};
		Utilities.addComponentsRespectingOrientation(buttonBar, components);
	}
	
	protected Class getJumpAction()
	{
		return null;
	}
	
	
	
	protected class ActionDirections extends EAMAction
	{

		public ActionDirections()
		{
			super(EAM.text("Instructions"), "icons/directions.png");
		}
		
		public void doAction() throws CommandFailedException
		{
			if (getJumpAction()!=null)
				mainWindow.getActions().get(getJumpAction()).doAction();
		}
		
		public Doer getDoer()
		{
			return null;
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
}
