/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.questions;

import java.util.Arrays;
import java.util.Vector;

import org.miradi.objects.BaseObject;

abstract public class ObjectQuestion extends DynamicChoiceQuestion
{
	public ObjectQuestion(BaseObject[] objectsToUse)
	{
		objects = objectsToUse;
	}
	
	@Override
	public ChoiceItem[] getChoices()
	{
		Vector choiceItems = new Vector();
		for (int i = 0; i < objects.length; ++i)
		{
			BaseObject thisObject = objects[i];
			choiceItems.add(new ChoiceItem(thisObject.getRef().toString(), getStringToDisplay(thisObject)));
		}
		
		ChoiceItem[] sortedChoiceItems = (ChoiceItem[]) choiceItems.toArray(new ChoiceItem[0]);
		Arrays.sort(sortedChoiceItems);
		
		return sortedChoiceItems;
	}

	protected String getStringToDisplay(BaseObject thisObject)
	{
		return thisObject.combineShortLabelAndLabel();
	}
	
	private BaseObject[] objects;
}
