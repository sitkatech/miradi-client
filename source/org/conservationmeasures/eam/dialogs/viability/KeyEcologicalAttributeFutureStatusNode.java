/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Goal;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class KeyEcologicalAttributeFutureStatusNode extends TreeTableNode
{
	public KeyEcologicalAttributeFutureStatusNode(TreeTableNode parentNodeToUse) throws Exception
	{
		parentNode = parentNodeToUse;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return null;
	}

	public ORef getObjectReference()
	{
		return new ORef(Goal.getObjectType(), BaseId.INVALID);
	}
	
	public int getType()
	{
		return Goal.getObjectType();
	}

	public String toString()
	{
		return parentNode.getObject().getData(Indicator.TAG_FUTURE_STATUS_DATE);
	}

	public int getChildCount()
	{
		return 0;
	}

	public TreeTableNode getChild(int index)
	{
		return null;
	}
	
	public TreeTableNode getParentNode()
	{
		return parentNode;
	}

	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		String summaryData = parentNode.getObject().getData(Indicator.TAG_FUTURE_STATUS_SUMMARY);
		String statusData = parentNode.getObject().getData(Indicator.TAG_FUTURE_STATUS_RATING);
		if (tag.equals(StatusQuestion.POOR) && StatusQuestion.POOR.equals(statusData))
			return summaryData;

		if (tag.equals(StatusQuestion.FAIR) && StatusQuestion.FAIR.equals(statusData))
			return summaryData;

		if (tag.equals(StatusQuestion.GOOD) && StatusQuestion.GOOD.equals(statusData))
			return summaryData;

		if (tag.equals(StatusQuestion.VERY_GOOD) && StatusQuestion.VERY_GOOD.equals(statusData))
			return summaryData;
		
		return null;
	}

	public void rebuild() throws Exception
	{
	}
	
	public static final String[] COLUMN_TAGS = {
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		Goal.TAG_EMPTY,
		
		StatusQuestion.POOR,
		StatusQuestion.FAIR,
		StatusQuestion.GOOD,
	    StatusQuestion.VERY_GOOD,

		Goal.TAG_EMPTY,
	};
	
	private TreeTableNode parentNode;
}
