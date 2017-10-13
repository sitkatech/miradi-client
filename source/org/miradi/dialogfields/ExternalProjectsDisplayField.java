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

import java.awt.Color;
import java.util.HashMap;
import java.util.Set;

import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Xenodata;

public class ExternalProjectsDisplayField extends ObjectMultilineDisplayField
{
	public ExternalProjectsDisplayField(MainWindow mainWindow, ORef refToUse, String tag) throws Exception
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
			Set<String> externalContextCodes = stringMap.getKeys();
			String projectIds = "";
			for(String externalContextCode : externalContextCodes)
			{
				ORef xenodataRefForKey = stringMap.getValue(externalContextCode);
				Xenodata xenodata = Xenodata.find(getProject(), xenodataRefForKey);
				final String projectCodeWithinExternalContext = xenodata.getData(Xenodata.TAG_PROJECT_ID);
				
				HashMap<String, String> tokenReplacementMap = new HashMap<String, String>();
				tokenReplacementMap.put("%externalContextCode", externalContextCode);
				tokenReplacementMap.put("%projectCodeWithinExternalContext", projectCodeWithinExternalContext);
				
				projectIds += EAM.substitute(EAM.text("%externalContextCode - %projectCodeWithinExternalContext  <br/>"), tokenReplacementMap);
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
