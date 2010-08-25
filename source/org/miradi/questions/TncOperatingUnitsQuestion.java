/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.questions;

import java.util.Vector;

import org.miradi.objecthelpers.TncOperatingUnitsFileLoader;
import org.miradi.objecthelpers.TwoLevelFileLoader;

public class TncOperatingUnitsQuestion extends TwoLevelQuestion
{
	public TncOperatingUnitsQuestion()
	{
		super(new TncOperatingUnitsFileLoader(TwoLevelFileLoader.TNC_OPERATING_UNITS_FILE));
		sortChoices();
		moveSupersededChoiceToEnd();
	}

	private void moveSupersededChoiceToEnd()
	{
		ChoiceItem superseded = findChoiceByCode(TNC_SUPERSEDED_OU_CODE);
		if(superseded == null)
			throw new RuntimeException("Missing TNC Superseded OU choice");
		
		Vector<ChoiceItem> choices = getRawChoices();
		choices.remove(superseded);
		choices.add(superseded);
	}
	
	public static final String TNC_SUPERSEDED_OU_CODE = "OBSOLETE";
}
