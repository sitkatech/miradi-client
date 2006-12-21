/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;

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
