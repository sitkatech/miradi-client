package org.conservationmeasures.eam.views.map;

import java.awt.BorderLayout;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;
import org.martus.swing.UiScrollPane;

public class MapView extends UmbrellaView
{
	public MapView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new MapToolBar(mainWindowToUse.getActions()));
		setLayout(new BorderLayout());
		add(new UiScrollPane(new MapComponent()), BorderLayout.CENTER);
	}

	public String cardName() 
	{
		return getViewName();
	}
	
	static public String getViewName()
	{
		return "Map";
	}
}

	class MapComponent extends JTabbedPane
	{

		public MapComponent()
		{
			setTabPlacement(JTabbedPane.BOTTOM);
		
			String[] demoMaps = 
			{
				"bay",
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
				add(image);
			}
		}
	
	}

