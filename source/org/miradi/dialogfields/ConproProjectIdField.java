/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
import java.util.Set;

import javax.swing.JComponent;

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;

public class ConproProjectIdField extends ObjectDataInputField
{
	public ConproProjectIdField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
	}
	
	@Override
	public String getText()
	{
		if (getORef().isInvalid())
			return "";
						
		try
		{
			String data = getProject().getObjectData(getORef(), getTag());
			StringRefMap stringRefMap = new StringRefMap(data);
			Set keys = stringRefMap.getKeys();
			String appendedValues = "";
			for(Object key: keys)
			{
				Xenodata xenodata = Xenodata.find(getProject(), stringRefMap.getValue(key.toString()));
				String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);
				appendedValues += projectId + SPLITTER;
			}
			return appendedValues;
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
		//FIXME finish implementation
	}
	
	@Override
	public JComponent getComponent()
	{
		return null;
	}
	
	public static final String SPLITTER = ";";
}
