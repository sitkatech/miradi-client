/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.AssignmentId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.DateRangeEffortListData;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Assignment extends BaseObject
{
	public Assignment(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, new AssignmentId(idToUse.asInt()));
		clear();
	}
	
	public Assignment(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager,new TaskId(idAsInt), json);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.ASSIGNMENT;
	}
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.PROJECT_RESOURCE: 
				return true;
			case ObjectType.ACCOUNTING_CODE: 
				return true;
			case ObjectType.FUNDING_SOURCE: 
				return true;
			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set set = super.getReferencedObjectTags();
		set.add(TAG_ASSIGNMENT_RESOURCE_ID);
		set.add(TAG_ACCOUNTING_CODE);
		set.add(TAG_FUNDING_SOURCE);
		return set;
	}
	
	public DateRangeEffortList getDetails()
	{
		return detailListData.getDateRangeEffortList();
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
	
	public Vector<Command> getCommandsToShiftEffort(int monthDelta) throws Exception
	{
		Vector<Command> commands = new Vector<Command>();
		DateRangeEffortList shifted = detailListData.getDateRangeEffortList().cloneShiftedByMonths(monthDelta);
		CommandSetObjectData cmd = new CommandSetObjectData(getRef(), Assignment.TAG_DATERANGE_EFFORTS, shifted.toString());
		commands.add(cmd);
		return commands;
	}

	public DateRangeEffortList getDateRangeEffortList() throws Exception
	{
		String dREffortListAsString = getData(Assignment.TAG_DATERANGE_EFFORTS);
		return new DateRangeEffortList(dREffortListAsString);
	}
	
	public static Assignment find(ObjectManager objectManager, ORef assignmentRef)
	{
		return (Assignment) objectManager.findObject(assignmentRef);
	}
	
	public static Assignment find(Project project, ORef assignmentRef)
	{
		return find(project.getObjectManager(), assignmentRef);
	}
	
	public void clear()
	{
		super.clear();
		resourceIdData = new BaseIdData(ProjectResource.getObjectType());
		detailListData = new DateRangeEffortListData();
		accountingIdData = new BaseIdData(AccountingCode.getObjectType());
		fundingIdData = new BaseIdData(FundingSource.getObjectType());
		
		addField(TAG_ASSIGNMENT_RESOURCE_ID, resourceIdData);
		addField(TAG_DATERANGE_EFFORTS, detailListData);
		addField(TAG_ACCOUNTING_CODE, accountingIdData);
		addField(TAG_FUNDING_SOURCE, fundingIdData);
	}
	
	public static final String TAG_ASSIGNMENT_RESOURCE_ID = "ResourceId";
	public static final String TAG_DATERANGE_EFFORTS = "Details";
	public static final String TAG_ACCOUNTING_CODE = "AccountingCode";
	public static final String TAG_FUNDING_SOURCE = "FundingSource";
	
	public static final String OBJECT_NAME = "Assignment";
	
	BaseIdData resourceIdData;
	DateRangeEffortListData detailListData;
	BaseIdData accountingIdData;
	BaseIdData fundingIdData;
}
