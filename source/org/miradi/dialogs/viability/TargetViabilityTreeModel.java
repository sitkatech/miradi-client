/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;

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
	
	public Object getValueAt(Object rawNode, int column)
	{
		return super.getValueAt(rawNode, getAdjustedColumn(column));
	}

	//FIXME this is to ajust for the missing column in each node.  
	private int getAdjustedColumn(int column)
	{
		if (column >= 1)
			return column + 1;
		
		return column;
	}
	
	public static String[] columnTags = {DEFAULT_COLUMN, 
										 Indicator.TAG_STATUS,
										 Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
										 KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
										 KeyEcologicalAttributeMeasurementNode.POOR,
										 KeyEcologicalAttributeMeasurementNode.FAIR,
										 KeyEcologicalAttributeMeasurementNode.GOOD,
										 KeyEcologicalAttributeMeasurementNode.VERY_GOOD,
										 Measurement.TAG_STATUS_CONFIDENCE,};
}
