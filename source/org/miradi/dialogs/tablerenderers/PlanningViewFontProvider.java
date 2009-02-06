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
package org.miradi.dialogs.tablerenderers;

import java.awt.Font;

import org.miradi.main.MainWindow;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;

public class PlanningViewFontProvider extends FontForObjectTypeProvider
{
	public PlanningViewFontProvider(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	public Font getFont(int objectType)
	{
		Font font = super.getFont(objectType);
		if(shouldBeBold(objectType))
			return font.deriveFont(Font.BOLD);
		
		return font;
	}

	private boolean shouldBeBold(int objectType)
	{
		if(objectType == Goal.getObjectType())
			return true;
		if(objectType == Objective.getObjectType())
			return true;
		if(objectType == Strategy.getObjectType())
			return true;
		if(objectType == Indicator.getObjectType())
			return true;
		return false;
	}
}
