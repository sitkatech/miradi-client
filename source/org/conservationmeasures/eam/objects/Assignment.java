/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.AssignmentId;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.ids.TaskId;
import org.conservationmeasures.eam.objectdata.BaseIdData;
import org.conservationmeasures.eam.objectdata.DateRangeEffortListData;
import org.conservationmeasures.eam.objecthelpers.CreateAssignmentParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.DateRangeEffortList;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class Assignment extends BaseObject
{
	public Assignment(ObjectManager objectManager, BaseId idToUse, CreateAssignmentParameter extraInfo)
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
	
	public ORefList getReferencedObjects(int objectType)
	{
		ORefList list = super.getReferencedObjects(objectType);
		
		switch(objectType)
		{
			case ObjectType.PROJECT_RESOURCE: 
				list.addAll(new ORefList(new ORef[] {new ORef(objectType, resourceIdData.getId())}));
				break;
			case ObjectType.ACCOUNTING_CODE: 
				list.addAll(new ORefList(new ORef[] {new ORef(objectType, accountingIdData.getId())}));
				break;
			case ObjectType.FUNDING_SOURCE: 
				list.addAll(new ORefList(new ORef[] {new ORef(objectType, fundingIdData.getId())}));
				break;
		}
		return list;
	}
	
	public DateRangeEffortList getDetails()
	{
		return detailListData.getDateRangeEffortList();
	}
	
	public void setResourceId(BaseId resourceIdToUse)
	{
		resourceIdData.setId(resourceIdToUse);
	}
	
	public BaseId getResourceId()
	{
		return resourceIdData.getId();
	}
	
	public Command[] getCommandsToClearSomeFields()
	{
		Vector<Command> commandsToClearSomeFields = new Vector();
		
		commandsToClearSomeFields.add(new CommandSetObjectData(getRef(), Assignment.TAG_FUNDING_SOURCE, ""));
		commandsToClearSomeFields.add(new CommandSetObjectData(getRef(), Assignment.TAG_ACCOUNTING_CODE, ""));
		commandsToClearSomeFields.add(new CommandSetObjectData(getRef(), Assignment.TAG_DATERANGE_EFFORTS, ""));
		commandsToClearSomeFields.add(new CommandSetObjectData(getRef(), Assignment.TAG_ASSIGNMENT_RESOURCE_ID, ""));

		return commandsToClearSomeFields.toArray(new Command[0]);
	}
	
	public CreateObjectParameter getCreationExtraInfo()
	{
		TaskId taskId = new TaskId(TaskId.INVALID.asInt());
		
		return new CreateAssignmentParameter(taskId);
	}
	
	public void clear()
	{
		super.clear();
		resourceIdData = new BaseIdData();
		detailListData = new DateRangeEffortListData();
		accountingIdData = new BaseIdData();
		fundingIdData = new BaseIdData();
		
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
