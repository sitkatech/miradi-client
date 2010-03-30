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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.JProgressBar;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.icons.IconManager;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.ProgressInterface;
import org.miradi.wizard.MiradiHtmlViewer;

public class ProgressDialog extends DialogWithDisposablePanel implements ProgressInterface
{
	public ProgressDialog(MainWindow mainWindow, String title)
	{
		super(mainWindow);
		setTitle(title);
		setModal(true);

		progressPanel = new ProgressPanel(mainWindow);
		setStatusMessage(EAM.text("Initializing..."), 1);

		cancelButton = new PanelButton(new CancelAction());
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(progressPanel, BorderLayout.BEFORE_FIRST_LINE);
		getContentPane().add(cancelButton, BorderLayout.AFTER_LAST_LINE);
	}
	
	public void setStatusMessage(String translatedMessage, int stepCount)
	{
		progressPanel.setStatusMessage(translatedMessage, stepCount);
		pack();
		repaint();
	}
	
	public void finished()
	{
		progressPanel.finished();
		setVisible(false);
		dispose();
	}
	
	public boolean shouldExit()
	{
		return progressPanel.shouldExit();
	}
	
	public void updateProgressMeter(int currentValue)
	{
		progressPanel.updateProgressMeter(currentValue);
	}

	public void incrementProgress()
	{
		progressPanel.incrementProgress();
	}

	class CancelAction extends AbstractAction
	{
		public CancelAction()
		{
			super(EAM.text("Stop"), IconManager.getCancelIcon());
		}
		
		public void actionPerformed(ActionEvent arg0)
		{
			progressPanel.requestExit();
			cancelButton.setEnabled(false);
		}
	
	}

	class ProgressPanel extends OneColumnPanel implements ProgressInterface
	{
		public ProgressPanel(MainWindow mainWindow)
		{
			message = new FlexibleWidthHtmlViewer(mainWindow);
			progressBar = new JProgressBar();
			
			add(message);
			add(progressBar);
		}
		
		public void requestExit()
		{
			shouldExit = true;
		}
		
		public void setStatusMessage(String translatedMessage, int stepCount)
		{
			message.setText(translatedMessage);
			progressBar.setMaximum(stepCount);
			progressBar.setValue(0);
		}
		
		public void finished()
		{
			progressBar.setValue(progressBar.getMaximum());
			shouldExit();
		}

		public boolean shouldExit()
		{
			return shouldExit;
		}

		public void updateProgressMeter(int currentValue)
		{
			progressBar.setValue(currentValue);
			progressBar.repaint();
		}

		public void incrementProgress()
		{
			updateProgressMeter(progressBar.getValue() + 1);
		}

		private boolean shouldExit;
		private MiradiHtmlViewer message;
		private JProgressBar progressBar;
	}
	
	private ProgressPanel progressPanel;
	private PanelButton cancelButton;
}
