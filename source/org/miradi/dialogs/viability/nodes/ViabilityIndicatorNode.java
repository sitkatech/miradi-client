/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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
import java.util.Vector;

import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.objecthelpers.BaseObjectByTagSorter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FutureStatus;
import org.miradi.objects.Indicator;
import org.miradi.objects.Measurement;
import org.miradi.project.Project;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.ProgressReportShortStatusQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.TaglessChoiceItem;
import org.miradi.schemas.FutureStatusSchema;

public class ViabilityIndicatorNode extends TreeTableNode
{
	public ViabilityIndicatorNode(Project projectToUse, TreeTableNode parent, Indicator indicatorToUse) throws Exception
	{
		project = projectToUse;
		indicator = indicatorToUse;
		keyEcologicalAttributesNode = parent;
		rebuild();
	}
	
	@Override
	public BaseObject getObject()
	{
		return getIndicator();
	}

	private Indicator getIndicator()
	{
		return indicator;
	}

	@Override
	public ORef getObjectReference()
	{
		return indicator.getRef();
	}
	
	@Override
	public int getType()
	{
		return indicator.getType();
	}

	@Override
	public String getNodeLabel()
	{
		return indicator.toString();
	}

	@Override
	public int getChildCount()
	{
		return measurements.length;
	}

	@Override
	public TreeTableNode getChild(int index)
	{
		return measurements[index];
	}
	
	@Override
	public TreeTableNode getParentNode()
	{
		return keyEcologicalAttributesNode;
	}

	@Override
	public Object getValueAt(int column)
	{
		String tag = COLUMN_TAGS[column];
		if (tag.equals(Indicator.PSEUDO_TAG_STATUS_VALUE))
			return new StatusQuestion().findChoiceByCode(getObject().getPseudoData(tag));
		
		if (tag.equals(BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE))
			return new ProgressReportShortStatusQuestion().findChoiceByCode(getObject().getPseudoData(tag));
		
		if(tag.equals(Indicator.TAG_EMPTY))
			return new EmptyChoiceItem();
		
		String data = getObject().getData(tag);
		
		if(tag.equals(Indicator.TAG_RATING_SOURCE) && isViabilityIndicator())
			return new RatingSourceQuestion().findChoiceByCode(data);
		
		if (tag.equals(Indicator.TAG_THRESHOLDS_MAP))
		{
			int thresholdColumn = (column + 1) - getFirstIndexOfThreshold();
			String threshold = indicator.getThresholdsMap().getCodeToUserStringMap().getUserString(Integer.toString(thresholdColumn));
			
			return new TaglessChoiceItem(threshold);
		}
		
		return new TaglessChoiceItem(data);
	}

	private boolean isViabilityIndicator()
	{
		return getIndicator().isViabilityIndicator();
	}
	
	private int getFirstIndexOfThreshold()
	{
		for (int i = 0; i < COLUMN_TAGS.length; ++i)
		{
			if (COLUMN_TAGS[i].equals(Indicator.TAG_THRESHOLDS_MAP))
				return i;
		}
		
		throw new RuntimeException("Could not find Threshold in array.");
	}

	@Override
	public void rebuild() throws Exception
	{
		ORefList measurementRefs = indicator.getMeasurementRefs();
		Vector<TreeTableNode> measurementNodes = new Vector<TreeTableNode>();
		for (int i = 0; i < measurementRefs.size(); ++i)
		{
			Measurement measurement = (Measurement) project.findObject(measurementRefs.get(i));
			measurementNodes.add(new ViabilityMeasurementNode(this, measurement));
		}

		Collections.sort(measurementNodes, new MeasurementNodeDateComparator());
		
		Vector<TreeTableNode> futureStatusNodes = new Vector<TreeTableNode>();
		ORefList futureStatusRefs = indicator.getFutureStatusRefs();
		for(ORef futureStatusRef : futureStatusRefs)
		{
			FutureStatus futureStatus = FutureStatus.find(project, futureStatusRef);
			futureStatusNodes.add(new ViabilityFutureStatusNode(this, futureStatus));	
		}
		
		Collections.sort(measurementNodes, new MeasurementNodeDateComparator());
		Collections.sort(futureStatusNodes, new FutureStatusNodeDateComparator());
		Vector<TreeTableNode> measurementAndFutureStatusNodes = new Vector<TreeTableNode>();
		measurementAndFutureStatusNodes.addAll(measurementNodes);
		measurementAndFutureStatusNodes.addAll(futureStatusNodes);
		
		measurements = measurementAndFutureStatusNodes.toArray(new TreeTableNode[0]);
	}
	
	private static class MeasurementNodeDateComparator extends BaseObjectByTagSorter
	{
		public MeasurementNodeDateComparator()
		{
			super(Measurement.TAG_DATE);
		}	
	}
	
	private static class FutureStatusNodeDateComparator extends BaseObjectByTagSorter
	{
		public FutureStatusNodeDateComparator()
		{
			super(FutureStatusSchema.TAG_FUTURE_STATUS_DATE);
		}
	}
	
	public static final String[] COLUMN_TAGS = {
		Indicator.TAG_LABEL,
		Indicator.TAG_EMPTY,
		Indicator.PSEUDO_TAG_STATUS_VALUE,
		Indicator.TAG_EMPTY,
		
		Indicator.TAG_THRESHOLDS_MAP,
		Indicator.TAG_THRESHOLDS_MAP,
		Indicator.TAG_THRESHOLDS_MAP,
		Indicator.TAG_THRESHOLDS_MAP,
		
		Indicator.TAG_RATING_SOURCE,
		BaseObject.PSEUDO_TAG_LATEST_PROGRESS_REPORT_CODE,
	};
	
	private Project project;
	private Indicator indicator;
	private TreeTableNode keyEcologicalAttributesNode;
	private TreeTableNode[] measurements;
}