/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Collections;
import java.util.Vector;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class ViabilityIndicatorNode extends TreeTableNode
{
	public ViabilityIndicatorNode(Project projectToUse, TreeTableNode parent, Indicator indicatorToUse) throws Exception
	{
		project = projectToUse;
		indicator = indicatorToUse;
		keyEcologicalAttributesNode = parent;
		rebuild();
	}
	
	public BaseObject getObject()
	{
		return indicator;
	}

	public ORef getObjectReference()
	{
		return indicator.getRef();
	}
	
	public int getType()
	{
		return indicator.getType();
	}

	public String toString()
	{
		return indicator.toString();
	}

	public int getChildCount()
	{
		return measurements.length;
	}

	public TreeTableNode getChild(int index)
	{
		return measurements[index];
	}
	
	public TreeTableNode getParentNode()
	{
		return keyEcologicalAttributesNode;
	}

	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		if (tag.equals(Indicator.PSEUDO_TAG_STATUS_VALUE))
			return new StatusQuestion(tag).findChoiceByCode(getObject().getPseudoData(tag));
		
		if(tag.equals(Indicator.TAG_EMPTY))
			return null;
		
		String data = getObject().getData(tag);
		if (!tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
			return data;

		int threasholdColumn = (column + 1) - getFirstIndexOfThreshold();
		return indicator.getThreshold().get(Integer.toString(threasholdColumn));
		
	}
	
	private int getFirstIndexOfThreshold()
	{
		for (int i = 0; i < COLUMN_TAGS.length; ++i)
		{
			if (COLUMN_TAGS[i].equals(Indicator.TAG_INDICATOR_THRESHOLD))
				return i;
		}
		
		throw new RuntimeException("Could not find Threshold in array.");
	}

	public void rebuild() throws Exception
	{
		ORefList measurementRefs = indicator.getMeasurementRefs();
		Vector measurementAndFutureStatusObjects = new Vector();
		for (int i = 0; i < measurementRefs.size(); ++i)
		{
			Measurement measurement = (Measurement) project.findObject(measurementRefs.get(i));
			measurementAndFutureStatusObjects.add(new KeyEcologicalAttributeMeasurementNode(this, measurement));
		}

		Collections.sort(measurementAndFutureStatusObjects);		
		measurementAndFutureStatusObjects.add(new KeyEcologicalAttributeFutureStatusNode(this));
		measurements = (TreeTableNode[]) measurementAndFutureStatusObjects.toArray(new TreeTableNode[0]);
	}
	
	public static final String[] COLUMN_TAGS = {
		Indicator.TAG_LABEL,
		Indicator.TAG_EMPTY,
		Indicator.PSEUDO_TAG_STATUS_VALUE, 
		Indicator.TAG_EMPTY,
		
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		
		Indicator.TAG_EMPTY,
	};
	
	private Project project;
	private Indicator indicator;
	private TreeTableNode keyEcologicalAttributesNode;
	private TreeTableNode[] measurements;
}