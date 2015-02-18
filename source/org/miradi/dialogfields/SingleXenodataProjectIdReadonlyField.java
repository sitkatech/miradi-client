/* 
Copyright 2005-2015, Foundations of Success, Bethesda, Maryland
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
import java.text.ParseException;

import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Xenodata;

public class SingleXenodataProjectIdReadonlyField extends ObjectStringInputField
{
	public SingleXenodataProjectIdReadonlyField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, String xenodataKeyToUse) throws Exception
	{
		super(mainWindowToUse, refToUse, tagToUse, 30);
		
		xenodataKey = xenodataKeyToUse;
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
			final String projectId = extractProjectId(newValue);
			
			super.setText(projectId);
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		getComponent().setForeground(Color.black);
	}

	private String extractProjectId(String stringRefMapAsString) throws ParseException
	{
		StringRefMap stringRefMap = new StringRefMap(stringRefMapAsString);
		ORef xenodataRefForKey = stringRefMap.getValue(xenodataKey);
		if (xenodataRefForKey.isInvalid())
			return "";
		
		Xenodata xenodata = Xenodata.find(getProject(), xenodataRefForKey);
		final String projectId = xenodata.getData(Xenodata.TAG_PROJECT_ID);
		return projectId;
	}
	
	@Override
	public void updateEditableState()
	{
		super.updateEditableState();

		((PanelTextArea)getComponent()).setDisabledTextColor(Color.BLACK);
	}

	@Override
	protected boolean shouldBeEditable()
	{
		return false;
	}
	
	private String xenodataKey;
}
