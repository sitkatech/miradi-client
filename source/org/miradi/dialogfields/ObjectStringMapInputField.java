/* 
Copyright 2005-2017, Foundations of Success, Bethesda, Maryland
on behalf of the Conservation Measures Partnership ("CMP").
Material developed between 2005-2013 is jointly copyright by Beneficent Technology, Inc. ("The Benetech Initiative"), Palo Alto, California.

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

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.CodeToUserStringMap;
import org.miradi.objecthelpers.ORef;

public class ObjectStringMapInputField extends ObjectStringInputField
{
	public ObjectStringMapInputField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, String codeToUse, int columnsToUse) throws Exception
	{
		super(mainWindowToUse, refToUse, tagToUse, columnsToUse);
		
		code = codeToUse;
	}
	
	@Override
	public String getText()
	{
		if (getORef().isInvalid())
			return "";
						
		try
		{
			String mapAsString = getProject().getObjectData(getORef(), getTag());
			CodeToUserStringMap stringMap = new CodeToUserStringMap(mapAsString);
			stringMap.putUserString(code, super.getText());
			
			return stringMap.toJsonString();
		}
		catch(ParseException e)
		{
			EAM.alertUserOfNonFatalException(e);
			return "";
		}
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			CodeToUserStringMap stringMap = new CodeToUserStringMap(newValue);
			String value = stringMap.getUserString(code);
			super.setText(value);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
	}
	
	private String code;
}
