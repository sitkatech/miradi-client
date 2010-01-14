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
package org.miradi.dialogs.threatrating.properties;

import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.main.MainWindow;
import org.miradi.objects.BaseObject;

public class ThreatStressRatingTable extends EditableObjectTable
{
	public ThreatStressRatingTable(MainWindow mainWindowToUse, ThreatStressRatingTableModel threatStressRatingTableModel) throws Exception
	{
		super(mainWindowToUse, threatStressRatingTableModel, UNIQUE_IDENTIFIER);
		
		rebuildColumnEditorsAndRenderers();
		listenForColumnWidthChanges(this);
		//TODO shouldn't set row height to constant value
		setRowHeight(26);
	}
	
	@Override
	public boolean allowUserToSetRowHeight()
	{
		return false;
	}
	
	public ThreatStressRatingTableModel getThreatStressRatingTableModel()
	{
		return (ThreatStressRatingTableModel) getModel();
	}
	
	public void rebuildColumnEditorsAndRenderers() throws Exception
	{
		ThreatStressRatingTableModel threatStressRatingTableModel = getThreatStressRatingTableModel();
		for (int tableColumn = 0; tableColumn < threatStressRatingTableModel.getColumnCount(); ++tableColumn)
		{
			int modelColumn = convertColumnIndexToModel(tableColumn);
			if (threatStressRatingTableModel.isStressRatingColumn(modelColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createStressRatingQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isContributionColumn(modelColumn))
				createInvokePopupColumn(new ListSelectionHandler(), threatStressRatingTableModel.createContributionQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isIrreversibilityColumn(modelColumn))
				createInvokePopupColumn(new ListSelectionHandler(), threatStressRatingTableModel.createIrreversibilityQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isThreatRatingColumn(modelColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createThreatStressRatingQuestion(modelColumn), tableColumn);
		}
	}

	protected void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(new ColumnMarginResizeListenerValidator(this));
	}
	
	private class ListSelectionHandler implements ListSelectionListener
	{
		public void valueChanged(ListSelectionEvent event)
		{
			int selectedColumn = getSelectedColumn();
			int selectedRow = getSelectedRow();
			
			int modelColumn = convertColumnIndexToModel(selectedColumn);
			int modelRow = convertRowIndexToModel(selectedRow);
			BaseObject baseObject = getThreatStressRatingTableModel().getBaseObjectForRowColumn(modelRow, modelColumn);
			String columnTag = getThreatStressRatingTableModel().getColumnTag(selectedColumn);
			String threatRatingCode = event.getSource().toString();
			getThreatStressRatingTableModel().setValueUsingCommand(baseObject.getRef(), columnTag, threatRatingCode);
		}
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
}
