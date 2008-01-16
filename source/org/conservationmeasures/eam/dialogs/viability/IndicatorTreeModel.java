/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;

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
