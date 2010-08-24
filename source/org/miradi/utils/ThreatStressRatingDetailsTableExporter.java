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

public class ThreatStressRatingDetailsTableExporter extends	AbstractThreatRatingDetailsTableExporter
{
	public ThreatStressRatingDetailsTableExporter(Project projectToUse, Target targetToUse, Cause threatToUse)
	{
		super(projectToUse, targetToUse, columnTags);
		
		threat = threatToUse;
		stressesForRows = loadStressesFromTarget(targetToUse);
	}

	private Vector<Stress> loadStressesFromTarget(Target targetToUse)
	{
		Vector<Stress> stresses = new Vector<Stress>();
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
	public String getModelColumnName(int modelColumn)
	{
		if (modelColumn == STRESS_NAME_COLUMN_INDEX)
			return EAM.text("Stress");

		if (isThreatNameColumn(modelColumn))
			return getThreatColumnName();

		return EAM.fieldLabel(Stress.getObjectType(), getColumnTag(modelColumn));
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
			return getContribution(getProject(), getTargetRef(), threat.getRef(), stressForRow);
		
		if (columnTag.equals(ThreatStressRating.TAG_IRREVERSIBILITY))
			return getIrreversibility(getProject(), getTargetRef(), threat.getRef(), stressForRow);
		
		String valueToConvert = stressForRow.getData(columnTag);
		return TargetThreatLinkTableModel.convertThreatRatingCodeToChoiceItem(valueToConvert);
	}

	public static ChoiceItem getIrreversibility(Project project, ORef targetRef, ORef threatRef, Stress stress)
	{
		ThreatStressRating threatStressRating = findThreatStressRating(project, targetRef, threatRef, stress);
		if (threatStressRating == null)
			return new EmptyChoiceItem();
			
		return threatStressRating.getIrreversibility();
	}

	public static ChoiceItem getContribution(Project project, ORef targetRef, ORef threatRef, Stress stress)
	{
		ThreatStressRating threatStressRating = findThreatStressRating(project, targetRef, threatRef, stress);
		if (threatStressRating == null)
			return new EmptyChoiceItem();
		
		return threatStressRating.getContribution();
	}
	
	public static ThreatStressRating findThreatStressRating(Project project, ORef targetRef, ORef threatRef, Stress stress)
	{
		ThreatTargetVirtualLinkHelper virtualLink = new ThreatTargetVirtualLinkHelper(project);
		ORef threatStressRatingRef = virtualLink.findThreatStressRating(threatRef, targetRef, stress.getRef());
		
		return ThreatStressRating.find(project, threatStressRatingRef);
	}

	@Override
	public BaseObject getBaseObjectForRow(int row)
	{
		return stressesForRows.get(row);
	}
	
	private boolean isStressNameColumn(BaseObject baseObjectForRow, String columnTag)
	{
		return (Stress.is(baseObjectForRow) && columnTag.equals(Stress.TAG_LABEL));
	} 
	
	private Cause threat;
	private Vector<Stress> stressesForRows;
	
	private static final int STRESS_NAME_COLUMN_INDEX = 1;
	private static final String[] columnTags = new String[]{
			Cause.TAG_LABEL, 
			Stress.TAG_LABEL, 
			Stress.TAG_SEVERITY, 
			Stress.TAG_SCOPE, 
			Stress.PSEUDO_STRESS_RATING, 
			ThreatStressRating.TAG_CONTRIBUTION, 
			ThreatStressRating.TAG_IRREVERSIBILITY, 
			};
}
