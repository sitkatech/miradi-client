/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.Factor;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;

public class FactorTagListEditor extends AbstractListComponent implements CommandExecutedListener
{
	public FactorTagListEditor(Project projectToUse, Factor selectedFactorToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(questionToUse, columnCount);
		
		project = projectToUse;
		selectedFactor = selectedFactorToUse;
		project.addCommandExecutedListener(this);
	
		setEnabled();
	}
	
	//FIXME this needs to get called when window is closed.
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	protected void valueChanged(ChoiceItem choiceItem, boolean isSelected) throws Exception
	{
		String refAsCode = choiceItem.getCode();
		ORef taggedObjectSetRef = ORef.createFromString(refAsCode);
		TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);
		ORefList taggedSet = new ORefList(taggedObjectSet.getTaggedObjectRefs());
		taggedSet.remove(getFactorToTag().getRef());
		if (isSelected)
			taggedSet.add(getFactorToTag().getRef());
		
		CommandSetObjectData tagFactorCommand = new CommandSetObjectData(taggedObjectSetRef, TaggedObjectSet.TAG_TAGGED_OBJECT_REFS, taggedSet.toString());
		getProject().executeCommand(tagFactorCommand);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if (event.isSetDataCommandWithThisTypeAndTag(TaggedObjectSet.getObjectType(), TaggedObjectSet.TAG_TAGGED_OBJECT_REFS))
			setEnabled();
	}
	
	public void setEnabled()
	{
		for (int checkBoxIndex = 0; checkBoxIndex < checkBoxes.length; ++checkBoxIndex)
		{
			String label = checkBoxes[checkBoxIndex].getText();
			ChoiceItem choiceItem = getQuestion().findChoiceByLabel(label);
			ORef taggedObjectSetRef = ORef.createFromString(choiceItem.getCode());
			TaggedObjectSet taggedObjectSet = TaggedObjectSet.find(getProject(), taggedObjectSetRef);
			ORefList taggedSet = taggedObjectSet.getTaggedObjectRefs();
			boolean contains = taggedSet.contains(getFactorToTag().getRef());
			checkBoxes[checkBoxIndex].setSelected(contains);
		}
	}
		
	private Project getProject()
	{
		return project;
	}
	
	private Factor getFactorToTag()
	{
		return selectedFactor;
	}
	
	private Project project;
	private Factor selectedFactor;
}
