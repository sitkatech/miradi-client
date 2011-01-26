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

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
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
		relevantDesireRefListEditor = new RefListComponent(new ObjectPoolChoiceQuestion(getProject(), objectTypeToUpdate));
		relevantDesireRefListEditor.addListSelectionListener(this);
		//TODO Panels that use this component are still needing to place the component into a scroll pane.
		//IF they dont, the list will not be scrollable.  
		refListScroller = new MiradiScrollPane(relevantDesireRefListEditor);
	}

	@Override
	public JComponent getComponent()
	{
		return refListScroller;
	}

	@Override
	public void updateEditableState()
	{
		relevantDesireRefListEditor.setEnabled(true);
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
			CommandVector updateDesireRelevancyRefCommands = new CommandVector();
			ORefList selectedDesireRefs = new ORefList(relevantDesireRefListEditor.getText());
			ORefList allDesires = getProject().getPool(desireType).getORefList();
			for(int index = 0; index < allDesires.size(); ++index)
			{
				ORef desireRef = allDesires.get(index);
				updateDesireRelevancyRefCommands.addAll(getCommandsToEnsureProperRelevancy(selectedDesireRefs, desireRef));
			}
			
			getProject().executeCommandsAsTransaction(updateDesireRelevancyRefCommands);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}

	private CommandVector getCommandsToEnsureProperRelevancy(ORefList selectedDesireRefs, ORef desireRef) throws Exception
	{
		Desire desire = Desire.findDesire(getProject(), desireRef);
		if (selectedDesireRefs.contains(desireRef))
			return  desire.createCommandsToEnsureStrategyIsRelevant(getORef());
		
		return  desire.createCommandsToEnsureFactorIsIrrelevant(getORef());
	}
	
	@Override
	public void updateFromObject()
	{
		try
		{
			ORefList relevantDesireRefs = Desire.findAllRelevantDesires(getProject(), getORef(), desireType);
			relevantDesireRefListEditor.setText(relevantDesireRefs.toString());
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
	private RefListComponent relevantDesireRefListEditor;
	private MiradiScrollPane refListScroller;
}
