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

import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Xenodata;
import org.miradi.project.Project;

public class ConproProjectIdField extends ObjectStringInputField
{
	public ConproProjectIdField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, 50);
		
		setEditable(false);
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
			ORef xenodataRef = stringRefMap.getValue(ProjectMetadata.XENODATA_CONTEXT_CONPRO);
			if (xenodataRef.isInvalid())
				return "";
			
			Xenodata xenodata = Xenodata.find(getProject(), xenodataRef);
			
			return xenodata.getData(Xenodata.TAG_PROJECT_ID);
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
}
