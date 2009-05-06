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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.ids.ResourceAssignmentId;
import org.miradi.main.EAM;
import org.miradi.objectdata.BaseIdData;
import org.miradi.objectdata.DateRangeEffortListData;
import org.miradi.objecthelpers.DateRangeEffortList;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.BudgetCalculator;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.DateRange;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.OptionalDouble;

public class ResourceAssignment extends Assignment
{
	public ResourceAssignment(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, new ResourceAssignmentId(idToUse.asInt()));
		clear();
	}
	
	public ResourceAssignment(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new ResourceAssignmentId(idAsInt), json);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.RESOURCE_ASSIGNMENT;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public DateRangeEffortList getDetails()
	{
		return detailListData.getDateRangeEffortList();
	}
	
	@Override
	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_PROJECT_RESOURCE_LABEL))
			return getProjectResourceLabel();
		
		if (fieldTag.equals(PSEUDO_TAG_OWNING_FACTOR_NAME))
			return getOwningFactorName();
		
		if (fieldTag.equals(PSEUDO_TAG_WHEN))
			return getWhen();
		
		if (fieldTag.equals(PSEUDO_TAG_BUDGET_TOTAL))
			return getBudgetTotal();
		
		if (fieldTag.equals(PSEUDO_TAG_WORK_UNIT_TOTAL))
			return getWorkUnitTotal();
		
		return super.getPseudoData(fieldTag);
	}
	
	private String getWorkUnitTotal()
	{
		StringBuffer workUnitTotal = new StringBuffer("");
		ProjectResource projectResource = getProjectResource();
		if (projectResource == null)
			return workUnitTotal.toString();

		try
		{	
			workUnitTotal.append(getDateRangeEffortList().getTotalUnitQuantity());
			workUnitTotal.append(" ");
			
			String costUnit = projectResource.getCostUnitValue();
			workUnitTotal.append(costUnit);

			return workUnitTotal.toString();
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return null;
		}
	}

	private String getBudgetTotal()
	{
		try
		{
			//TODO should these variables be only initialized once in project?
			BudgetCalculator totalsCalculator = new BudgetCalculator(getProject());
			DateRange dateRange = getProject().getProjectCalendar().combineStartToEndProjectRange();

			double totalCost = totalsCalculator.getTotalCost(this, dateRange);
			return Double.toString(totalCost);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error");
		}
	}

	private String getWhen()
	{
		try
		{
			DateRange combinedDateRange = getDetails().getCombinedDateRange();
			if (combinedDateRange != null)
				return combinedDateRange.toString();
			
			return "";
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "";
		}
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

	private ProjectResource getProjectResource()
	{
		return ProjectResource.find(getProject(), getResourceRef());
	}
	
	@Override
	public double getBudgetCostRollup(DateRange dateRangeToUse) throws Exception
	{
		return getTotalResourceAssignmentCost(dateRangeToUse);
	}
	
	public double getTotalResourceAssignmentCost(DateRange dateRangeToUse) throws Exception
	{
		double cost = 0;
		ProjectResource projectResource = getProjectResource();
		if (projectResource != null)
		{
			DateRangeEffortList effortList = getDateRangeEffortList();
			double totalCostPerResourceAssignment = getTotalUnitQuantity(dateRangeToUse, projectResource.getCostPerUnit(), effortList);
			cost += totalCostPerResourceAssignment;
		}
		
		return cost;
	}
	
	private double getTotalUnitQuantity(DateRange dateRangeToUse, double costPerUnit, DateRangeEffortList effortList)
	{
		if (dateRangeToUse != null)
			return (effortList.getTotalUnitQuantity(dateRangeToUse) * costPerUnit);
		
		return (effortList.getTotalUnitQuantity() * costPerUnit);
	}
	
	@Override
	public OptionalDouble getWorkUnits(DateRange dateRangeToUse) throws Exception
	{
		return getDateRangeEffortList().getOptionalTotalUnitQuantity(dateRangeToUse);
	}
	
	public DateRange getCombinedEffortListDateRange() throws Exception
	{
		return getDetails().getCombinedDateRange();
	}
	
	public void setResourceId(BaseId resourceIdToUse)
	{
		resourceIdData.setId(resourceIdToUse);
	}
	
	public ORef getFundingSourceRef()
	{
		return fundingIdData.getRef();
	}
	
	public ORef getAccountingCodeRef()
	{
		return accountingIdData.getRef();
	}
	
	public ORef getResourceRef()
	{
		return resourceIdData.getRef();
	}
	
	public DateRangeEffortList getDateRangeEffortList() throws Exception
	{
		String dREffortListAsString = getData(ResourceAssignment.TAG_DATERANGE_EFFORTS);
		return new DateRangeEffortList(dREffortListAsString);
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
		return objectType == getObjectType();
	}
	
	public static ResourceAssignment find(ObjectManager objectManager, ORef assignmentRef)
	{
		return (ResourceAssignment) objectManager.findObject(assignmentRef);
	}
	
	public static ResourceAssignment find(Project project, ORef assignmentRef)
	{
		return find(project.getObjectManager(), assignmentRef);
	}
	
	@Override
	public void clear()
	{
		super.clear();
		resourceIdData = new BaseIdData(TAG_RESOURCE_ID, ProjectResource.getObjectType());
		detailListData = new DateRangeEffortListData(TAG_DATERANGE_EFFORTS);
		accountingIdData = new BaseIdData(TAG_ACCOUNTING_CODE, AccountingCode.getObjectType());
		fundingIdData = new BaseIdData(TAG_FUNDING_SOURCE, FundingSource.getObjectType());
		pseudoProjectResourceLabel = new PseudoStringData(PSEUDO_TAG_PROJECT_RESOURCE_LABEL);
		pseudoOwningFactorNameLabel = new PseudoStringData(PSEUDO_TAG_OWNING_FACTOR_NAME);
		pseudoWhen = new PseudoStringData(PSEUDO_TAG_WHEN);
		pseudoBudgetTotal = new PseudoStringData(PSEUDO_TAG_BUDGET_TOTAL);
		pseudoWorkUnitTotal = new PseudoStringData(PSEUDO_TAG_WORK_UNIT_TOTAL);
		
		addField(TAG_RESOURCE_ID, resourceIdData);
		addField(TAG_DATERANGE_EFFORTS, detailListData);
		addField(TAG_ACCOUNTING_CODE, accountingIdData);
		addField(TAG_FUNDING_SOURCE, fundingIdData);
		addField(PSEUDO_TAG_PROJECT_RESOURCE_LABEL, pseudoProjectResourceLabel);
		addField(PSEUDO_TAG_OWNING_FACTOR_NAME, pseudoOwningFactorNameLabel);
		addField(PSEUDO_TAG_WHEN, pseudoWhen);
		addField(PSEUDO_TAG_BUDGET_TOTAL, pseudoBudgetTotal);
		addField(PSEUDO_TAG_WORK_UNIT_TOTAL, pseudoWorkUnitTotal);
	}
	
	public static final String TAG_RESOURCE_ID = "ResourceId";
	public static final String TAG_DATERANGE_EFFORTS = "Details";
	public static final String TAG_ACCOUNTING_CODE = "AccountingCode";
	public static final String TAG_FUNDING_SOURCE = "FundingSource";
	public static final String PSEUDO_TAG_PROJECT_RESOURCE_LABEL = "PseudoTagProjectResourceLabel";
	public static final String PSEUDO_TAG_OWNING_FACTOR_NAME = "PseudoTagOwningFactorName";
	public static final String PSEUDO_TAG_WHEN = "PseudoWhen";
	public static final String PSEUDO_TAG_BUDGET_TOTAL = "PseudoBudgetTotal";
	public static final String PSEUDO_TAG_WORK_UNIT_TOTAL = "PseudoWorkUnitTotal";
	
	
	public static final String OBJECT_NAME = "Assignment";
	
	private BaseIdData resourceIdData;
	private DateRangeEffortListData detailListData;
	private BaseIdData accountingIdData;
	private BaseIdData fundingIdData;
	private PseudoStringData pseudoProjectResourceLabel;
	private PseudoStringData pseudoOwningFactorNameLabel;
	private PseudoStringData pseudoWhen;
	private PseudoStringData pseudoBudgetTotal;
	private PseudoStringData pseudoWorkUnitTotal;
}
