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

package org.miradi.wizard;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import org.martus.swing.UiLabel;
import org.martus.util.xml.XmlUtilities;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.utils.MiradiResourceImageIcon;

import com.jhlabs.awt.BasicGridLayout;

public class WizardTitlePanel extends JPanel
{
	public WizardTitlePanel(MainWindow mainWindow)
	{
		super(new BorderLayout());
		
		stepTitle = new WizardTitleHtmlViewer(mainWindow);
		stepTitle.setForeground(AppPreferences.WIZARD_TITLE_FOREGROUND);
		stepTitle.setBackground(AppPreferences.getWizardTitleBackground());
		screenTitle = new WizardTitleHtmlViewer(mainWindow);
		screenTitle.setForeground(Color.BLACK);
		screenTitle.setBackground(AppPreferences.getWizardTitleBackground());
		
		JPanel titlePanel = new JPanel(new BasicGridLayout(2,1));
		titlePanel.add(stepTitle);
		titlePanel.add(screenTitle);
		titlePanel.setBackground(AppPreferences.getWizardTitleBackground());
		
		ImageIcon icon = new MiradiResourceImageIcon("images/MiradiLogo48.png");
		UiLabel iconHolder = new UiLabel();
		iconHolder.setBackground(AppPreferences.getWizardTitleBackground());
		iconHolder.setOpaque(true);
		iconHolder.setIcon(icon);
		add(iconHolder, BorderLayout.BEFORE_LINE_BEGINS);
		add(titlePanel, BorderLayout.CENTER);
		
		setBackground(AppPreferences.getWizardTitleBackground());
	}
	
	public void setStepTitle(String text)
	{
		String title = EAM.text("Intro to View");
		if(text.length() > 0)
			title = EAM.text("Step ") + XmlUtilities.getXmlEncoded(text);
		stepTitle.setText("<div class='processsteptitle'>" + title + "</div>");
	}
	
	public void setScreenTitle(String text)
	{
		screenTitle.setText("<div class='wizardscreentitle'>" + XmlUtilities.getXmlEncoded(text) + "</div>");
	}
	
	WizardTitleHtmlViewer stepTitle;
	WizardTitleHtmlViewer screenTitle;
}
