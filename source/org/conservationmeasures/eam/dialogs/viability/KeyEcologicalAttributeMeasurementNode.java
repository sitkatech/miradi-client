/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.questions.ChoiceItem;
import org.conservationmeasures.eam.questions.StatusConfidenceQuestion;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class KeyEcologicalAttributeMeasurementNode extends TreeTableNode
{
	public KeyEcologicalAttributeMeasurementNode(ViabilityIndicatorNode parent, Measurement measurementToUse)
	{
		measurement = measurementToUse;
		parentNode = parent;
	}
	
	public BaseObject getObject()
	{
		return measurement;
	}

	public ORef getObjectReference()
	{
		return measurement.getRef();
	}
	
	public int getType()
	{
		return measurement.getType();
	}

	public String toString()
	{
		return measurement.toString();
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

	//TODO: this method could be pulled up to the supper
	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		String statusData = getObject().getData(Measurement.TAG_STATUS);
		String summaryData = getObject().getData(Measurement.TAG_SUMMARY);
		if (tag.equals(POOR) && StatusQuestion.POOR.equals(statusData))
			return summaryData;

		if (tag.equals(FAIR) && StatusQuestion.FAIR.equals(statusData))
			return summaryData;

		if (tag.equals(GOOD) && StatusQuestion.GOOD.equals(statusData))
			return summaryData;

		if (tag.equals(VERY_GOOD) && StatusQuestion.VERY_GOOD.equals(statusData))
			return summaryData;
		
		if (tag.equals(Measurement.TAG_STATUS_CONFIDENCE))
		{
			String rawValue = getObject().getData(tag);
			ChoiceItem choiceItem = new StatusConfidenceQuestion().findChoiceByCode(rawValue);
			return choiceItem;
		}
		
		return null;
	}

	public void rebuild() throws Exception
	{
	}
	
	public static final String POOR = StatusQuestion.POOR;
	public static final String FAIR = StatusQuestion.FAIR;
	public static final String GOOD = StatusQuestion.GOOD;
	public static final String VERY_GOOD = StatusQuestion.VERY_GOOD;
	
	public static final String[] COLUMN_TAGS = {Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY, 
												Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY,
												Measurement.TAG_EMPTY,
												POOR,
												FAIR,
												GOOD,
												VERY_GOOD,
												Measurement.TAG_STATUS_CONFIDENCE};
	
	private Measurement measurement;
	private ViabilityIndicatorNode parentNode;	
}
