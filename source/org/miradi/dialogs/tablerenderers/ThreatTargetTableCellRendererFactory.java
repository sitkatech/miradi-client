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
package org.miradi.dialogs.tablerenderers;

import javax.swing.Icon;
import javax.swing.JTable;

import org.miradi.main.AppPreferences;
import org.miradi.objects.BaseObject;
import org.miradi.objects.FactorLink;
import org.miradi.questions.ChoiceItem;

public class ThreatTargetTableCellRendererFactory extends ThreatRatingTableCellRendererFactory
{
	public ThreatTargetTableCellRendererFactory(AppPreferences preferences,	RowColumnBaseObjectProvider providerToUse, FontForObjectTypeProvider fontProviderToUse)
	{
		super(preferences, providerToUse, fontProviderToUse);
	}
	
	protected Icon getConfiguredIcon(JTable table, int row, int modelColumn, ChoiceItem choice)
	{
		BaseObject object = getObjectProvider().getBaseObjectForRowColumn(row, modelColumn);
		if(choice == null || object == null)
		{
			return null;
		}
		
		if(object.getProject().isStressBaseMode())
		{
			stressBasedIcon.setColor(choice.getColor());
			return stressBasedIcon;
		}

		if (FactorLink.is(object))
		{
			simpleIcon.setLink((FactorLink)object);
			return simpleIcon;
		}
		
		stressBasedIcon.setColor(choice.getColor());
		return stressBasedIcon;
	}
}
