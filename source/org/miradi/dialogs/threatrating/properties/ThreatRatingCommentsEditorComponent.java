/* 
Copyright 2005-2018, Foundations of Success, Bethesda, Maryland
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
package org.miradi.dialogs.threatrating.properties;

import com.inet.jortho.SpellChecker;
import org.miradi.actions.Actions;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.FieldSaver;
import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogfields.SavableField;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractThreatRatingData;
import org.miradi.project.Project;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.utils.EditableHtmlPane;
import org.miradi.utils.HtmlEditorRightClickMouseHandler;
import org.miradi.utils.MiradiScrollPane;

import javax.swing.*;

public class ThreatRatingCommentsEditorComponent extends SavableField
{
	public ThreatRatingCommentsEditorComponent(Project projectToUse, Actions actions) throws Exception
	{
		super();
		
		project = projectToUse;		
		selectedHierarchy = new ORefList();
		panelTextArea = new EditableHtmlPane(EAM.getMainWindow(), AbstractObjectDataInputPanel.DEFAULT_TEXT_COLUMN_COUNT, ObjectScrollingMultilineInputField.INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT);
		
		panelTextArea.setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
		panelTextArea.setBackground(EAM.EDITABLE_BACKGROUND_COLOR);
		
		new HtmlEditorRightClickMouseHandler(actions, panelTextArea);
		panelTextArea.addFocusListener(this);
		
		if(EAM.getMainWindow().isSpellCheckerActive())
			SpellChecker.register(panelTextArea, false, false, true);
	}
	
	public void setObjectRefs(ORefList selectedHierarchyToUse)
	{
		FieldSaver.savePendingEdits();
		selectedHierarchy = selectedHierarchyToUse;

		updateText();
	}

	private void updateText()
	{
		ORef threatRef = getThreatRef();
		ORef targetRef = getTargetRef();
		String comments = getComments(threatRef, targetRef);
		
		getTextArea().setText(comments);
		getTextArea().invalidate();
	}

	private String getComments(ORef threatRef, ORef targetRef)
	{
		AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findThreatRatingData(getProject(), threatRef, targetRef);
		return threatRatingData != null ? threatRatingData.getComments() : "";
	}

	public JComponent getComponent()
	{
		if(scrollPane == null)
			scrollPane = new MiradiScrollPane(getTextArea());
		
		return scrollPane;
	}
	
	private ORef getTargetRef()
	{
		return getSelectedHierarchy().getRefForType(TargetSchema.getObjectType());
	}

	private ORef getThreatRef()
	{
		return getSelectedHierarchy().getRefForType(CauseSchema.getObjectType());
	}
	
	private ORefList getSelectedHierarchy()
	{
		return selectedHierarchy;
	}
	
	private EditableHtmlPane getTextArea()
	{
		return panelTextArea;
	}
		
	public Project getProject()
	{
		return project;
	}
	
	@Override
	public void saveIfNeeded()
	{
		try
		{
			String comments = getTextArea().getText();
			AbstractThreatRatingData threatRatingData = AbstractThreatRatingData.findOrCreateThreatRatingData(getProject(), getThreatRef(), getTargetRef());
			CommandSetObjectData command = new CommandSetObjectData(threatRatingData.getRef(), AbstractThreatRatingData.TAG_COMMENTS, comments);
			getProject().executeCommand(command);
		}
		catch(Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private Project project;
	private MiradiScrollPane scrollPane;
	private EditableHtmlPane panelTextArea;
	private ORefList selectedHierarchy;
}
