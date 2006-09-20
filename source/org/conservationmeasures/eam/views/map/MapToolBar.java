package org.conservationmeasures.eam.views.map;

import org.conservationmeasures.eam.actions.Actions;
import org.conservationmeasures.eam.actions.views.ActionViewMap;
import org.conservationmeasures.eam.main.EAMToolBar;

public class MapToolBar extends EAMToolBar
{
	public MapToolBar(Actions actions)
	{
		super(actions, ActionViewMap.class);
	}

}
