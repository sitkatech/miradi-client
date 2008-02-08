/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogfields;

import java.awt.Dimension;

import javax.swing.JComponent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.conservationmeasures.eam.utils.CodeList;
import org.conservationmeasures.eam.utils.MiradiScrollPane;

public class ObjectCodeListField extends ObjectDataInputField implements ListSelectionListener
{
	public ObjectCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse)
	{
		this(projectToUse, objectTypeToUse, objectIdToUse, tagToUse, questionToUse, 3);
	}
	
	public ObjectCodeListField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse, ChoiceQuestion questionToUse, int columnCount)
	{
		super(projectToUse, objectTypeToUse, objectIdToUse, tagToUse);
		codeListEditor = new CodeListComponent(questionToUse, columnCount, this);
		component = new MiradiScrollPane(codeListEditor);
		Dimension preferredSize = component.getPreferredSize();
		final int ARBITRARY_REASONABLE_MAX_WIDTH = 800;
		final int ARBITRARY_REASONABLE_MAX_HEIGHT = 200;
		int width = Math.min(preferredSize.width, ARBITRARY_REASONABLE_MAX_WIDTH);
		int height = Math.min(preferredSize.height, ARBITRARY_REASONABLE_MAX_HEIGHT);
		component.getViewport().setPreferredSize(new Dimension(width, height));
	}
	
	public JComponent getComponent()
	{
		return component;
	}

	public String getText()
	{
		return codeListEditor.getText();
	}

	public void setText(String codes)
	{
		codeListEditor.setText(codes);
	}
	
	public void updateEditableState()
	{
		boolean editable = allowEdits() && isValidObject();
		codeListEditor.setEnabled(editable);
	}
	
	public void valueChanged(ListSelectionEvent arg0)
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	public void setDisabledCodes(CodeList codesToDiable)
	{
		codeListEditor.setDisabledCodes(codesToDiable);
	}
	
	CodeListComponent codeListEditor;
	MiradiScrollPane component; 
}
