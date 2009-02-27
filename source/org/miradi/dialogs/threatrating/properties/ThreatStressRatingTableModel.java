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
package org.miradi.dialogs.threatrating.properties;

import java.util.Vector;

import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Cause;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.StressContributionQuestion;
import org.miradi.questions.StressIrreversibilityQuestion;
import org.miradi.questions.StressRatingChoiceQuestion;
import org.miradi.questions.ThreatStressRatingChoiceQuestion;
import org.miradi.utils.ColumnTagProvider;
import org.miradi.utils.ThreatStressRatingHelper;

public class ThreatStressRatingTableModel extends EditableObjectTableModel implements ColumnTagProvider
{
	public ThreatStressRatingTableModel(Project projectToUse)
	{
		super(projectToUse);
		
		ratings = new ThreatStressRating[0];
	}
	
	public void setObjectRefs(ORef[] hierarchyToSelectedRef)
	{
		ORefList refs = new ORefList(hierarchyToSelectedRef);
		try
		{
			threatBeingEdited = extractThreat(refs);
			rebuild(refs);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}

	private Factor extractThreat(ORefList refs) throws Exception
	{
		ORef linkBeingEditedRef = refs.getRefForType(FactorLink.getObjectType());
		if(linkBeingEditedRef.isInvalid())
			return null;
		
		FactorLink linkBeingEdited = FactorLink.find(getProject(), linkBeingEditedRef);
		if (!linkBeingEdited.isThreatTargetLink())
			return null;
		
		ORef threatRef = linkBeingEdited.getUpstreamThreatRef();
		return Cause.findFactor(getProject(), threatRef);
	}

	private void rebuild(ORefList hierarchyToSelectedRef) throws Exception
	{
		ratings = new ThreatStressRating[0];
		ORef factorLinkRef = hierarchyToSelectedRef.getRefForType(FactorLink.getObjectType());
		if (factorLinkRef.isInvalid())
			return;

		FactorLink factorLink = FactorLink.find(getProject(), factorLinkRef);
		if (!factorLink.isThreatTargetLink())
			return;
		
		ThreatStressRatingHelper helper = new ThreatStressRatingHelper(getProject());
		Vector<ThreatStressRating> threatStressRatings = helper.getRelatedThreatStressRatings(factorLink);
		ratings = threatStressRatings.toArray(new ThreatStressRating[0]);
	}

    public Class getColumnClass(int columnIndex) 
    {
    	if (isIsActiveColumn(columnIndex))
    		return Boolean.class;
    	
    	return super.getColumnClass(columnIndex);
    }
	
	public boolean isCellEditable(int row, int column)
	{
		if (isContributionColumn(column))
			return true;
		
		if (isIrreversibilityColumn(column))
			return true;
		
		if (isIsActiveColumn(column))
			return true;
		
		return false;
	}
	
	public boolean isThreatLabelColumn(int column)
	{
		return getColumnTag(column).equals(THREAT_NAME_COLUMN_TAG);
	}
	
	public boolean isStressLabelColumn(int column)
	{
		return getColumnTag(column).equals(STRESS_NAME_COLUMN_TAG);
	}
	
	public boolean isStressRatingColumn(int column)
	{
		return getColumnTag(column).equals(Stress.PSEUDO_STRESS_RATING);
	}
	
	public boolean isIrreversibilityColumn(int column)
	{
		return getColumnTag(column).equals(ThreatStressRating.TAG_IRREVERSIBILITY);
	}

	public boolean isContributionColumn(int column)
	{
		return getColumnTag(column).equals(ThreatStressRating.TAG_CONTRIBUTION);
	}
	
	public boolean isThreatRatingColumn(int column)
	{
		return getColumnTag(column).equals(ThreatStressRating.PSEUDO_TAG_THREAT_RATING);
	}
	
	public boolean isIsActiveColumn(int column)
	{
		return getColumnTag(column).equals(ThreatStressRating.TAG_IS_ACTIVE);
	}
		
	public String getColumnName(int column)
	{
		if (isThreatLabelColumn(column))
			return EAM.text("Threat Name");
		if (isStressLabelColumn(column))
			return EAM.text("Stress Name");
		if (isStressRatingColumn(column))
			return EAM.fieldLabel(Stress.getObjectType(), getColumnTag(column));
		
		return EAM.fieldLabel(ThreatStressRating.getObjectType(), getColumnTag(column));
	}
	
	public String getColumnTag(int column)
	{
		return getColumnTags()[column];
	}

	public int getColumnCount()
	{
		return getColumnTags().length;
	}

	public int getRowCount()
	{
		return ratings.length;
	}

	public Object getValueAt(int row, int column)
	{
		if (isThreatLabelColumn(column))
		{
			return threatBeingEdited.toString();
		}
		
		if (isStressLabelColumn(column))
		{
			return getStress(row, column).toString();
		}

		if (isStressRatingColumn(column))
		{
			String code = getStress(row, column).getPseudoData(getColumnTag(column));
			return createStressRatingQuestion(column).findChoiceByCode(code);
		}
		
		if (isContributionColumn(column))
		{
			return getThreatStressRating(row, column).getContribution();
		}
		
		if (isIrreversibilityColumn(column))
		{
			return getThreatStressRating(row, column).getIrreversibility();
		}

		if (isThreatRatingColumn(column))
		{
			String code = getThreatStressRating(row, column).getPseudoData(getColumnTag(column));
			return createThreatStressRatingQuestion(column).findChoiceByCode(code);
		}
		
		if (isIsActiveColumn(column))
		{
			return new Boolean(getThreatStressRating(row, column).isActive());
		}
		
		return null;
	}

	private Stress getStress(int row, int column)
	{
		ORef stressRef = getThreatStressRating(row, column).getStressRef();
		Stress stress = (Stress) getProject().findObject(stressRef);
		return stress;
	}
		
	public void setValueAt(Object value, int row, int column)
	{
		if (value == null)
			return;
		
		if (isContributionColumn(column) || isIrreversibilityColumn(column))
		{
			ORef ref = getBaseObjectForRowColumn(row, column).getRef();
			setValueUsingCommand(ref, getColumnTag(column), ((ChoiceItem) value));
		}
		
		if (isIsActiveColumn(column))
		{
			ORef ref = getBaseObjectForRowColumn(row, column).getRef();
			Boolean valueAsBoolean = (Boolean)value;
			setValueUsingCommand(ref, getColumnTag(column), BooleanData.toString(valueAsBoolean));
		}
	}

	public BaseObject getBaseObjectForRowColumn(int row, int column)
	{
		return ratings[row];
	}

	public ThreatStressRating getThreatStressRating(int row, int column)
	{
		return (ThreatStressRating) getBaseObjectForRowColumn(row, column);
	}
	
	public StressContributionQuestion createContributionQuestion(int column)
	{
		return new StressContributionQuestion();
	}
	
	public StressIrreversibilityQuestion createIrreversibilityQuestion(int column)
	{
		return new StressIrreversibilityQuestion();
	}
	
	public StressRatingChoiceQuestion createStressRatingQuestion(int column)
	{
		return new StressRatingChoiceQuestion();
	}
	
	public ThreatStressRatingChoiceQuestion createThreatStressRatingQuestion(int column)
	{
		return new ThreatStressRatingChoiceQuestion();
	}
	
	public static String[] getColumnTags()
	{
		return new String[] {
				ThreatStressRating.TAG_IS_ACTIVE,
				THREAT_NAME_COLUMN_TAG,
				STRESS_NAME_COLUMN_TAG,
				Stress.PSEUDO_STRESS_RATING,
				ThreatStressRating.TAG_CONTRIBUTION,
				ThreatStressRating.TAG_IRREVERSIBILITY,
				ThreatStressRating.PSEUDO_TAG_THREAT_RATING,
		};
	}
	
	private static final String THREAT_NAME_COLUMN_TAG = "Fake Tag: Threat Name";
	private static final String STRESS_NAME_COLUMN_TAG = "Fake Tag: Stress Name";
	private ThreatStressRating[] ratings;
	private Factor threatBeingEdited;
}
