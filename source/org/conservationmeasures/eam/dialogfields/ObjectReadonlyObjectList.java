/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.project.Project;

public class ObjectReadonlyObjectList extends ObjectMultilineInputField
{
	public ObjectReadonlyObjectList(Project projectToUse, int objectTypeToUse, BaseId idToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, idToUse, tagToUse, COLUMNS);
	}

	public String getText()
	{
		return null;
	}

	public void setText(String newValue)
	{
		try
		{
			ORefList orefList = new ORefList(newValue);
			
			String names = "";
			for (int i = 0; i < orefList.size(); ++i)
			{
				BaseObject object = project.findObject(orefList.get(i)); 
				
				if(i > 0)
					names += "\n";
				names += object.toString();
			}
			super.setText(names);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public boolean allowEdits()
	{
		return false;
	}


	// FIXME: We really don't want to set the number of columns here,
	// so we probably need to change this to use a list widget 
	// instead of a text area
	static final int COLUMNS = 40;
}
