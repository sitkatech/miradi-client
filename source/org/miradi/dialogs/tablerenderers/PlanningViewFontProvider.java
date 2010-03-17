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
import org.miradi.objects.BaseObject;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Objective;
import org.miradi.objects.Strategy;
import org.miradi.objects.Task;

public class PlanningViewFontProvider extends FontForObjectProvider
{
	public PlanningViewFontProvider(MainWindow mainWindowToUse)
	{
		super(mainWindowToUse);
	}

	@Override
	public Font getFont(BaseObject baseObject)
	{
		Font font = super.getFont(baseObject);
		if(shouldBeBold(baseObject.getType()))
			font = font.deriveFont(Font.BOLD);
	
		if(Task.is(baseObject))
			font = deriveTaskFont((Task) baseObject);

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
	
	private Font deriveTaskFont(Task task)
	{
		final int SINGLE_PROPORTION = 1;
		return deriveTaskFont(task, SINGLE_PROPORTION);
	}
	
	public Font deriveTaskFont(Task task, int proportionShares)
	{
		Font font = super.getFont(task);
		if (task.isPartOfASharedTaskTree() && proportionShares < task.getTotalShareCount())
			font = font.deriveFont(Font.ITALIC);

		if (task.isMethod())
			font = font.deriveFont(Font.BOLD + font.getStyle());
		
		return font;
	}
}
