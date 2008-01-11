/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.wizard.noproject.FileSystemTreeNode;

public class ObjectReadonlyTimestampField extends ObjectStringInputField
{
	public ObjectReadonlyTimestampField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 20);
		setEditable(false);
	}
	
	public void setText(String newValue)
	{
		try
		{
			long millis = Long.parseLong(newValue);
			if(millis > 0)
			{
				super.setText(FileSystemTreeNode.timestampToString(millis));
				return;
			}
		}
		catch(NumberFormatException e)
		{
			EAM.logStackTrace();
		}

		super.setText(UNKNOWN);
	}

	private final static String UNKNOWN = EAM.text("(Unknown)");
}
