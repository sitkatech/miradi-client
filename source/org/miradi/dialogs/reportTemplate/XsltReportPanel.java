/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.reportTemplate;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;

public class XsltReportPanel extends TwoColumnPanel
{
	public XsltReportPanel()
	{
		setBackground(AppPreferences.getDataPanelBackgroundColor());
		addRunButton();
	}
	
	private void addRunButton()
	{
		PanelButton runButton = new PanelButton(EAM.text("Run"));
		runButton.addActionListener(new ActionHandler());
		add(runButton);
	}

	private class ActionHandler implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
		}
	}
}
