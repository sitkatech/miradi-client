/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import java.awt.Color;

import javax.swing.JPanel;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.MainWindow;
import org.martus.util.xml.XmlUtilities;

import com.jhlabs.awt.BasicGridLayout;

public class WizardTitlePanel extends JPanel
{
	public WizardTitlePanel(MainWindow mainWindow)
	{
		super(new BasicGridLayout(2, 1));
		
		stepTitle = new WizardTitleHtmlViewer(mainWindow);
		stepTitle.setForeground(AppPreferences.WIZARD_TITLE_FOREGROUND);
		stepTitle.setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
		screenTitle = new WizardTitleHtmlViewer(mainWindow);
		screenTitle.setForeground(Color.BLACK);
		screenTitle.setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
		add(stepTitle);
		add(screenTitle);
		
		setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
	}
	
	public void setStepTitle(String text)
	{
		stepTitle.setText("<div class='processsteptitle'>" + XmlUtilities.getXmlEncoded(text) + "</div>");
	}
	
	public void setScreenTitle(String text)
	{
		screenTitle.setText("<div class='wizardscreentitle'>" + XmlUtilities.getXmlEncoded(text) + "</div>");
	}
	
	WizardTitleHtmlViewer stepTitle;
	WizardTitleHtmlViewer screenTitle;
}
