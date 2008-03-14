/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class ObjectOverridenListField extends RelevancyOverrideListField
{
	public ObjectOverridenListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, questionToUse, tagToUse);
	}
	
	public String getText()
	{
		try
		{
			return new ORefList(refListEditor.getText()).toString();
		}
		catch(Exception e)
		{
			EAM.logException(e);
			return "";
		}
	}

	public void setText(String codes)
	{
		try
		{
			BaseObject foundObject = getProject().findObject(getORef());
			ORefList refList = new ORefList(foundObject.getData(getTag()));
			refListEditor.setText(refList.toString());
		}
		catch(Exception e)
		{
			//FIXME do something else with this exception
			EAM.logException(e);
		}	
	}
}
