/* 
Copyright 2005-2022, Foundations of Success, Bethesda, Maryland
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
import org.miradi.ids.ResourceAssignmentId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.*;
import org.miradi.project.CurrencyFormat;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.ResourceAssignmentSchema;
import org.miradi.utils.DateRange;
import org.miradi.utils.OptionalDouble;

public class ResourceAssignment extends Assignment
{
	public ResourceAssignment(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, new ResourceAssignmentId(idToUse.asInt()), createSchema(objectManager));
	}

	public static ResourceAssignmentSchema createSchema(Project projectToUse)
	{
		return createSchema(projectToUse.getObjectManager());
	}

	public static ResourceAssignmentSchema createSchema(ObjectManager objectManager)
	{
		return (ResourceAssignmentSchema) objectManager.getSchemas().get(ObjectType.RESOURCE_ASSIGNMENT);
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_PROJECT_RESOURCE_LABEL))
			return getProjectResourceLabel();
		
		if (fieldTag.equals(PSEUDO_TAG_PROJECT_RESOURCE_COST_PER_UNIT))
			return getProjectResourceCostPerUnit();

		if (fieldTag.equals(PSEUDO_TAG_OWNING_FACTOR_NAME))
			return getOwningFactorName();
		
		return super.getPseudoData(fieldTag);
	}
	
	@Override
	public TimePeriodCostsMap getTotalTimePeriodCostsMapForAssignments() throws Exception
	{
		return getTimePeriodCostsMap(TAG_RESOURCE_ASSIGNMENT_IDS);
	}
	
	private String getOwningFactorName()
	{
		Factor owningFactor = getDirectOrIndirectOwningFactor();
		if (owningFactor == null)
			return "";
		
		return owningFactor.toString();
	}

	private String getProjectResourceLabel()
	{
		ProjectResource projectResource = getProjectResource();
		if (projectResource == null)
			return "";
		
		return projectResource.getInitials();
	}

	private String getProjectResourceCostPerUnit()
	{
		ProjectResource projectResource = getProjectResource();
		if (projectResource == null)
			return "";

		try
		{
			double cost = projectResource.getCostPerUnit();
			CurrencyFormat currencyFormatter = getProject().getCurrencyFormatterWithCommas();
			return currencyFormatter.format(cost);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	private ProjectResource getProjectResource()
	{
		return ProjectResource.find(getProject(), getResourceRef());
	}
	
	@Override
	protected TimePeriodCosts createTimePeriodCosts(OptionalDouble quantity)
	{
		return new TimePeriodCosts(getResourceRef(), getFundingSourceRef(), getAccountingCodeRef(), getCategoryOneRef(), getCategoryTwoRef(), quantity); 
	}
	
	public DateRange getCombinedTimePeriodCostsMapDateRange() throws Exception
	{
		DateRange projectDateRange = getProject().getProjectCalendar().getProjectPlanningDateRange();
		return convertDateUnitEffortList().getRolledUpDateRange(projectDateRange);
	}
	
	@Override
	protected String getProjectResourceTag()
	{
		return TAG_RESOURCE_ID;
	}

	@Override
	protected String getFundingSourceTag()
	{
		return TAG_FUNDING_SOURCE_ID;
	}

	@Override
	protected String getAccountingCodeTag()
	{
		return TAG_ACCOUNTING_CODE_ID;
	}
	
	@Override
	public ORef getFundingSourceRef()
	{
		return getRefData(TAG_FUNDING_SOURCE_ID);
	}
	
	@Override
	public ORef getAccountingCodeRef()
	{
		return getRefData(TAG_ACCOUNTING_CODE_ID);
	}
	
	public ORef getResourceRef()
	{
		return getRefData(TAG_RESOURCE_ID);
	}
	
	@Override
	public boolean hasCategoryData()
	{
		if (super.hasCategoryData())
			return true;
		
		return getResourceRef().isValid();
	}
	
	@Override
	public String getFullName()
	{
		return toString();
	}

	@Override
	public String toString()
	{
		ProjectResource projectResource = getProjectResource();
		if (projectResource == null)
			return "";
		
		return projectResource.getFullName();
	}

	public static boolean is(BaseObject baseObject)
	{
		return is(baseObject.getType());
	}
	
	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == ResourceAssignmentSchema.getObjectType();
	}
	
	public static ResourceAssignment find(ObjectManager objectManager, ORef assignmentRef)
	{
		return (ResourceAssignment) objectManager.findObject(assignmentRef);
	}
	
	public static ResourceAssignment find(Project project, ORef assignmentRef)
	{
		return find(project.getObjectManager(), assignmentRef);
	}
	
	public static final String TAG_RESOURCE_ID = "ResourceId";
	public static final String TAG_ACCOUNTING_CODE_ID = "AccountingCode";
	public static final String TAG_FUNDING_SOURCE_ID = "FundingSource";
	public static final String PSEUDO_TAG_PROJECT_RESOURCE_LABEL = "PseudoTagProjectResourceLabel";
	public static final String PSEUDO_TAG_PROJECT_RESOURCE_COST_PER_UNIT = "PseudoTagProjectResourceCostPerUnit";
	public static final String PSEUDO_TAG_OWNING_FACTOR_NAME = "PseudoTagOwningFactorName";
}
