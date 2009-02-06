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
package org.miradi.wizard.noproject;

import java.awt.BorderLayout;

import javax.swing.JPanel;

import org.martus.swing.HyperlinkHandler;
import org.miradi.main.AppPreferences;
import org.miradi.main.MainWindow;
import org.miradi.wizard.WizardHtmlViewer;

public class LeftSideTextPanel extends JPanel
{
	public LeftSideTextPanel(MainWindow mainWindow, String htmlToUse, HyperlinkHandler wizardToUse)
	{
		super(new BorderLayout());
		String html = htmlToUse;
		setBackground(AppPreferences.getWizardBackgroundColor());
		viewer = new WizardHtmlViewer(mainWindow, wizardToUse);
		viewer.setText(html);

		add(viewer, BorderLayout.CENTER);
	}
	
	public void refresh() throws Exception
	{
		viewer.setText(viewer.getText());
	}
	
	WizardHtmlViewer viewer;
}
