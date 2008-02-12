/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.main.EAM;
import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;

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
			if (Goal.is(node.getType()) && getColumnTag(column) == Measurement.TAG_SUMMARY)
				return node.getParentNode().getObject().getData(Indicator.TAG_FUTURE_STATUS_SUMMARY);

			else if (Measurement.is(node.getType()) && getColumnTag(column) == Measurement.TAG_SUMMARY)
				return node.getObject().getData(Measurement.TAG_SUMMARY);

			return super.getValueAt(rawNode, column);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("ERROR");
		}
	}
	
	public String[] getColumnTags()
	{
		return columnTags;
	}

	public static String[] columnTags = {DEFAULT_COLUMN, 
		 Measurement.TAG_SUMMARY,
		 Measurement.TAG_STATUS_CONFIDENCE,};
}
