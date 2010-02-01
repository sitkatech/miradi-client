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

import org.miradi.dialogs.base.ColumnMarginResizeListenerValidator;
import org.miradi.dialogs.base.EditableObjectTable;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.StressBasedThreatRatingQuestionPopupEditorComponent;

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
				createInvokePopupColumn(threatStressRatingTableModel.getColumnQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isIrreversibilityColumn(modelColumn))
				createInvokePopupColumn(threatStressRatingTableModel.getColumnQuestion(modelColumn), tableColumn);
			
			if (threatStressRatingTableModel.isThreatRatingColumn(modelColumn))
				createReadonlyComboQuestionColumn(threatStressRatingTableModel.createThreatStressRatingQuestion(modelColumn), tableColumn);
		}
	}

	protected void listenForColumnWidthChanges(JTable table)
	{
		table.getColumnModel().addColumnModelListener(new ColumnMarginResizeListenerValidator(this));
	}
	
	@Override
	public int getDefaultColumnWidth(int tableColumn, String columnTag,	int columnHeaderWidth)
	{
		try
		{
			int modelColumn = convertColumnIndexToModel(tableColumn);
			if (getThreatStressRatingTableModel().isContributionColumn(modelColumn))
				return getPreferredEditorComponentWidth(modelColumn);

			if (getThreatStressRatingTableModel().isIrreversibilityColumn(modelColumn))
				return getPreferredEditorComponentWidth(modelColumn);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			EAM.unexpectedErrorDialog(e);
		}	
		
		return super.getDefaultColumnWidth(tableColumn, columnTag, columnHeaderWidth);
	}

	private int getPreferredEditorComponentWidth(int modelColumn) throws Exception
	{
		ChoiceQuestion question = getThreatStressRatingTableModel().getColumnQuestion(modelColumn);
		StressBasedThreatRatingQuestionPopupEditorComponent component = new StressBasedThreatRatingQuestionPopupEditorComponent(getProject(), question);
		return component.getPreferredSize().width;
	}

	public static final String UNIQUE_IDENTIFIER = "ThreatStressRatingTable";
}
