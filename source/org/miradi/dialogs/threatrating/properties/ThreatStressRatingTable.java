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
package org.miradi.dialogs.threatrating.properties;

import java.awt.Dimension;

import javax.swing.JTable;

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.main.MainWindow;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(MainWindow mainWindowToUse, ThreatStressRatingTableModel threatStressRatingTableModel)
	{
		super(mainWindowToUse, threatStressRatingTableModel, UNIQUE_IDENTIFIER);
		rebuildColumnEditorsAndRenderers();
		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		listenForColumnWidthChanges(this);
		//TODO shouldn't set row height to constant value
		setRowHeight(26);
	}
	
	@Override
	public boolean allowUserToSetRowHeight()
	{
		return false;
	}
	
	@Override
	public Dimension getPreferredScrollableViewportSize()
	{
		Dimension size = new Dimension(getPreferredSize());
		size.height = Math.max(size.height, MINIMUM_VIEWPORT_HEIGHT);
		size.height = Math.min(size.height, MAXIMUM_VIEWPORT_HEIGHT);
		return size;
	}
	
	public ThreatStressRatingTableModel getThreatStressRatingTableModel()
	{
		return (ThreatStressRatingTableModel) getModel();
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		ThreatStressRatingTableModel threatStressRatingTableModel = getThreatStressRatingTableModel();
		for (int tableColumn = 0; tableColumn < threatStressRatingTableModel.getColumnCount(); ++tableColumn)
		{
			int modelColumn = convertColumnIndexToModel(tableColumn);


			if (threatStressRatingTableModel.isStressRatingColumn(modelColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createStressRatingQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isContributionColumn(modelColumn))
				createComboQuestionColumn(threatStressRatingTableModel.createContributionQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isIrreversibilityColumn(modelColumn))
				createComboQuestionColumn(threatStressRatingTableModel.createIrreversibilityQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isThreatRatingColumn(modelColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createThreatStressRatingQuestion(modelColumn), tableColumn);
		}
	}

	protected void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(new ColumnMarginResizeListenerValidator(this));
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
	public static final int MINIMUM_VIEWPORT_HEIGHT = 100;
	public static final int MAXIMUM_VIEWPORT_HEIGHT = 400;
}
