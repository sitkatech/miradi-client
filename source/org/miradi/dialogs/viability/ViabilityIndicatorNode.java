/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.viability;

import java.util.Collections;
import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.StatusQuestion;

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
			return new StatusQuestion().findChoiceByCode(getObject().getPseudoData(tag));
		
		if (tag.equals(Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return new ProgressReportStatusQuestion().findChoiceByCode(getObject().getPseudoData(tag));
		
		if(tag.equals(Indicator.TAG_EMPTY))
			return null;
		
		String data = getObject().getData(tag);
		if (!tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
			return data;

		int threasholdColumn = (column + 1) - getFirstIndexOfThreshold();
		return indicator.getThreshold().getStringMap().get(Integer.toString(threasholdColumn));
		
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
		Indicator.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
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