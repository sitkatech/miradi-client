/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

import java.awt.Color;
import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Xenodata;

public class StringToRefMapDisplayField extends ObjectMultilineDisplayField
{
	public StringToRefMapDisplayField(MainWindow mainWindow, ORef refToUse, String tag) throws Exception
	{
		super(mainWindow, refToUse, tag);
	}

	@Override
	public String getText()
	{
		return "";
	}
	
	@Override
	public void setText(String newValue)
	{
		try
		{
			StringRefMap stringMap = new StringRefMap(newValue);
			Set<String> projectIdsAsKeys = stringMap.getKeys();
			String projectIds = "";
			for(String key : projectIdsAsKeys)
			{
				ORef xenodataRefForKey = stringMap.getValue(key);
				Xenodata xenodata = Xenodata.find(getProject(), xenodataRefForKey);
				final String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);
				projectIds += key + " - " + projectId + "<br/>";
			}
			
			super.setText(projectIds);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		getComponent().setForeground(Color.black);
	}
}
