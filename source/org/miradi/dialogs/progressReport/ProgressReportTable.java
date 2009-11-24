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

package org.miradi.dialogs.progressReport;

import javax.swing.JTable;

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.dialogs.base.ProgressReportTableModel;
import org.miradi.main.MainWindow;
import org.miradi.questions.ProgressReportLongStatusQuestion;

public class ProgressReportTable extends EditableObjectTable
{
	public ProgressReportTable(MainWindow mainWindowToUse, EditableObjectTableModel modelToUse)
	{
		super(mainWindowToUse, modelToUse, modelToUse.getUniqueTableModelIdentifier());
		
		rebuildColumnEditorsAndRenderers();
		
		listenForColumnWidthChanges(this);
		//TODO shouldn't set row height to constant value
		setRowHeight(26);
	}
	
	private ProgressReportTableModel getProgressReportTableModel()
	{
		return (ProgressReportTableModel) getModel();
	}
	
	public void rebuildColumnEditorsAndRenderers()
	{
		ProgressReportTableModel progressReportTableModel = getProgressReportTableModel();
		for (int tableColumn = 0; tableColumn < progressReportTableModel.getColumnCount(); ++tableColumn)
		{
			int modelColumn = convertColumnIndexToModel(tableColumn);
			if (progressReportTableModel.isProgressStatusColumn(modelColumn))
				createComboQuestionColumn(new ProgressReportLongStatusQuestion(), tableColumn);
			if (progressReportTableModel.isDateColumn(modelColumn))
				createDateColumn(tableColumn);
			if (progressReportTableModel.isDetailsColumn(modelColumn))
				createWrappableTextFieldColumn(tableColumn);
		}
	}

	protected void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(new ColumnMarginResizeListenerValidator(this));
	}
}
