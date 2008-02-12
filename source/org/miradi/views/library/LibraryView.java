/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.library;

import javax.swing.JLabel;
import javax.swing.JToolBar;

import org.miradi.main.MainWindow;
import org.miradi.project.Project;
import org.miradi.utils.MiradiResourceImageIcon;
import org.miradi.utils.MiradiScrollPane;
import org.miradi.views.TabbedView;
import org.miradi.wizard.WizardPanel;

public class LibraryView extends TabbedView
{
	public LibraryView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return Project.LIBRARY_VIEW_NAME;
	}

	public JToolBar createToolBar()
	{
		return new LibraryToolBar(getActions());
	}

	public void createTabs() throws Exception
	{
		String[] demoMaps = 
		{
			"Bay",
		};
		
		for(int i = 0; i < demoMaps.length; ++i)
		{
			String mapName = demoMaps[i] + ".jpg";
			JLabel image = new JLabel(new MiradiResourceImageIcon("images/" + mapName));
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
