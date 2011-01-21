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

import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ObjectPoolChoiceQuestion;
import org.miradi.utils.MiradiScrollPane;

abstract public class AbstractRelevancyOverrideListField extends ObjectDataField implements ListSelectionListener
{
	public AbstractRelevancyOverrideListField(Project projectToUse,	ORef refToUse, int objectTypeToUpdate)
	{
		super(projectToUse, refToUse);
		refListEditor = new RefListComponent(new ObjectPoolChoiceQuestion(getProject(), objectTypeToUpdate));
		refListEditor.addListSelectionListener(this);
		//TODO Panels that use this component are still needing to place the component into a scroll pane.
		//IF they dont, the list will not be scrollable.  
		refListScroller = new MiradiScrollPane(refListEditor);
	}

	public void refreshRefs()
	{
		refListEditor.getQuestion().reloadQuestion();
		refListEditor.rebuildToggleButtonsBoxes();
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
		//FIXME needs to write to objectives
	}
	
	@Override
	public void updateFromObject()
	{
		//FIXME needs to read from strategy or use TreeRebuilder
	}
	
	@Override
	public String getTag()
	{
		return "";
	}

	protected RefListComponent refListEditor;
	private MiradiScrollPane refListScroller;
}
