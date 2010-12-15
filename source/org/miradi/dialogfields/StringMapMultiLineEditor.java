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

package org.miradi.dialogfields;

import java.text.ParseException;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringMap;

public class StringMapMultiLineEditor extends ObjectScrollingMultilineInputField
{
	public StringMapMultiLineEditor(MainWindow mainWindow, ORef refToUse, String tagToUse, String codeToUse) throws Exception
	{
		super(mainWindow, refToUse, tagToUse, 50);
		
		code = codeToUse;
	}
	
	@Override
	public String getText()
	{
		if (getORef().isInvalid())
			return "";
			
		try
		{
			return getUpdateObjectStringMap(super.getText());
		}
		catch(ParseException e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
			return "";
		}
	}

	private String getUpdateObjectStringMap(String newValue) throws ParseException
	{
		String data = getProject().getObjectData(getORef(), getTag());
		StringMap stringMap = new StringMap(data);
		stringMap.put(code, newValue);

		return stringMap.toString();
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			StringMap stringMap = new StringMap(newValue);

			super.setText(stringMap.get(code));
		}
		catch (Exception e)
		{
			EAM.unexpectedErrorDialog(e);
			EAM.logException(e);
		}
	}

	@Override
	protected void setTextFromPopup(String textFronPopupEditor)
	{
		try
		{
			setText(getUpdateObjectStringMap(textFronPopupEditor));
		}
		catch (Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}
	}
	
	private String code;
}
