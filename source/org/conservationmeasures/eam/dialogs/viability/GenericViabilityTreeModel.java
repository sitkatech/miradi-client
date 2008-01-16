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

abstract public class GenericViabilityTreeModel extends GenericTreeTableModel
{
	public GenericViabilityTreeModel(Object root)
	{
		super(root);
		statusQuestion = new StatusQuestion("");
	}

	public int getColumnCount()
	{
		return getColumnTags().length;
	}

	public String getColumnTag(int column)
	{
		return getColumnTags()[column];
	}

	public String getColumnName(int column)
	{
		String columnTag = getColumnTag(column);
		if(isChoiceItemColumn(columnTag))
			return getColumnChoiceItem(columnTag).getLabel();
		
		return EAM.fieldLabel(getObjectTypeForColumnLabel(columnTag), columnTag);
	}
	
	private int getObjectTypeForColumnLabel(String tag)
	{
		if(tag.equals(Target.TAG_VIABILITY_MODE))
			return Target.getObjectType();
			
		else if(tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			return Measurement.getObjectType();
			
		else if (tag.equals(Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return Indicator.getObjectType();
		
		return KeyEcologicalAttribute.getObjectType();
	}
	
	boolean isChoiceItemColumn(String columnTag)
	{
		ChoiceItem choiceItem = statusQuestion.findChoiceByCode(columnTag);
		return (choiceItem != null);
	}

	private ChoiceItem getColumnChoiceItem(String columnTag)
	{
		return statusQuestion.findChoiceByCode(columnTag);	
	}
	
	abstract public String[] getColumnTags();

	private StatusQuestion statusQuestion;
}
