/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.map;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.MiradiScrollPane;
import org.conservationmeasures.eam.views.TabbedView;
import org.conservationmeasures.eam.wizard.WizardPanel;

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
			URL imageURL = MapView.class.getResource(demoMaps[i] + ".jpg");
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
