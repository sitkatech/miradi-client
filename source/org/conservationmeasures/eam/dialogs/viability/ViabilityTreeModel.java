/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.views.GenericTreeTableModel;

public class ViabilityTreeModel extends GenericTreeTableModel
{
	public ViabilityTreeModel(Object root)
	{
		super(root);
	}

	public int getColumnCount()
	{
		return columnTags.length;
	}

	public String getColumnTag(int column)
	{
		return columnTags[column];
	}

	public String getColumnName(int column)
	{
		String tag = getColumnTag(column);
		return EAM.fieldLabel(getObjectTypeForColumnLabel(tag), tag);
	}
	
	private int getObjectTypeForColumnLabel(String tag)
	{
		return KeyEcologicalAttribute.getObjectType();
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
