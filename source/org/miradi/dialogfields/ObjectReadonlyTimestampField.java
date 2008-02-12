/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.project.Project;
import org.miradi.wizard.noproject.FileSystemTreeNode;

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
