/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, "CMP") and 
Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 

This file is part of Miradi

Miradi is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License version 3, 
as published by the Free Software Foundation.

Miradi is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Miradi.  If not, see <http://www.gnu.org/licenses/>. 
*/ 

package org.miradi.dialogfields;

import org.miradi.dialogs.base.AbstractCodeCodeListMapEditorField;
import org.miradi.objecthelpers.AbstractStringToStringMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.CodeToStringMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class CodeStringMapEditorField extends AbstractCodeCodeListMapEditorField
{
	public CodeStringMapEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapCodeToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse, mapCodeToUse);
	}

	@Override
	protected AbstractStringToStringMap createCurrentStringKeyMap() throws Exception
	{
		return new CodeToStringMap(getProject().getObjectData(getORef(), getTag()));
	}

	@Override
	protected AbstractStringToStringMap createStringKeyMap(String StringMapAsString) throws Exception
	{
		return new CodeToStringMap(StringMapAsString);
	}
}
