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

package org.miradi.dialogfields;

import javax.swing.event.ListSelectionListener;

import org.miradi.dialogfields.editors.SplitterPanelWithStaticRightSideTextPanel;
import org.miradi.dialogs.base.DisposablePanel;
import org.miradi.dialogs.base.ModalDialogWithClose;
import org.miradi.dialogs.base.OneFieldObjectDataInputPanel;
import org.miradi.dialogs.base.RowSelectionListener;
import org.miradi.main.MainWindow;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class SingleCodeEditableField extends AbstractEditableCodeListField
{
	public SingleCodeEditableField(MainWindow mainWindowToUse, ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(mainWindowToUse.getProject(), refToUse, tagToUse, questionToUse, columnCount);
		
		mainWindow = mainWindowToUse;
	}
	
	@Override
	protected AbstractReadOnlyComponent createReadOnlyComponent(ChoiceQuestion questionToUse, int columnCount)
	{
		return new ReadonlyChoiceComponent(questionToUse, columnCount);
	}

	@Override
	protected DisposablePanel createEditorPanel() throws Exception
	{
		RadioButtonEditorComponentWithHierarchicalRows editor = new RadioButtonEditorComponentWithHierarchicalRows(mainWindow, question);
		OneFieldObjectDataInputPanelWithListenerDelegator leftPanel = new OneFieldObjectDataInputPanelWithListenerDelegator(getProject(), getORef(), getTag(), editor);
		if (editor.getQuestion().hasLongDescriptionProvider())
			return new SplitterPanelWithStaticRightSideTextPanel(mainWindow, question, leftPanel);

		return leftPanel;
	}
	
	private class OneFieldObjectDataInputPanelWithListenerDelegator extends OneFieldObjectDataInputPanel  implements RowSelectionListener
	{
		public OneFieldObjectDataInputPanelWithListenerDelegator(Project projectToUse, ORef orefToUse, String tagToUse, RadioButtonEditorComponentWithHierarchicalRows editorToUse)
		{
			super(projectToUse, orefToUse, tagToUse, new ComponentWrapperObjectDataInputField(projectToUse, getORef(), getTag(), editorToUse));
			
			editor = editorToUse;
		}
		
		public void addRowSelectionListener(ListSelectionListener listener)
		{
			editor.getSafeRowSelectionHandler().addSelectionListener(listener);
		}

		public void removeRowSelectionListener(ListSelectionListener listener)
		{
			editor.getSafeRowSelectionHandler().removeSelectionListener(listener);
		}
		
		private RadioButtonEditorComponentWithHierarchicalRows editor;
	}
	
	@Override
	protected void addDialogMainPanel(ModalDialogWithClose dialog, DisposablePanel editorPanel)
	{
		dialog.setMainPanel(editorPanel);
	}
	
	private MainWindow mainWindow;
}
