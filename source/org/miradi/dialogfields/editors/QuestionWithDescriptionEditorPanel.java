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

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogfields.QuestionBasedEditorComponent;
import org.miradi.dialogs.dashboard.LeftSideRightSideSplitterContainerTab;
import org.miradi.dialogs.fieldComponents.PanelCheckBox;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.questions.ChoiceQuestion;

//FIXME this class is under construction and needs full review
public class QuestionWithDescriptionEditorPanel extends LeftSideRightSideSplitterContainerTab implements ListSelectionListener
{
	public QuestionWithDescriptionEditorPanel(MainWindow mainWindowToUse, ChoiceQuestion questionToUse, QuestionBasedEditorComponent editorComponent) throws Exception
	{
		super(mainWindowToUse, null);
		
		TwoColumnPanel leftMainPanel = new TwoColumnPanel();
		
		editorComponent = new QuestionBasedEditorComponent(questionToUse, 1);
		leftMainPanel.add(editorComponent);
		editorComponent.addListSelectionListener(this);

		addLeftPanel(leftMainPanel);
	}		
	
	public void setText(String codeListAsString)
	{
		editorComponent.setText(codeListAsString);
	}

	public String getText()
	{
		return editorComponent.getText();
	}
	
	@Override
	protected String getMainDescriptionFileName()
	{
		return "dashboard/1.html";
	}

	protected JComponent createLeftComponent(String leftColumnTranslatedText)
	{
		return new PanelCheckBox(leftColumnTranslatedText);
	}

	@Override
	public String getPanelDescription()
	{
		return EAM.text("Edit..");
	}

	public void valueChanged(ListSelectionEvent e)
	{
		FieldSaver.savePendingEdits();
	}
	
	private QuestionBasedEditorComponent editorComponent;
}
