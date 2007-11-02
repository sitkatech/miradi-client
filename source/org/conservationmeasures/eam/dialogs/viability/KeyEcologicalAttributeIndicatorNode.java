/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs.viability;

import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Indicator;
import org.conservationmeasures.eam.objects.Measurement;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.TreeTableNode;

public class KeyEcologicalAttributeIndicatorNode extends TreeTableNode
{
	public KeyEcologicalAttributeIndicatorNode(Project projectToUse, TreeTableNode parent, Indicator indicatorToUse) throws Exception
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
		
		final int FIRST_TWO_COLUM_COUNT = 2;
		return ((Indicator)getObject()).getThreshold().get(Integer.toString(column - FIRST_TWO_COLUM_COUNT));
	}

	public void rebuild() throws Exception
	{
		ORefList measurementRefs = indicator.getMeasurementRefs();
		measurements = new KeyEcologicalAttributeMeasurementNode[measurementRefs.size()];
		for (int i = 0; i < measurementRefs.size(); ++i)
		{
			Measurement measurement = (Measurement) project.findObject(measurementRefs.get(i));
			measurements[i] = new KeyEcologicalAttributeMeasurementNode(this, measurement);
		}
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
	private KeyEcologicalAttributeMeasurementNode[] measurements;
}