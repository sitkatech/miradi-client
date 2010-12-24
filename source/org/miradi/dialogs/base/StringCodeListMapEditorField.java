/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.base;

import org.miradi.objecthelpers.AbstractStringKeyMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringCodeListMap;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
   
public class StringCodeListMapEditorField extends AbstractStringCodeListMapEditorField
{
	public StringCodeListMapEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapKeyCodeToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse, mapKeyCodeToUse);
	}

	@Override
	protected AbstractStringKeyMap createCurrentStringKeyMap() throws Exception
	{
		return new StringCodeListMap(getProject().getObjectData(getORef(), getTag()));
	}

	@Override
	protected AbstractStringKeyMap createStringKeyMap(String StringMapAsString) throws Exception
	{
		return new StringCodeListMap(StringMapAsString);
	}
}
