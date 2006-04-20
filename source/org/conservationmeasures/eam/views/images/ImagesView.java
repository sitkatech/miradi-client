/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.images;

import java.awt.BorderLayout;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;

public class ImagesView extends UmbrellaView
{
	public ImagesView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new ImagesToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		add(new UiScrollPane(new ImagesComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Images";
	}

	public void becomeActive() throws Exception
	{
	}

	public void becomeInactive() throws Exception
	{
	}

}

	class ImagesComponent extends JTabbedPane
	{

		public ImagesComponent()
		{
			setTabPlacement(JTabbedPane.BOTTOM);
		
			String[] demoMaps = 
			{
				"Bay",
			};
			
			for(int i = 0; i < demoMaps.length; ++i)
			{
				String mapName = demoMaps[i] + ".jpg";
				JLabel image = new JLabel(new ImageIcon("images/" + mapName));
				image.setName(demoMaps[i]);
				add(image);
			}
		}
	
	}

