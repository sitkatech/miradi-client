/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.dialogfields;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;

public class ObjectMultiCodeInputField extends ObjectCodeListField
{
	public ObjectMultiCodeInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse,  objectTypeToUse,  objectIdToUse,  tagToUse);
	}
}
