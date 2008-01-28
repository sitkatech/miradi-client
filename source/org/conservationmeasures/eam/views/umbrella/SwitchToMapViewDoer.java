/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.umbrella;

import org.conservationmeasures.eam.views.map.MapView;

public class SwitchToMapViewDoer extends ViewSwitchDoer 
{
	protected String getViewName()
	{
		return MapView.getViewName();
	}
}
