/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;

public class TargetViabilityTreeModel extends GenericViabilityTreeModel
{
	public TargetViabilityTreeModel(Object root)
	{
		super(root);
	}

	public String[] getColumnTags()
	{
		return columnTags;
	}
	
	public static String[] columnTags = {DEFAULT_COLUMN, 
										 Indicator.TAG_STATUS, 
										 KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
										 KeyEcologicalAttributeMeasurementNode.POOR,
										 KeyEcologicalAttributeMeasurementNode.FAIR,
										 KeyEcologicalAttributeMeasurementNode.GOOD,
										 KeyEcologicalAttributeMeasurementNode.VERY_GOOD,
										 Measurement.TAG_STATUS_CONFIDENCE,};
}
