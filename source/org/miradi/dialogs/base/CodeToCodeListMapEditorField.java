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

import java.text.ParseException;

import org.miradi.objecthelpers.CodeToCodeListMap;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
   
public class CodeToCodeListMapEditorField extends AbstractCodeToCodeListMapEditorField
{
	public CodeToCodeListMapEditorField(Project projectToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapKeyCodeToUse)
	{
		super(projectToUse, refToUse, tagToUse, questionToUse, mapKeyCodeToUse);
	}

	@Override
	protected CodeToCodeListMap createCurrentStringKeyMap() throws Exception
	{
		String mapAsString = getProject().getObjectData(getORef(), getTag());
		return new CodeToCodeListMap(mapAsString);
	}

	@Override
	protected CodeToCodeListMap createStringKeyMap(String StringMapAsString) throws Exception
	{
		return new CodeToCodeListMap(StringMapAsString);
	}
	
	@Override
	protected void put(CodeToCodeListMap existingMap, String key, String value) throws ParseException
	{
		CodeToCodeListMap map = existingMap;
		CodeList codeList = new CodeList(value);
		map.putCodeList(key, codeList);
	}
}
