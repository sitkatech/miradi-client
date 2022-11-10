/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
package org.miradi.dialogs.fieldComponents;

import org.martus.swing.UiTabbedPane;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.main.Miradi;

import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class PanelTabbedPane extends UiTabbedPane
{
	public PanelTabbedPane()
	{
		super();
		setFont(getMainWindow().getUserDataPanelFont());

		// work-around to address JDK-8251377 & JDK-8269984
		if (Miradi.isMacos() && getMainWindow().isDefaultSystemLookAndFeel())
			this.addChangeListener(new TabChangeListener());
	}

	class TabChangeListener implements ChangeListener
	{
		public void stateChanged(ChangeEvent event)
		{
			PanelTabbedPane tabbedPane = (PanelTabbedPane) event.getSource();

			int selectedTabIndex = tabbedPane.getSelectedIndex();

			for(int t = 0; t < tabbedPane.getTabCount(); ++t)
				tabbedPane.setForegroundAt(t, Color.BLACK);

			if (selectedTabIndex != -1)
				tabbedPane.setForegroundAt(selectedTabIndex, Color.DARK_GRAY);
		}
	}

	public MainWindow getMainWindow()
	{
		return EAM.getMainWindow();
	}
}
