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
package org.miradi.dialogs.summary;

import javax.swing.table.TableColumn;

import org.miradi.dialogs.base.ObjectPoolTable;
import org.miradi.dialogs.base.ObjectPoolTableModel;
import org.miradi.dialogs.base.ObjectTableModel;
import org.miradi.dialogs.tablerenderers.QuestionPopupEditorTableCellEditorFactory;
import org.miradi.main.MainWindow;
import org.miradi.objects.ProjectResource;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceRoleQuestion;
import org.miradi.utils.CodeList;

public class TeamPoolTable extends ObjectPoolTable
{
	public TeamPoolTable(MainWindow mainWindowToUse, ObjectPoolTableModel modelToUse)
	{
		super(mainWindowToUse, modelToUse);
	}
	
	@Override
	public void rebuildColumnEditorsAndRenderers()
	{
		super.rebuildColumnEditorsAndRenderers();
		
		ObjectTableModel model = getObjectTableModel();
		for (int tableColumn = 0; tableColumn < model.getColumnCount(); ++tableColumn)
		{
			int modelColumn = convertColumnIndexToModel(tableColumn);
			if (model.isCodeListColumn(modelColumn))
				createCodeListColumn(model.getColumnQuestion(modelColumn), modelColumn);
		}
	}
	
	private void createCodeListColumn(ChoiceQuestion columnQuestion, int modelColumn)
	{
		final ChoiceQuestion question = getObjectTableModel().getColumnQuestion(modelColumn);
		CodeList codesToDisable = new CodeList(new String[] {ResourceRoleQuestion.TEAM_MEMBER_ROLE_CODE});
		QuestionPopupEditorTableCellEditorFactory editorFactory = new QuestionPopupEditorTableCellEditorFactory(getMainWindow(), this, ProjectResource.TAG_ROLE_CODES, question, codesToDisable);
		TableColumn column = getColumnModel().getColumn(modelColumn);
		column.setCellEditor(editorFactory);
	}
}
