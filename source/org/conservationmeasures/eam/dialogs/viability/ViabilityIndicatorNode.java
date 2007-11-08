/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import java.util.Vector;

import org.conservationmeasures.eam.dialogs.treetables.TreeTableNode;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.StatusQuestion;

public class ViabilityIndicatorNode extends ViabilityTreeTableNode
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
		String data = getObject().getData(tag);
		if (tag.equals(Measurement.TAG_STATUS))
			return new StatusQuestion(tag).findChoiceByCode(data);
		
		if (!tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
			return data;
		
		int threasholdColumn = (column + 1) - getFirstIndexOfThreshold();
		return indicator.getThreshold().get(Integer.toString(threasholdColumn));
	}
	
	public int getFirstIndexOfThreshold()
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
		Vector measurementsAndFutureStartus = new Vector();
		for (int i = 0; i < measurementRefs.size(); ++i)
		{
			Measurement measurement = (Measurement) project.findObject(measurementRefs.get(i));
			measurementsAndFutureStartus.add(new KeyEcologicalAttributeMeasurementNode(this, measurement));
		}
		
		measurementsAndFutureStartus.add(new KeyEcologicalAttributeFutureStatusNode(this));
		measurements = (TreeTableNode[]) measurementsAndFutureStartus.toArray(new TreeTableNode[0]); 
	}
	
	public static final String[] COLUMN_TAGS = {
		Indicator.TAG_LABEL,
		Indicator.TAG_EMPTY,
		Measurement.TAG_STATUS, 
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