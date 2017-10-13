/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.project.Project;
import org.miradi.questions.ObjectPoolChoiceQuestion;
import org.miradi.utils.CommandVector;
import org.miradi.utils.MiradiScrollPane;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

abstract public class AbstractRelevancyOverrideListField extends ObjectDataField implements ListSelectionListener
{
	public AbstractRelevancyOverrideListField(Project projectToUse,	ORef refToUse, int objectTypeToUpdate)
	{
		super(projectToUse, refToUse);
		
		objectType = objectTypeToUpdate;
		relevantObjectRefListEditor = new RefListComponent(new ObjectPoolChoiceQuestion(getProject(), objectTypeToUpdate));
		relevantObjectRefListEditor.addListSelectionListener(this);
		//TODO Panels that use this component are still needing to place the component into a scroll pane.
		//IF they dont, the list will not be scrollable.  
		refListScroller = new MiradiScrollPane(relevantObjectRefListEditor);
	}

	@Override
	public JComponent getComponent()
	{
		return refListScroller;
	}

	@Override
	public void updateEditableState()
	{
		relevantObjectRefListEditor.setEnabled(shouldBeEditable());
	}

	@Override
	protected boolean shouldBeEditable()
	{
		return true;
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		saveIfNeeded();
	}

	@Override
	public void saveIfNeeded()
	{
		try
		{
			CommandVector updateObjectRelevancyRefCommands = new CommandVector();
			ORefList selectedObjectRefs = new ORefList(relevantObjectRefListEditor.getText());
			ORefList allObjects = getProject().getPool(objectType).getORefList();
			for(int index = 0; index < allObjects.size(); ++index)
			{
				ORef objectRef = allObjects.get(index);
				updateObjectRelevancyRefCommands.addAll(getCommandsToEnsureProperRelevancy(selectedObjectRefs, objectRef));
			}
			
			getProject().executeCommands(updateObjectRelevancyRefCommands);
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}

	@Override
	public void updateFromObject()
	{
		try
		{
			ORefList relevantObjectsRefs = findAllRelevantObjects(objectType);
			relevantObjectRefListEditor.setText(relevantObjectsRefs.toString());
		}
		catch(Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}
	
	@Override
	public String getTag()
	{
		return "";
	}

	protected abstract CommandVector getCommandsToEnsureProperRelevancy(ORefList selectedObjectRefs, ORef objectRef) throws Exception;
	protected abstract ORefList findAllRelevantObjects(int objectType) throws Exception;

	private int objectType;
	private RefListComponent relevantObjectRefListEditor;
	private MiradiScrollPane refListScroller;
}
