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

import org.miradi.diagram.renderers.ComboBoxRenderer;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.planning.ViabilityTableHeader;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.KeyEcologicalAttributeTypeQuestion;
import org.miradi.questions.RatingSourceQuestion;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.questions.StatusConfidenceQuestion;
import org.miradi.questions.ViabilityModeQuestion;

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
		int modelColumn = convertColumnIndexToModel(tableColumn);
		ViabilityViewMainTableModel model = (ViabilityViewMainTableModel) getCastedModel().getCastedModel(modelColumn);
		if (model.isAbstractTargetViabilityModeCell(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(ViabilityModeQuestion.class).getChoices();
			return new ComboBoxRenderer(choices);
		}
		if (model.isKeaAttributeTypeCell(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(KeyEcologicalAttributeTypeQuestion.class).getChoices();
			return new ComboBoxRenderer(choices);
		}
		if (model.isIndicatorRatingSourceColumn(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(RatingSourceQuestion.class).getChoices();
			return new ComboBoxRenderer(choices);
		}
		
		if (model.isMeasurementStatusConfidenceColumn(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(StatusConfidenceQuestion.class).getChoices();
			return new ComboBoxRenderer(choices);
		}

		return super.getCellRenderer(row, tableColumn);
	}
	
	@Override
	public TableCellEditor getCellEditor(int row, int tableColumn)
	{
		int modelColumn = convertColumnIndexToModel(tableColumn);
		ViabilityViewMainTableModel model = (ViabilityViewMainTableModel) getCastedModel().getCastedModel(modelColumn);
		if (model.isAbstractTargetViabilityModeCell(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(ViabilityModeQuestion.class).getChoices();
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(choices);
			return new DefaultCellEditor(comboBox);
		}
		if (model.isKeaAttributeTypeCell(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(KeyEcologicalAttributeTypeQuestion.class).getChoices();
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(choices);
			return new DefaultCellEditor(comboBox);
		}
		if (model.isIndicatorRatingSourceColumn(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(RatingSourceQuestion.class).getChoices();
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(choices);
			return new DefaultCellEditor(comboBox);
		}
		
		if (model.isMeasurementStatusConfidenceColumn(row, modelColumn))
		{
			ChoiceItem[] choices = StaticQuestionManager.getQuestion(StatusConfidenceQuestion.class).getChoices();
			ChoiceItemComboBox comboBox = new ChoiceItemComboBox(choices);
			return new DefaultCellEditor(comboBox);
		}
		
		if (model.isMeasurementThresholdCell(row, modelColumn))
		{
			return getDoubleClickAutoSelectCellEditor();
		}

		if (model.isFutureStatusThresholdCell(row, modelColumn))
		{
			return getDoubleClickAutoSelectCellEditor();
		}
		
		if (model.isCellEditable(row, modelColumn))
		{
			return getDoubleClickAutoSelectCellEditor();
		}
		
		return super.getCellEditor(row, tableColumn);
	}
}
