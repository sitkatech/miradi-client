/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.dialogs.treetables.GenericTreeTableModel;
import org.miradi.main.EAM;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.Target;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StatusQuestion;

abstract public class GenericViabilityTreeModel extends GenericTreeTableModel
{
	public GenericViabilityTreeModel(Object root)
	{
		super(root);
		statusQuestion = new StatusQuestion();
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
			
		else if (tag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return Indicator.getObjectType();
		
		else if(tag.equals(Measurement.TAG_SUMMARY))
			return Measurement.getObjectType();
		
		return KeyEcologicalAttribute.getObjectType();
	}
	
	public boolean isChoiceItemColumn(String columnTag)
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
