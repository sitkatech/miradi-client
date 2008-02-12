/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.map;

import org.miradi.actions.Actions;
import org.miradi.actions.views.ActionViewMap;
import org.miradi.main.EAMToolBar;

public class MapToolBar extends EAMToolBar
{
	public MapToolBar(Actions actions)
	{
		super(actions, ActionViewMap.class);
	}

}
