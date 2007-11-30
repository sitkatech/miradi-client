/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.tablerenderers;

import java.awt.Color;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.questions.ChoiceItem;

public class ChoiceItemTableCellRendererWithGrayCells extends ChoiceItemTableCellRenderer
{
	public ChoiceItemTableCellRendererWithGrayCells(RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(providerToUse, fontProviderToUse);
	}

	protected ChoiceItem getChoiceItem(Object value)
	{
		if(value == null)
			return new ChoiceItem("NOT LINKED", "", Color.GRAY.brighter());
		
		if(value instanceof String)
		{
			EAM.logError("Expected ChoiceItem, not: " + value);
			return emptyChoiceItem;
		}
		return (ChoiceItem)value;
	}	
}
