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

import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
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
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.EmptyChoiceItem;
import org.miradi.questions.TaglessChoiceItem;

public class ThreatStressRatingDetailsTableExporter extends	AbstractSingleTableExporter
{
	public ThreatStressRatingDetailsTableExporter(Project projectToUse, Target targetToUse, Cause threatToUse)
	{
		super(projectToUse);
		
		target = targetToUse;
		threat = threatToUse;
		stressesForRows = loadStressesFromTarget(targetToUse);
	}

	private Vector<Stress> loadStressesFromTarget(Target targetToUse)
	{
		Vector stresses = new Vector();
		for (int index = 0; index < targetToUse.getStressRefs().size(); ++index)
		{
			Stress stress = Stress.find(getProject(), targetToUse.getStressRefs().get(index));
			stresses.add(stress);
		}
		
		return stresses;
	}
	
	@Override
	public int getRowCount()
	{
		return stressesForRows.size();
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
		if (modelColumn == STRESS_NAME_COLUMN_INDEX)
			return EAM.text("Stress");

		if (isThreatNameColumn(modelColumn))
			return EAM.text("Threat");

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
		Stress stressForRow = (Stress) getBaseObjectForRow(row);
		
		if (isThreatNameColumn(modelColumn))
			return new TaglessChoiceItem(threat.getFullName());
		
		if (isStressNameColumn(stressForRow, columnTag))
			return new TaglessChoiceItem(stressForRow.getFullName());
		
		if (columnTag.equals(ThreatStressRating.TAG_CONTRIBUTION))
			return getContribution(stressForRow);
		
		if (columnTag.equals(ThreatStressRating.TAG_IRREVERSIBILITY))
			return getIrreversibility(stressForRow);
		
		String valueToConvert = stressForRow.getData(columnTag);
		return TargetThreatLinkTableModel.convertThreatRatingCodeToChoiceItem(valueToConvert);
	}

	private ChoiceItem getIrreversibility(Stress stressForRow)
	{
		ThreatStressRating threatStressRating = findThreatStressRating(stressForRow);
		if (threatStressRating == null)
			return new EmptyChoiceItem();
			
		return threatStressRating.getIrreversibility();
	}

	private ChoiceItem getContribution(Stress stressForRow)
	{
		ThreatStressRating threatStressRating = findThreatStressRating(stressForRow);
		if (threatStressRating == null)
			return new EmptyChoiceItem();
		
		return threatStressRating.getContribution();
	}
	
	private ThreatStressRating findThreatStressRating(Stress stress)
	{
		ThreatTargetVirtualLinkHelper virtualLink = new ThreatTargetVirtualLinkHelper(getProject());
		ORef threatStressRatingRef = virtualLink.findThreatStressRating(threat.getRef(), target.getRef(), stress.getRef());
		
		return ThreatStressRating.find(getProject(), threatStressRatingRef);
	}

	@Override
	public int getMaxDepthCount()
	{
		return 0;
	}
	
	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return stressesForRows.get(row);
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
	
	private boolean isStressNameColumn(BaseObject baseObjectForRow, String columnTag)
	{
		return (Stress.is(baseObjectForRow) && columnTag.equals(Stress.TAG_LABEL));
	} 
	
	private Cause threat;
	private Target target;
	
	private Vector<Stress> stressesForRows;
	private static final int THREAT_NAME_COLUMN_INDEX = 0;
	private static final int STRESS_NAME_COLUMN_INDEX = 1;
	
	private final String[] columnTags = new String[]{
			Cause.TAG_LABEL, 
			Stress.TAG_LABEL, 
			Stress.TAG_SEVERITY, 
			Stress.TAG_SCOPE, 
			Stress.PSEUDO_STRESS_RATING, 
			ThreatStressRating.TAG_CONTRIBUTION, 
			ThreatStressRating.TAG_IRREVERSIBILITY, 
			};
}
