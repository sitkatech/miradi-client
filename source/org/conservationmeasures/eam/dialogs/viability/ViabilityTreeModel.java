/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Target;

public class ViabilityTreeModel extends GenericViabilityTreeModel
{
	public ViabilityTreeModel(Object root)
	{
		super(root);
	}

	public String[] getColumnTags()
	{
		return columnTags;
	}
	
	public static String[] columnTags = {DEFAULT_COLUMN, 
										 Target.TAG_VIABILITY_MODE,
										 Indicator.TAG_STATUS,
										 Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
										 KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
										 KeyEcologicalAttributeMeasurementNode.POOR,
										 KeyEcologicalAttributeMeasurementNode.FAIR,
										 KeyEcologicalAttributeMeasurementNode.GOOD,
										 KeyEcologicalAttributeMeasurementNode.VERY_GOOD,
										 Measurement.TAG_STATUS_CONFIDENCE,};
}
