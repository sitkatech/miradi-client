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
package org.miradi.questions;

import org.miradi.objects.BaseObject;
import org.miradi.project.Project;

import java.util.Arrays;
import java.util.Vector;

abstract class ObjectPoolIdChoiceQuestionWithUnspecifiedChoice extends ObjectPoolChoiceQuestion
{
	public ObjectPoolIdChoiceQuestionWithUnspecifiedChoice(Project projectToUse, int typeToUse)
	{
		super(projectToUse, typeToUse);
	}

	@Override
	public ChoiceItem[] getChoices()
	{
		ChoiceItem[] baseChoices = getBaseObjectIdChoices();
		return createChoiceItemListWithUnspecifiedItem(baseChoices);
	}

	private ChoiceItem[] getBaseObjectIdChoices()
	{
		Vector<ChoiceItem> choiceItems = new Vector<ChoiceItem>();
		BaseObject[] baseObjects = getObjects();

		for (int i = 0; i < baseObjects.length; ++i)
		{
			BaseObject thisObject = baseObjects[i];
			choiceItems.add(new ChoiceItem(thisObject.getRef().getObjectId().toString(), getStringToDisplay(thisObject)));
		}

		ChoiceItem[] sortedChoiceItems = choiceItems.toArray(new ChoiceItem[0]);
		Arrays.sort(sortedChoiceItems);

		return sortedChoiceItems;
	}
}
