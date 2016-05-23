/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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

package org.miradi.dialogfields;

import org.miradi.commands.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Timeframe;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.schemas.ProjectResourceSchema;
import org.miradi.schemas.TimeframeSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.CommandVector;
import org.miradi.utils.DateUnitEffort;
import org.miradi.utils.DateUnitEffortList;
import org.miradi.views.diagram.CreateAnnotationDoer;
import org.miradi.views.planning.doers.TreeNodeDeleteDoer;

import java.util.Vector;

public class WhoPlannedCodeListEditorComponent extends AbstractQuestionBasedComponent
{
	public WhoPlannedCodeListEditorComponent(BaseObject baseObjectToUse, ChoiceQuestion questionToUse)
	{
		super(questionToUse, SINGLE_COLUMN);
		
		baseObject = baseObjectToUse;
		updateToggleButtonSelections(baseObject.getPlannedWhoResourcesAsCodeList());
	}
	
	@Override
	public void toggleButtonStateChanged(ChoiceItem choiceItem, boolean isSelected)	throws Exception
	{
		CodeList currentCodes = getBaseObject().getPlannedWhoResourcesAsCodeList();
		boolean doesCodeExist = currentCodes.contains(choiceItem.getCode());
		final boolean needToDelete = doesCodeExist && !isSelected;
		final boolean needToCreate = !doesCodeExist && isSelected;
		ORef refCode = ORef.createFromString(choiceItem.getCode());
		
		if (needToDelete)
			deleteMatchingTimeframes(refCode);
		if (needToCreate)
			createTimeframe(refCode);
	}

