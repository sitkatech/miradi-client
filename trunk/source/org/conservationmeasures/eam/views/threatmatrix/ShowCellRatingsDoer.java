/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.threatmatrix;

import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.AppPreferences;

public class ShowCellRatingsDoer extends CellRatingsDoer
{
	public boolean isAvailable()
	{
		if (!isCellRatingVisible())
			return true;
		
		return false;
	}
	
	public void doIt() throws CommandFailedException
	{
		if (!isAvailable())
			return;
		
		getMainWindow().setBooleanPreference(AppPreferences.TAG_CELL_RATINGS_VISIBLE, true);
		updateToolBar();
	}
}
