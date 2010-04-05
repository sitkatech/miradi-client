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

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import org.martus.swing.UiComboBox;
import org.miradi.layout.OneColumnPanel;

public class WhenEditorUpperPanel extends OneColumnPanel
{
	public WhenEditorUpperPanel()
	{
		String[] choices = new String[]{YEAR_ITEM, QUARTER_ITEM, MONTH_ITEM, DAY_ITEM,};
		dateUnitTypeCombo = new UiComboBox(choices);
		dateUnitTypeCombo.addItemListener(new ChangeHandler());
		
		add(dateUnitTypeCombo);
	}
	
	class ChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
		}
	}
	
	private UiComboBox dateUnitTypeCombo;
	private static final String YEAR_ITEM = "Year";
	private static final String QUARTER_ITEM = "Quarter";
	private static final String MONTH_ITEM = "Month";
	private static final String DAY_ITEM = "Day";
}
