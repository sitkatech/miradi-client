/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import org.miradi.ids.BaseId;
import org.miradi.project.Project;

public class ObjectPercentageInputField extends ObjectNumericInputField
{
	public ObjectPercentageInputField(Project project, int objectType, BaseId objectIdForType, String tag)
	{
		super(project, objectType, objectIdForType, tag);
	}

}
