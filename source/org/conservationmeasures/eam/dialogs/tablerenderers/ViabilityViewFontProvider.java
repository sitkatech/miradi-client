/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Font;

import org.conservationmeasures.eam.objects.Target;

public class ViabilityViewFontProvider extends FontForObjectTypeProvider
{
	public Font getFont(int objectType)
	{
		Font font = super.getFont(objectType);
		if(objectType == Target.getObjectType())
			return font.deriveFont(Font.BOLD);
		
		return font;
	}

}
