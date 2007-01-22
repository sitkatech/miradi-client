/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
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
