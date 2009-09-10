/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.utils;

import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;

public class SimpleThreatRatingDetailsTableExporter extends AbstractSingleTableExporter
{
	public SimpleThreatRatingDetailsTableExporter(Project projectToUse, Target targetToUse, ORefList threatRefsToUse)
	{
		super(projectToUse);
		
		target = targetToUse;
		threatRefs = threatRefsToUse;
	}

	@Override
	public int getRowCount()
	{
		return threatRefs.size();
	}

	@Override
	public int getColumnCount()
	{
		return columnTags.length;
	}

	@Override
	public int getModelDepth(int row, int modelColumn)
	{
		return 0;
	}

	@Override
	public String getModelColumnName(int modelColumn)
	{
		if (isThreatNameColumn(modelColumn))
			return ThreatStressRatingDetailsTableExporter.getThreatColumnName();

		return EAM.fieldLabel(Stress.getObjectType(), getColumnTag(modelColumn));
	}

	private boolean isThreatNameColumn(int modelColumn)
	{
		return modelColumn == THREAT_NAME_COLUMN_INDEX;
	}

	public String getColumnGroupName(int modelColumn)
	{
		return getModelColumnName(modelColumn);
	}

	private String getColumnTag(int column)
	{
		return columnTags[column];
	}
	
	@Override
	public ChoiceItem getModelChoiceItemAt(int row, int modelColumn)
	{
		String columnTag = getColumnTag(modelColumn);
		Cause threatForRow = (Cause) getBaseObjectForRow(row);
		if (isThreatNameColumn(modelColumn))
			return new TaglessChoiceItem(threatForRow.getFullName());
		
		try
		{	
			SimpleThreatRatingFramework simpleThreatRatingFramework = getProject().getSimpleThreatRatingFramework();
			ThreatRatingBundle bundle = simpleThreatRatingFramework.getBundle(threatForRow.getRef(), getTargetRef());
			if (columnTag.equals(Stress.TAG_SEVERITY))
				return simpleThreatRatingFramework.getSeverityChoiceItem(bundle);
			
			if (columnTag.equals(Stress.TAG_SCOPE))
				return simpleThreatRatingFramework.getScopeChoiceItem(bundle);
			
			if (columnTag.equals(ThreatStressRating.TAG_IRREVERSIBILITY))
				return simpleThreatRatingFramework.getIrreversibilityChoiceItem(bundle);
			
			if (columnTag.equals(Stress.PSEUDO_STRESS_RATING))
			{
				String threatRatingBundleValueCode = new ThreatTargetVirtualLinkHelper(getProject()).getCalculatedThreatRatingBundleValue(threatForRow.getRef(), getTargetRef());
				return simpleThreatRatingFramework.convertToChoiceItem(threatRatingBundleValueCode);
			}
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}

		return new EmptyChoiceItem();
	}

	private ORef getTargetRef()
	{
		return target.getRef();
	}
	
	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}
	
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		ORef threatRef = threatRefs.get(row);
		return Cause.find(getProject(), threatRef);
	}
	
	@Override
	public int getRowType(int row)
	{
		return getBaseObjectForRow(row).getType();
	}

	@Override
	public String getModelTextAt(int row, int modelColumn)
	{
		return "";
	}

	@Override
	public ORefList getAllRefs(int objectType)
	{
		ORefList allObjectRefs = new ORefList();
		for (int row = 0; row < getRowCount(); ++row)
		{
			allObjectRefs.add(getBaseObjectForRow(row).getRef());
		}
		
		return allObjectRefs;
	}

	@Override
	public Vector<Integer> getAllTypes()
	{
		if (getRowCount() == 0)
			return new Vector<Integer>();
		
		Vector<Integer> rowTypes = new Vector<Integer>();
		rowTypes.add(getRowType(0));
		
		return rowTypes;
	}
	
	private ORefList threatRefs;
	private Target target;
	private static final int THREAT_NAME_COLUMN_INDEX = 0;
	private final String[] columnTags = new String[]{
								Cause.TAG_LABEL,  
								Stress.TAG_SCOPE,
								Stress.TAG_SEVERITY,
								ThreatStressRating.TAG_IRREVERSIBILITY,
								Stress.PSEUDO_STRESS_RATING,
			};
}
