package org.conservationmeasures.eam.views.map;

import javax.swing.JToolBar;

import org.conservationmeasures.eam.actions.ActionViewMap;
import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.main.ViewSwitcher;

public class MapToolBar extends JToolBar
{
	public MapToolBar(Actions actions)
	{
		setFloatable(false);

		add(ViewSwitcher.create(actions, ActionViewMap.class));
	}

}
