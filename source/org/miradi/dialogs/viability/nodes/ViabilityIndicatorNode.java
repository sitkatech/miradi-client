/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 
package org.miradi.dialogs.viability.nodes;

import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.ProgressReportStatusQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.utils.BaseObjectDateAndIdComparator;

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

	@Override
	public String toRawString()
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
		
		if (tag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return new ProgressReportStatusQuestion().findChoiceByCode(getObject().getPseudoData(tag));
		
		if(tag.equals(Indicator.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		String data = getObject().getData(tag);
		
		if(tag.equals(Indicator.TAG_RATING_SOURCE))
			return new RatingSourceQuestion().findChoiceByCode(data);
		
		if (tag.equals(Indicator.TAG_INDICATOR_THRESHOLD))
		{
			int threasholdColumn = (column + 1) - getFirstIndexOfThreshold();
			String threashold = indicator.getThreshold().getStringMap().get(Integer.toString(threasholdColumn));
			
			return new TaglessChoiceItem(threashold);
		}
		
		return new TaglessChoiceItem(data);
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
			measurementAndFutureStatusObjects.add(new ViabilityMeasurementNode(this, measurement));
		}

		Collections.sort(measurementAndFutureStatusObjects, new MeasurementNodeDateComparator());		
		measurementAndFutureStatusObjects.add(new ViabilityFutureStatusNode(this));
		measurements = (TreeTableNode[]) measurementAndFutureStatusObjects.toArray(new TreeTableNode[0]);
	}
	
	public static class MeasurementNodeDateComparator implements Comparator<TreeTableNode>
	{
		public int compare(TreeTableNode rawNode1, TreeTableNode rawNode2)
		{
			return BaseObjectDateAndIdComparator.compare(rawNode1.getObject(), rawNode2.getObject(), Measurement.TAG_DATE);
		}	
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
		
		Indicator.TAG_RATING_SOURCE,
		BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
	};
	
	private Project project;
	private Indicator indicator;
	private TreeTableNode keyEcologicalAttributesNode;
	private TreeTableNode[] measurements;
}