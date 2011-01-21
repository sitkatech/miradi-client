/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogfields;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.planning.upperPanel.rebuilder.TreeRebuilder;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.RelevancyOverrideSet;
import org.miradi.objects.Desire;
import org.miradi.project.Project;
import org.miradi.questions.ObjectPoolChoiceQuestion;
import org.miradi.utils.CommandVector;
import org.miradi.utils.MiradiScrollPane;

abstract public class AbstractRelevancyOverrideListField extends ObjectDataField implements ListSelectionListener
{
	public AbstractRelevancyOverrideListField(Project projectToUse,	ORef refToUse, int objectTypeToUpdate)
	{
		super(projectToUse, refToUse);
		
		desireType = objectTypeToUpdate;
		refListEditor = new RefListComponent(new ObjectPoolChoiceQuestion(getProject(), objectTypeToUpdate));
		refListEditor.addListSelectionListener(this);
		//TODO Panels that use this component are still needing to place the component into a scroll pane.
		//IF they dont, the list will not be scrollable.  
		refListScroller = new MiradiScrollPane(refListEditor);
	}

	@Override
	public JComponent getComponent()
	{
		return refListScroller;
	}

	@Override
	public void updateEditableState()
	{
		refListEditor.setEnabled(true);
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		saveIfNeeded();
	}
	
	@Override
	public void saveIfNeeded()
	{
		CommandVector updateDesireRelevancyRefCommands = new CommandVector();
		try
		{
			ORefList selectedRefs = new ORefList(refListEditor.getText());
			ORefList singleStrategyRefList = new ORefList(getORef());
			for(int index = 0; index < selectedRefs.size(); ++index)
			{
				ORef desireRef = selectedRefs.get(index);
				Desire desire = Desire.findDesire(getProject(), desireRef);
				RelevancyOverrideSet relevantOverrides = desire.getCalculatedRelevantStrategyActivityOverrides(singleStrategyRefList);
				CommandSetObjectData setCommand = new CommandSetObjectData(desire, Desire.TAG_RELEVANT_STRATEGY_ACTIVITY_SET, relevantOverrides.toString());
				updateDesireRelevancyRefCommands.add(setCommand);
			}
			
			getProject().executeCommandsAsTransaction(updateDesireRelevancyRefCommands);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	@Override
	public void updateFromObject()
	{
		try
		{
			ORefList relevantDesireRefs = TreeRebuilder.findRelevantDesires(getProject(), getORef(), desireType);
			refListEditor.setText(relevantDesireRefs.toString());
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	@Override
	public String getTag()
	{
		return "";
	}

	private int desireType;
	private RefListComponent refListEditor;
	private MiradiScrollPane refListScroller;
}
