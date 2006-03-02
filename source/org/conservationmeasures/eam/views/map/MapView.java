package org.conservationmeasures.eam.views.map;

import org.conservationmeasures.eam.main.MainWindow;
import org.conservationmeasures.eam.views.umbrella.UmbrellaView;

public class MapView extends UmbrellaView
{
	public MapView(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
		setToolBar(new MapToolBar(mainWindowToUse.getActions()));
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
