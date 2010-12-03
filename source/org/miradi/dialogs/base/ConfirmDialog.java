/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.FlexibleWidthHtmlViewer;

public class ConfirmDialog extends DialogWithDisposablePanelAndMainWindowUpdating
{
	public static boolean confirm(MainWindow mainWindowToUse, String titleToUse, String confirmationTextToUse, String yesButtonTextToUse)
	{
		ConfirmDialogTemplate template = new ConfirmDialogTemplate(titleToUse, confirmationTextToUse, yesButtonTextToUse);
		return confirm(mainWindowToUse, template);
	}
	
	public static boolean confirm(MainWindow mainWindow, ConfirmDialogTemplate template)
	{
		return isYesButton(showDialog(mainWindow, template));
	}
	
	private ConfirmDialog(MainWindow mainWindow, ConfirmDialogTemplate template)
	{
		super(mainWindow, new DisposablePanel(new OneColumnGridLayout()));
		
		pressedButtonIndex = -1;
		
		setModal(true);
		JPanel panel = getWrappedPanel();

		Color backgroundColor = getMainWindow().getAppPreferences().getWizardBackgroundColor();
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		FlexibleWidthHtmlViewer htmlPanel = new FlexibleWidthHtmlViewer(mainWindow, template.getConfirmationText());
		panel.add(htmlPanel);

		panel.add(new FillerLabel());
		Box buttonPanel = Box.createHorizontalBox();
		buttonPanel.setBackground(backgroundColor);
		buttonPanel.add(Box.createHorizontalGlue());
		for(int i = 0; i < template.getButtonLabels().length; ++i)
		{
			String buttonText = template.getButtonLabels()[i];
			JButton button = new JButton(buttonText);
			button.addActionListener(new ButtonListener(i));
			buttonPanel.add(new FillerLabel());
			buttonPanel.add(button);
		}
		panel.add(buttonPanel);

		// Try to avoid having the window narrower than the title
		setMinimumSize(new Dimension(400, 0));
		setTitle(template.getTitle());
		// Set width so HTML will wrap appropriately
		htmlPanel.setPreferredSize(new Dimension(700, 10));
		pack();
		// Clear dimension set earlier, so the height can be as tall as it wants
		htmlPanel.setPreferredSize(null);
		pack();
	}

	private static int showDialog(MainWindow mainWindow, ConfirmDialogTemplate template)
	{
		ConfirmDialog dialog = new ConfirmDialog(mainWindow, template);
		dialog.showDialog();
		return dialog.pressedButtonIndex;
	}

	class ButtonListener implements ActionListener
	{
		public ButtonListener(int buttonIndex)
		{
			thisButtonIndex = buttonIndex;
		}

		public void actionPerformed(ActionEvent event)
		{
			buttonWasPressed(thisButtonIndex);
		}

		private int thisButtonIndex;
	}
	
	private void buttonWasPressed(int buttonIndex)
	{
		pressedButtonIndex = buttonIndex;
		dispose();
	}

	private static boolean isYesButton(int button)
	{
		if (button == 0)
			return true;
		
		return false;
	}
	
	public static boolean confirmWithinThread(MainWindow mainWindowToUse, ConfirmDialogTemplate templateToUse)
	{
		ThreadedConfirmDialogLauncher launcher = new ThreadedConfirmDialogLauncher(mainWindowToUse, templateToUse);
		try
		{
			SwingUtilities.invokeAndWait(launcher);
		}
		catch(InterruptedException e)
		{
			EAM.unexpectedErrorDialog(e);
		}
		catch(InvocationTargetException e)
		{
			EAM.unexpectedErrorDialog(e);
		}
		return launcher.confirmed();
	}
	
	public static class ThreadedConfirmDialogLauncher implements Runnable
	{
		public ThreadedConfirmDialogLauncher(MainWindow mainWindowToUse, ConfirmDialogTemplate templateToUse)
		{
			mainWindow = mainWindowToUse;
			template = templateToUse;
		}
		
		public void run()
		{
			result = confirm(mainWindow, template);
		}

		public boolean confirmed()
		{
			return result;
		}
		
		private MainWindow mainWindow;
		private ConfirmDialogTemplate template;
		private boolean result;
	}
	
	public static class ConfirmDialogTemplate
	{
		public ConfirmDialogTemplate(String titleToUse, String confirmationTextToUse, String yesButtonTextToUse)
		{
			title = titleToUse;
			confirmationText = confirmationTextToUse;
			yesText = yesButtonTextToUse;
			
			noText = CANCEL_TEXT;
		}
		
		public String getConfirmationText()
		{
			return confirmationText;
		}

		public String getTitle()
		{
			return title;
		}

		public String[] getButtonLabels()
		{
			return new String[] {yesText, noText};
		}

		private final String CANCEL_TEXT = EAM.text("Button|Cancel");
		
		private String title;
		private String confirmationText;
		private String yesText;
		private String noText;
	}
	
	private int pressedButtonIndex;
}
