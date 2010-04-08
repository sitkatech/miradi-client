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

package org.miradi.dialogfields.editors;

import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.utils.DateEditorComponent;

public class DayPanel extends TwoColumnPanel
{
	public DayPanel(DateUnit dateUnit, String title)
	{
		add(new PanelTitleLabel(title));
		dateEditor = new DateEditorComponent();
		add(dateEditor);
		
		setSelectedDateUnit(dateUnit);
	}

	private void setSelectedDateUnit(DateUnit dateUnit)
	{
		if (dateUnit != null && dateUnit.isDay())
			dateEditor.setText(dateUnit.getDateUnitCode());
	}

	public DateUnit getDateUnit()
	{
		return DateUnit.createDayDateUnit(dateEditor.getText()); 
	}
	
	private DateEditorComponent dateEditor;
}
