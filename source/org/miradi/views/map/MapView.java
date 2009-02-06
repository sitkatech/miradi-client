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
package org.miradi.views.map;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.miradi.main.MainWindow;
import org.miradi.main.ResourcesHandler;
import org.miradi.project.Project;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.TabbedView;
import org.miradi.wizard.WizardPanel;

public class MapView extends TabbedView
{
	public MapView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.MAP_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new MapToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		String[] demoMaps = 
		{
			"base",
			"scope",
			"targets",
			"threats",
		};
		
		for(int i = 0; i < demoMaps.length; ++i)
		{
			URL imageURL = ResourcesHandler.getResourceURL("demo/map-" + demoMaps[i] + ".jpg");
			JLabel image = new JLabel(new ImageIcon(imageURL));
			image.setName(demoMaps[i]);
			addTab(image.getName(), new MiradiScrollPane(image));
		}
	}

	public void deleteTabs() throws Exception
	{
		// lightweight tabs...nothing to dispose yet
	}

	public WizardPanel createWizardPanel() throws Exception
	{
		// TODO Auto-generated method stub
		return null;
	}

}
