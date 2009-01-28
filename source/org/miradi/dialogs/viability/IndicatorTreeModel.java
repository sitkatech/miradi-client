/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StatusConfidenceQuestion;

public class IndicatorTreeModel extends GenericViabilityTreeModel
{
	public IndicatorTreeModel(Object root)
	{
		super(root);
	}

	public Object getValueAt(Object rawNode, int column)
	{
		try
		{
			TreeTableNode node = (TreeTableNode) rawNode;
			
			String columnTag = getColumnTag(column);
			if(columnTag.equals(Measurement.TAG_SUMMARY))
			{
				if (Goal.is(node.getType()))
					return node.getParentNode().getObject().getData(Indicator.TAG_FUTURE_STATUS_SUMMARY);

				if (Measurement.is(node.getType()))
					return node.getObject().getData(Measurement.TAG_SUMMARY);
			}
			
			if(columnTag.equals(Measurement.TAG_STATUS_CONFIDENCE))
			{
				if(Indicator.is(node.getType()))
					return "";

				if (Measurement.is(node.getType()))
				{
					String rawValue = node.getObject().getData(columnTag);
					ChoiceItem choiceItem = new StatusConfidenceQuestion().findChoiceByCode(rawValue);
					return choiceItem;
				}
			}

			Object result = super.getValueAt(rawNode, column);
			return result;
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error");
		}
	}
	
	public String[] getColumnTags()
	{
		return columnTags;
	}

	public static String[] columnTags = {DEFAULT_COLUMN, 
		 Measurement.TAG_SUMMARY,
		 Measurement.TAG_STATUS_CONFIDENCE,
	};
}
