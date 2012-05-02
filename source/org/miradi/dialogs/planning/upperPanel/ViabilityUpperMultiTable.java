/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.dialogs.planning.upperPanel;

import javax.swing.DefaultCellEditor;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import org.miradi.diagram.renderers.ChoiceItemComboBoxTableCellRenderer;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.planning.ViabilityTableHeader;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.StaticQuestionManager;

public class ViabilityUpperMultiTable extends PlanningUpperMultiTable
{
	public ViabilityUpperMultiTable(MainWindow mainWindowToUse,	PlanningTreeTable masterTreeToUse, PlanningTreeMultiTableModel model)
	{
		super(mainWindowToUse, masterTreeToUse, model);
	}

	@Override
	protected void setTableHeaderRenderer()
	{
		setTableHeader(new ViabilityTableHeader(this));
	}
	
	@Override
	public TableCellRenderer getCellRenderer(int row, int tableColumn)
	{
		final int modelColumn = convertColumnIndexToModel(tableColumn);
		final ViabilityViewMainTableModel model = (ViabilityViewMainTableModel) getCastedModel().getCastedModel(modelColumn);
		final Class questionClassName = model.getCellQuestion(row, modelColumn);
		if (questionClassName != null)
		{
			ChoiceQuestion question = StaticQuestionManager.getQuestion(questionClassName);
			return new ChoiceItemComboBoxTableCellRenderer(question);
		}

		return super.getCellRenderer(row, tableColumn);
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int tableColumn)
	{
		//FIXME medium this is duplicate code which is also in parent class. Remove the duplications here.
		final int modelColumn = convertColumnIndexToModel(tableColumn);
		final ViabilityViewMainTableModel model = (ViabilityViewMainTableModel) getCastedModel().getCastedModel(modelColumn);
		final Class questionClassName = model.getCellQuestion(row, modelColumn);
		if (questionClassName != null)
		{
			ChoiceQuestion question = StaticQuestionManager.getQuestion(questionClassName);
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(question);
			return new DefaultCellEditor(comboBox);
		}
		
		if (model.isMeasurementThresholdCell(row, modelColumn))
		{
			return getSingleLineTextCellEditorFactory();
		}

		if (model.isFutureStatusThresholdCell(row, modelColumn))
		{
			return getSingleLineTextCellEditorFactory();
		}
		
		if (model.isCellEditable(row, modelColumn))
		{
			return getSingleLineTextCellEditorFactory();
		}
		
		return super.getCellEditor(row, tableColumn);
	}
}
