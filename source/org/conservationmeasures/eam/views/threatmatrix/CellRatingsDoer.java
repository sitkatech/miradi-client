/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.main.AppPreferences;
import org.conservationmeasures.eam.views.ViewDoer;

abstract public class CellRatingsDoer extends ViewDoer
{
	public boolean isCellRatingVisible()
	{
		return getMainWindow().getBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE);
	}
	
	public void updateToolBar()
	{
		getMainWindow().updateToolBar();
	}

}
