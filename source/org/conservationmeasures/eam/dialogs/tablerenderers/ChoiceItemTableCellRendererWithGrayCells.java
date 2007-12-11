/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Color;

public class ChoiceItemTableCellRendererWithGrayCells extends ChoiceItemTableCellRenderer
{
	public ChoiceItemTableCellRendererWithGrayCells(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	protected Color getBackgroundColor(Object value)
	{
		if(value == null)
			return Color.GRAY.brighter();
		return super.getBackgroundColor(value);
	}
	
}
