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

package org.miradi.dialogs.dashboard;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import org.miradi.main.MainWindow;
import org.miradi.utils.FlexibleWidthHtmlViewer;
import org.miradi.utils.Translation;

public class DashboardRightSideDescriptionPanel extends JPanel
{
	public DashboardRightSideDescriptionPanel(MainWindow mainWindow) throws Exception
	{
		setLayout(new BorderLayout());
		viewer = new FlexibleWidthHtmlViewer(mainWindow);
		add(new JScrollPane(viewer));
	}

	public void setRightSidePanelContent(String resourceFileName) throws Exception
	{
		String htmlText = Translation.getHtmlContent(resourceFileName);
		viewer.setText(htmlText);
	}
	
	private FlexibleWidthHtmlViewer viewer;
}
