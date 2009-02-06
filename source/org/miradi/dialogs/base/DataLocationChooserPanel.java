/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTextField;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.Miradi;
import org.miradi.utils.FlexibleWidthHtmlViewer;

import com.jhlabs.awt.BasicGridLayout;

public class DataLocationChooserPanel extends MiradiPanel
{
	public DataLocationChooserPanel(MainWindow mainWindowToUse)
	{
		super(new BasicGridLayout(2, 1));

		mainWindow = mainWindowToUse;
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		
		createContents();
	}

	private void createContents()
	{
		PanelButton dataChooserButton = new PanelButton(EAM.text("Select Location..."));
		dataChooserButton.addActionListener(new ActionHandler());
		add(FlexibleWidthHtmlViewer.createFromResourceFile(getMainWindow(), "DataLocationInstructions.html"), BorderLayout.CENTER);
		TwoColumnPanel buttonPanel = new TwoColumnPanel();
		buttonPanel.add(createReadonlyDirectoryTextField());
		buttonPanel.add(dataChooserButton);
		buttonPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		add(buttonPanel, BorderLayout.SOUTH);
	}

	private PanelTextField createReadonlyDirectoryTextField()
	{
		directoryReadonlyTextField = new PanelTextField(EAM.getHomeDirectory().getAbsolutePath());
		directoryReadonlyTextField.setEditable(false);
		directoryReadonlyTextField.setForeground(EAM.READONLY_FOREGROUND_COLOR);
		directoryReadonlyTextField.setBackground(EAM.READONLY_BACKGROUND_COLOR);
		
		//NOTE:  on vista left edge of 'c'  gets cut off without padding.  
		Dimension preferredSize = directoryReadonlyTextField.getPreferredSize();
		preferredSize.width += PADDING;
		directoryReadonlyTextField.setPreferredSize(preferredSize);
		directoryReadonlyTextField.setBorder(ObjectDataInputField.createLineBorderWithMargin());
		
		return directoryReadonlyTextField;
	}
	
	public class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent event)
		{
			try
			{
				MiradiDirectoryChooser dirChooser = new MiradiDirectoryChooser(getMainWindow());
				File chosenDir = dirChooser.displayChooser();
				if (chosenDir == null) 
					return;
				
				if (!chosenDir.exists())
					return;
				
				Preferences.userNodeForPackage(Miradi.class).put(EAM.MIRADI_DATA_DIRECTORY_KEY, chosenDir.getAbsolutePath());
				directoryReadonlyTextField.setText(EAM.getHomeDirectory().getAbsolutePath());
				mainWindow.refreshWizard();
			}
			catch(Exception e)
			{
				EAM.logException(e);
				EAM.errorDialog(EAM.text("Error occured while saving data location"));
			}	
		}	
	}
			
	public MainWindow getMainWindow()
	{
		return mainWindow;
	}
	
	private MainWindow mainWindow;
	private PanelTextField directoryReadonlyTextField;
	private static final int PADDING = 10;
}
