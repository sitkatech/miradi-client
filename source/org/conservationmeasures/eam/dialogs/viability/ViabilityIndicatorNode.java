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
		String data = getObject().getData(tag);
		if (!tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
			return data;
		
		return ((Indicator)getObject()).getThreshold().get(Integer.toString(column - getFirstIndexOfThreshold()));
	}
	
	public int getFirstIndexOfThreshold()
	{
		for (int index = -1; index < COLUMN_TAGS.length - 1; ++index)
		{
			if (COLUMN_TAGS[index + 1].equals(Indicator.TAG_INDICATOR_THRESHOLD))
				return index;
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
		Measurement.TAG_STATUS, 
		Indicator.TAG_EMPTY,
		
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		Indicator.TAG_INDICATOR_THRESHOLD,
		
		Indicator.TAG_RATING_SOURCE
	};
	
	private Project project;
	private Indicator indicator;
	private TreeTableNode keyEcologicalAttributesNode;
	private TreeTableNode[] measurements;
}