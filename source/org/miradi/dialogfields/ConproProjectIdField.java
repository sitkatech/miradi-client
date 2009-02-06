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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;
import org.miradi.xml.conpro.ConProMiradiXml;

public class ConproProjectIdField extends ObjectStringInputField
{
	public ConproProjectIdField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 50);
		
		stringRefMap = new StringRefMap();
		setEditable(false);
	}
	
	@Override
	public String getText()
	{
		return stringRefMap.toString();
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			stringRefMap.set(newValue);
			ORef xenodataRef = stringRefMap.getValue(ConProMiradiXml.CONPRO_CONTEXT);
			Xenodata xenodata = Xenodata.find(getProject(), xenodataRef);
			String data = xenodata.getData(Xenodata.TAG_PROJECT_ID);
			field.setText(data);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private StringRefMap stringRefMap;
}
