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

import java.text.ParseException;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringMap;
import org.miradi.project.Project;

public class ObjectStringMapInputField extends ObjectStringInputField
{
	public ObjectStringMapInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, String codeToUse, int columnsToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, columnsToUse);
		
		code = codeToUse;
	}
	
	@Override
	public String getText()
	{
		if (getORef().isInvalid())
			return "";
						
		try
		{
			String data = getProject().getObjectData(getORef(), getTag());
			StringMap stringMap = new StringMap(data);
			stringMap.add(code, super.getText());
			
			return stringMap.toString();
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			return "";
		}
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			StringMap stringMap = new StringMap(newValue);
			String value = stringMap.get(code);
			super.setText(value);
		}
		catch (Exception e)
		{
			//FIXME when fixing other setText fixmes, fix this as well
			EAM.logException(e);
		}
	}
	
	private String code;
}
