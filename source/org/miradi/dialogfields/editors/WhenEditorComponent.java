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
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.layout.OneColumnGridLayout;
import org.miradi.objecthelpers.DateUnit;
import org.miradi.utils.CodeList;

public class WhenEditorComponent extends DisposablePanel
{
	public WhenEditorComponent()
	{
		setLayout(new OneColumnGridLayout());

		String[] choices = new String[]{NONE_ITEM, PROJECT_TOTAL_ITEM, YEAR_ITEM, QUARTER_ITEM, MONTH_ITEM, DAY_ITEM,};
		dateUnitTypeCombo = new UiComboBox(choices);
		dateUnitTypeCombo.addItemListener(new ChangeHandler());

		lowerPanel = new WhenEditorLowerPanel();
		add(dateUnitTypeCombo);
		add(lowerPanel);
	}
	
	public CodeList getStartEndCodes() throws Exception
	{
		CodeList startEndCodes = new CodeList();
		if (getStartDateUnit() != null)
			startEndCodes.add(getStartDateUnit().toString());
		
		if (getEndDateUnit() != null)
			startEndCodes.add(getEndDateUnit().toString());
		
		return startEndCodes;
	}
	
	private DateUnit getStartDateUnit() throws Exception
	{
		return lowerPanel.getStartDateUnit();
	}
	
	private DateUnit getEndDateUnit() throws Exception
	{
		return lowerPanel.getEndDateUnit();
	}
	
	private class ChangeHandler implements ItemListener
	{
		public void itemStateChanged(ItemEvent e)
		{
			lowerPanel.showCard(e.getItem().toString());
		}
	}
	
	private WhenEditorLowerPanel lowerPanel;
	private UiComboBox dateUnitTypeCombo;
	
	public static final String NONE_ITEM = "None";
	public static final String PROJECT_TOTAL_ITEM = "Project Total";
	public static final String YEAR_ITEM = "Year";
	public static final String QUARTER_ITEM = "Quarter";
	public static final String MONTH_ITEM = "Month";
	public static final String DAY_ITEM = "Day";
}