	private void deleteMatchingTimeframes(ORef selectedResourceRef) throws Exception
	{
		getProject().executeBeginTransaction();
		try
		{
			int oldTimeframeRefsSize = getTimeframeRefs().size();
			DateUnitEffortList oldDateUnitEffortList = getDateUnitEffortListFromTimeframe();
			Vector<Timeframe> timeframesToDelete = extractTimeframes(selectedResourceRef);
			
			removeTimeframes(timeframesToDelete);
			updateDividedDateUnitEffortList(oldTimeframeRefsSize, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeEndTransaction();
		}
	}

	private void clearResourceRef(Timeframe timeframe) throws Exception
	{
		setTimeframeResource(timeframe, ORef.INVALID);
	}

	private void setTimeframeResource(Timeframe timeframe, ORef resourceRef) throws Exception
	{
		CommandSetObjectData setTimeframeResourceRef = new CommandSetObjectData(timeframe, Timeframe.TAG_RESOURCE_ID, resourceRef.getObjectId().toString());
		getProject().executeCommand(setTimeframeResourceRef);
	}

	private void removeTimeframes(Vector<Timeframe> timeframesToDelete) throws Exception
	{
		for (int index = 0; index < timeframesToDelete.size(); ++index)
		{
			Timeframe timeframe = timeframesToDelete.get(index);
			clearResourceRef(timeframe);
			if (getTimeframeRefs().size() > 1)
			{
				CommandVector deleteTimeframe = TreeNodeDeleteDoer.buildCommandsToDeleteAnnotation(getProject(), timeframe, getTimeframeTag());
				getProject().executeCommands(deleteTimeframe);
			}
		}
	}

	private void createTimeframe(ORef resourceRef) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORefList oldTimeframeRefs = getTimeframeRefs();
			int oldTimeframesCount = oldTimeframeRefs.size();
			DateUnitEffortList oldDateUnitEffortList = getDateUnitEffortListFromTimeframe();

			Timeframe timeframeWithoutResource = findTimeframeWithoutResource();
			if (timeframeWithoutResource == null)
				timeframeWithoutResource = createNewTimeframe();
			
			setTimeframeResource(timeframeWithoutResource, resourceRef);
			updateDividedDateUnitEffortList(oldTimeframesCount, oldDateUnitEffortList);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private Timeframe findTimeframeWithoutResource() throws Exception
	{
		ORef invalidResourceRef = ORef.createInvalidWithType(ProjectResourceSchema.getObjectType());
		Vector<Timeframe> timeframesWithoutResource = extractTimeframes(invalidResourceRef);
		if (timeframesWithoutResource.size() == 0)
			return null;
		
		return timeframesWithoutResource.get(0);
	}
	
	private Vector<Timeframe> extractTimeframes(ORef selectedTimeframeRef) throws Exception
	{
		ORefList oldTimeframeRefs = getTimeframeRefs();
		Vector<Timeframe> timeframesToDelete = new Vector<Timeframe>();
		for (int index = 0; index < oldTimeframeRefs.size(); ++index)
		{
			Timeframe timeframe = Timeframe.find(getProject(), oldTimeframeRefs.get(index));
			ORef resourceRef = timeframe.getResourceRef();
			if (resourceRef.equals(selectedTimeframeRef))
				timeframesToDelete.add(timeframe);
		}
		
		return timeframesToDelete;
	}

	private Timeframe createNewTimeframe() throws Exception
	{
		CommandCreateObject createCommand = new CommandCreateObject(TimeframeSchema.getObjectType());
		getProject().executeCommand(createCommand);

		ORef newTimeframeRef = createCommand.getObjectRef();
		Command appendCommand = CreateAnnotationDoer.createAppendCommand(getBaseObject(), newTimeframeRef, getTimeframeTag());
		getProject().executeCommand(appendCommand);
		
		return Timeframe.find(getProject(), newTimeframeRef);
	}

	private void updateDividedDateUnitEffortList(int oldTimeframeCount, DateUnitEffortList oldDateUnitEffortList) throws Exception
	{
		ORefList newTimeframeRefs = getTimeframeRefs();
		DateUnitEffortList templateDateUnitEffortList = createTemplateDateUnitEffortList(oldDateUnitEffortList, oldTimeframeCount, newTimeframeRefs.size());
		updateDateUnitEffortLists(newTimeframeRefs, templateDateUnitEffortList);
	}

	private DateUnitEffortList createTemplateDateUnitEffortList(DateUnitEffortList oldDateUnitEffortList, int oldTimeframeCount, int newTimeframeCount) throws Exception
	{
		DateUnitEffortList newDateUnitEffortList = new DateUnitEffortList();
		for (int index = 0; index < oldDateUnitEffortList.size(); ++index)
		{
			DateUnitEffort oldDateUnitEffort = oldDateUnitEffortList.getDateUnitEffort(index);
			double oldTotalUnits = oldDateUnitEffort.getQuantity() * oldTimeframeCount;
			double newUnitQuantity = oldTotalUnits / newTimeframeCount;
			DateUnitEffort newDateUnitEffort = new DateUnitEffort(oldDateUnitEffort.getDateUnit(), newUnitQuantity);
			newDateUnitEffortList.add(newDateUnitEffort);
		}
		
		return newDateUnitEffortList;
	}
	
	private void updateDateUnitEffortLists(ORefList newTimeframeRefs, DateUnitEffortList templateDateUnitEffortList) throws Exception
	{
		for (int index = 0; index < newTimeframeRefs.size(); ++index)
		{
			Timeframe timeframe = Timeframe.find(getProject(), newTimeframeRefs.get(index));
			CommandSetObjectData setDateUnitEffortList = new CommandSetObjectData(timeframe, Timeframe.TAG_DATEUNIT_EFFORTS, templateDateUnitEffortList.toString());
			getProject().executeCommand(setDateUnitEffortList);
		}
	}
	
	private DateUnitEffortList getDateUnitEffortListFromTimeframe() throws Exception
	{
		ORefList existingTimeframeRefs = getTimeframeRefs();
		if (existingTimeframeRefs.isEmpty())
			return new DateUnitEffortList();

		Timeframe firstTimeframe = Timeframe.find(getProject(), existingTimeframeRefs.get(0));
		return firstTimeframe.getDateUnitEffortList();
	}
	
	private ORefList getTimeframeRefs() throws Exception
	{
		return getBaseObject().getSafeRefListData(getTimeframeTag());
	}
	
	private String getTimeframeTag()
	{
		return BaseObject.TAG_TIMEFRAME_IDS;
	}
	
	private BaseObject getBaseObject()
	{
		return baseObject;
	}
	
	private Project getProject()
	{
		return getBaseObject().getProject();
	}
	
	private BaseObject baseObject;
}
