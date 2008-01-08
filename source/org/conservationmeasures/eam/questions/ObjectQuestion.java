/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.questions;

import java.util.Vector;

import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

abstract public class ObjectQuestion extends StaticChoiceQuestion
{
	public ObjectQuestion(Project project, int type, String questionLabel)
	{
		super("", questionLabel, getObjectChoices(project, type));
	}

	static ChoiceItem[] getObjectChoices(Project project, int type)
	{
		Vector choiceItems = new Vector();
		ORefList objectRefs = project.getPool(type).getORefList();
		for (int i = 0; i < objectRefs.size(); ++i)
		{
			BaseObject thisObject = project.findObject(objectRefs.get(i));
			choiceItems.add(new ChoiceItem(thisObject.getRef().toString(), thisObject.getLabel()));
		}
		
		return (ChoiceItem[]) choiceItems.toArray(new ChoiceItem[0]);
	}
}
