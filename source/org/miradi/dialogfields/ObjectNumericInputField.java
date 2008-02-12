/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.project.Project;

public class ObjectNumericInputField extends ObjectAdjustableStringInputField
{
	public ObjectNumericInputField(Project projectToUse, int objectType, BaseId objectId, String tag, int column)
	{
		super(projectToUse, objectType, objectId, tag, column);
	}
	 
	public ObjectNumericInputField(Project projectToUse, int objectType, BaseId objectId, String tag)
	{
		this(projectToUse, objectType, objectId, tag, 10);
	}
}
