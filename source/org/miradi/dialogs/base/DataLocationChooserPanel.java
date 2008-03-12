/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.prefs.Preferences;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.OneColumnPanel;
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
		PanelButton dataChooserButton = new PanelButton(EAM.text("Select Location"));
		dataChooserButton.addActionListener(new ActionHandler());
		
		add(FlexibleWidthHtmlViewer.createHtmlViewer(getMainWindow(), "DataLocationInstructions.html"), BorderLayout.CENTER);
		OneColumnPanel buttonPanel = new OneColumnPanel();
		buttonPanel.add(dataChooserButton);
		buttonPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		add(buttonPanel, BorderLayout.SOUTH);
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
				
				Preferences.userNodeForPackage(Miradi.class).put(EAM.MIRAID_DATA_DIRECTORY_KEY, chosenDir.getAbsolutePath());
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
}
