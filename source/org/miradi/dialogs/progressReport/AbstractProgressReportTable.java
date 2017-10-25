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

package org.miradi.dialogs.progressReport;

import org.miradi.dialogs.base.DynamicWidthEditableObjectTable;
import org.miradi.dialogs.base.EditableObjectTableModel;
import org.miradi.main.MainWindow;
import org.miradi.questions.ProgressReportLongStatusQuestion;

abstract public class AbstractProgressReportTable extends DynamicWidthEditableObjectTable
{
	public AbstractProgressReportTable(MainWindow mainWindowToUse, EditableObjectTableModel modelToUse) throws Exception
	{
		super(mainWindowToUse, modelToUse);		
	}
	
	protected AbstractProgressReportTableModel getProgressReportTableModel()
	{
		return (AbstractProgressReportTableModel) getModel();
	}

	@Override
	protected void setFixedRowHeight()
	{
	}

	@Override
	public void rebuildColumnEditorsAndRenderers()
	{
		AbstractProgressReportTableModel progressReportTableModel = getProgressReportTableModel();
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
}