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

import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.StringRefMap;
import org.miradi.objects.Xenodata;

public class SingleStringToRefMapReadonlyField extends ObjectStringInputField
{
	public SingleStringToRefMapReadonlyField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, String mapKeyToUse) throws Exception
	{
		super(mainWindowToUse, refToUse, tagToUse, 30);
		
		mapKey = mapKeyToUse;
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
			ORef xenodataRefForKey = stringMap.getValue(mapKey);
			Xenodata xenodata = Xenodata.find(getProject(), xenodataRefForKey);
			
			super.setText(xenodata.getData(Xenodata.TAG_PROJECT_ID));
		}
		catch (Exception e)
		{
			EAM.alertUserOfNonFatalException(e);
		}
		
		getComponent().setForeground(Color.black);
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
	
	private String mapKey;
}
