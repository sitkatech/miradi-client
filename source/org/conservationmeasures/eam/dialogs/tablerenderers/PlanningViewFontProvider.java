/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Font;

import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Objective;
import org.conservationmeasures.eam.objects.Strategy;

public class PlanningViewFontProvider extends FontForObjectTypeProvider
{
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
