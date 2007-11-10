/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.treetables.GenericTreeTableModel;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.KeyEcologicalAttribute;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class ViabilityTreeModel extends GenericTreeTableModel
{
	public ViabilityTreeModel(Object root)
	{
		super(root);
		statusQuestion = new StatusQuestion("");

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
		String columnTag = getColumnTag(column);
		if(isValueColumn(columnTag))
			return getValueColumnChoice(columnTag).getLabel();
		
		return EAM.fieldLabel(getObjectTypeForColumnLabel(columnTag), columnTag);
	}
	
	private int getObjectTypeForColumnLabel(String tag)
	{
		if(tag.equals(Target.TAG_VIABILITY_MODE))
			return Target.getObjectType();
		else if(tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return Measurement.getObjectType();
		return KeyEcologicalAttribute.getObjectType();
	}
	
	boolean isValueColumn(String columnTag)
	{
		ChoiceItem choiceItem = statusQuestion.findChoiceByCode(columnTag);
		return (choiceItem != null);
	}

	private ChoiceItem getValueColumnChoice(String columnTag)
	{
		return statusQuestion.findChoiceByCode(columnTag);	
	}

	public static String[] columnTags = {DEFAULT_COLUMN, 
										 Target.TAG_VIABILITY_MODE,
										 Indicator.TAG_STATUS, 
										 KeyEcologicalAttribute.TAG_KEY_ECOLOGICAL_ATTRIBUTE_TYPE,
										 KeyEcologicalAttributeMeasurementNode.POOR,
										 KeyEcologicalAttributeMeasurementNode.FAIR,
										 KeyEcologicalAttributeMeasurementNode.GOOD,
										 KeyEcologicalAttributeMeasurementNode.VERY_GOOD,
										 Measurement.TAG_STATUS_CONFIDENCE,};

	private StatusQuestion statusQuestion;
}
