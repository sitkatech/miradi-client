/*
Copyright 2005-2021, Foundations of Success, Bethesda, Maryland
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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.*;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.BaseObjectSchema;
import org.miradi.schemas.OutputSchema;
import org.miradi.schemas.StrategySchema;
import org.miradi.schemas.TaskSchema;
import org.w3c.tidy.Out;

public class Output extends BaseObject
{
    public Output(ObjectManager objectManager, BaseId idToUse, final BaseObjectSchema schema)
    {
        super(objectManager, idToUse, schema);
    }

    public Output(ObjectManager objectManager, BaseId idToUse)
    {
        this(objectManager, idToUse, createSchema(objectManager));
    }

    public static OutputSchema createSchema(Project projectToUse)
    {
        return createSchema(projectToUse.getObjectManager());
    }

    public static OutputSchema createSchema(ObjectManager objectManager)
    {
        return (OutputSchema) objectManager.getSchemas().get(ObjectType.OUTPUT);
    }

    @Override
    public int[] getTypesThatCanOwnUs()
    {
        return new int[] {
                StrategySchema.getObjectType(),
                TaskSchema.getObjectType(),
        };
    }

	@Override
	public String toString()
	{
		return getLabel();
	}

	@Override
	protected RelevancyOverrideSet getIndicatorRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_INDICATOR_IDS);
	}
    
	public ORefList getRelevantGoalRefList() throws Exception
	{
		ORefSet relevantRefList = getDefaultRelevantGoalRefs();
		RelevancyOverrideSet relevantOverrides = getGoalRelevancyOverrideSet();

		return calculateRelevantRefList(relevantRefList, relevantOverrides);
	}
    
	public RelevancyOverrideSet getCalculatedRelevantGoalOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = new ORefList();
		relevantOverrides.addAll(computeRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, all , false));

		return relevantOverrides;
	}
    
	protected ORefSet getDefaultRelevantGoalRefs()
	{
		return new ORefSet();
	}
    
	protected RelevancyOverrideSet getGoalRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_GOAL_IDS);
	}
    
	public ORefList getRelevantObjectiveRefList() throws Exception
	{
		ORefSet relevantRefList = getDefaultRelevantObjectiveRefs();
		RelevancyOverrideSet relevantOverrides = getObjectiveRelevancyOverrideSet();

		return calculateRelevantRefList(relevantRefList, relevantOverrides);
	}
    
	public RelevancyOverrideSet getCalculatedRelevantObjectiveOverrides(ORefList all) throws Exception
	{
		RelevancyOverrideSet relevantOverrides = new RelevancyOverrideSet();
		ORefList defaultRelevantRefList = new ORefList();
		relevantOverrides.addAll(computeRelevancyOverrides(all, defaultRelevantRefList, true));
		relevantOverrides.addAll(computeRelevancyOverrides(defaultRelevantRefList, all , false));

		return relevantOverrides;
	}
	
	protected ORefSet getDefaultRelevantObjectiveRefs()
	{
		return new ORefSet();
	}
    
	protected RelevancyOverrideSet getObjectiveRelevancyOverrideSet()
	{
		return getRawRelevancyOverrideData(TAG_OBJECTIVE_IDS);
	}
	
	@Override
	public boolean isRelevancyOverrideSet(String tag)
	{
		if (tag.equals(Output.TAG_GOAL_IDS))
			return true;

		if (tag.equals(Output.TAG_OBJECTIVE_IDS))
			return true;

		if (tag.equals(Output.TAG_INDICATOR_IDS))
			return true;

		return false;
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_GOAL_REFS))
			return getRelevantGoalRefsAsString();

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS))
			return getRelevantObjectiveRefsAsString();

		if (fieldTag.equals(PSEUDO_TAG_RELEVANT_INDICATOR_REFS))
			return getRelevantIndicatorRefsAsString();

		return super.getPseudoData(fieldTag);
	}

	protected String getRelevantGoalRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantGoalRefList();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	protected String getRelevantObjectiveRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantObjectiveRefList();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	protected String getRelevantIndicatorRefsAsString()
	{
		ORefList refList;
		try
		{
			refList = getRelevantIndicatorRefList();
			return refList.toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

    public static boolean is(ORef ref)
    {
        return is(ref.getObjectType());
    }

    public static boolean is(int objectType)
    {
        return objectType == OutputSchema.getObjectType();
    }

    public static boolean is(BaseObject baseObject)
    {
        return is(baseObject.getType());
    }

    public static Output find(ObjectManager objectManager, ORef outputRef)
    {
        return (Output) objectManager.findObject(outputRef);
    }

    public static Output find(Project project, ORef outputRef)
    {
        return find(project.getObjectManager(), outputRef);
    }

    public String getDetails()
	{
		return getStringData(Factor.TAG_TEXT);
	}

	public static final String TAG_URL = "Url";
	public static final String TAG_DUE_DATE = "DueDate";
    public static final String TAG_GOAL_IDS = "GoalIds";
    public static final String TAG_OBJECTIVE_IDS = "ObjectiveIds";
    public static final String TAG_INDICATOR_IDS = "IndicatorIds";

	public static final String PSEUDO_TAG_RELEVANT_GOAL_REFS = "PseudoRelevantGoalRefs";
	public static final String PSEUDO_TAG_RELEVANT_OBJECTIVE_REFS = "PseudoRelevantObjectiveRefs";
	public static final String PSEUDO_TAG_RELEVANT_INDICATOR_REFS = "PseudoRelevantIndicatorRefs";
}
