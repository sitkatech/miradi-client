/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 

package org.conservationmeasures.eam.wizard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.utils.MiradiResourceImageIcon;
import org.martus.swing.UiLabel;
import org.martus.util.xml.XmlUtilities;

import com.jhlabs.awt.BasicGridLayout;

public class WizardTitlePanel extends JPanel
{
	public WizardTitlePanel(MainWindow mainWindow)
	{
		super(new BorderLayout());
		
		stepTitle = new WizardTitleHtmlViewer(mainWindow);
		stepTitle.setForeground(AppPreferences.WIZARD_TITLE_FOREGROUND);
		stepTitle.setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
		screenTitle = new WizardTitleHtmlViewer(mainWindow);
		screenTitle.setForeground(Color.BLACK);
		screenTitle.setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
		
		JPanel titlePanel = new JPanel(new BasicGridLayout(2,1));
		titlePanel.add(stepTitle);
		titlePanel.add(screenTitle);
		titlePanel.setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
		
		ImageIcon icon = new MiradiResourceImageIcon("images/MiradiLogo.png");
		UiLabel iconHolder = new UiLabel();
		iconHolder.setIcon(icon);
		add(iconHolder, BorderLayout.BEFORE_LINE_BEGINS);
		add(titlePanel, BorderLayout.CENTER);
		
		setBackground(AppPreferences.WIZARD_TITLE_BACKGROUND);
	}
	
	public void setStepTitle(String text)
	{
		String title = EAM.text("Intro to View");
		if(text.length() > 0)
			title = "Step " + XmlUtilities.getXmlEncoded(text);
		stepTitle.setText("<div class='processsteptitle'>" + title + "</div>");
	}
	
	public void setScreenTitle(String text)
	{
		screenTitle.setText("<div class='wizardscreentitle'>" + XmlUtilities.getXmlEncoded(text) + "</div>");
	}
	
	WizardTitleHtmlViewer stepTitle;
	WizardTitleHtmlViewer screenTitle;
}
