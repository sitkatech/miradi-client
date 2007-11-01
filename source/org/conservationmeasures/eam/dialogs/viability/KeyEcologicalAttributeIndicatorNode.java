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
	public KeyEcologicalAttributeIndicatorNode(Project projectToUse, KeyEcologicalAttributeNode parent, Indicator indicatorToUse) throws Exception
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

	//TODO: this method could be pulled up to the supper
	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		return getObject().getData(tag);
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
		Indicator.TAG_EMPTY,
		Indicator.TAG_EMPTY,
		Indicator.TAG_EMPTY,
		Indicator.TAG_EMPTY,
		Indicator.TAG_EMPTY,
	};
	
	private Project project;
	private Indicator indicator;
	private KeyEcologicalAttributeNode keyEcologicalAttributesNode;
	private KeyEcologicalAttributeMeasurementNode[] measurements;
}