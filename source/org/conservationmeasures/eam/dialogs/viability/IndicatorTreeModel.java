/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.objects.Measurement;

public class IndicatorTreeModel extends GenericViabilityTreeModel
{
	public IndicatorTreeModel(Object root)
	{
		super(root);
	}

	public String[] getColumnTags()
	{
		return columnTags;
	}

	public static String[] columnTags = {DEFAULT_COLUMN, 
		 Measurement.TAG_SUMMARY,
		 Measurement.TAG_STATUS_CONFIDENCE,};
}
