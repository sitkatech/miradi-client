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

package org.miradi.dialogs.notify;

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

import org.miradi.dialogs.base.DialogWithDisposablePanelAndMainWindowUpdating;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.FlexibleWidthHtmlViewer;

public class NotifyDialog extends DialogWithDisposablePanelAndMainWindowUpdating
{
	public static void notify(MainWindow mainWindow, NotifyDialogTemplate template)
	{
		showDialog(mainWindow, template);
	}
	
	private NotifyDialog(MainWindow mainWindow, NotifyDialogTemplate template)
	{
		super(mainWindow, new DisposablePanel(new OneColumnGridLayout()));
		
		setModal(true);
		JPanel panel = getWrappedPanel();

		Color backgroundColor = getMainWindow().getAppPreferences().getWizardBackgroundColor();
		panel.setBackground(backgroundColor);
		panel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));

		FlexibleWidthHtmlViewer htmlPanel = new FlexibleWidthHtmlViewer(mainWindow, template.getNotificationText());
		panel.add(htmlPanel);

		panel.add(new FillerLabel());
		Box buttonPanel = Box.createHorizontalBox();
		buttonPanel.setBackground(backgroundColor);
		buttonPanel.add(Box.createHorizontalGlue());
		for(int i = 0; i < template.getButtonLabels().length; ++i)
		{
			String buttonText = template.getButtonLabels()[i];
			JButton button = new JButton(buttonText);
			button.addActionListener(new ButtonListener());
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

	private static void showDialog(MainWindow mainWindow, NotifyDialogTemplate template)
	{
		NotifyDialog dialog = new NotifyDialog(mainWindow, template);
		dialog.showDialog();
	}

	class ButtonListener implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			dispose();
		}
	}

	public static void notifyWithinThread(MainWindow mainWindowToUse, NotifyDialogTemplate templateToUse)
	{
		ThreadedNotifyDialogLauncher launcher = new ThreadedNotifyDialogLauncher(mainWindowToUse, templateToUse);
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
	}
	
	public static class ThreadedNotifyDialogLauncher implements Runnable
	{
		public ThreadedNotifyDialogLauncher(MainWindow mainWindowToUse, NotifyDialogTemplate templateToUse)
		{
			mainWindow = mainWindowToUse;
			template = templateToUse;
		}
		
		public void run()
		{
			NotifyDialog.notify(mainWindow, template);
		}

		private MainWindow mainWindow;
		private NotifyDialogTemplate template;
	}
}
