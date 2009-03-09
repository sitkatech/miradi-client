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

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JComponent;

import org.miradi.actions.Actions;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogfields.TextAreaRightClickMouseHandler;
import org.miradi.dialogfields.UndoRedoKeyHandler;
import org.miradi.dialogs.base.AbstractObjectDataInputPanel;
import org.miradi.dialogs.base.MiradiPanel;
import org.miradi.dialogs.fieldComponents.PanelTextArea;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.StringMap;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatRatingCommentsData;
import org.miradi.project.Project;
import org.miradi.utils.MiradiScrollPane;

public class ThreatRatingCommentsEditorComponent extends MiradiPanel
{
	public ThreatRatingCommentsEditorComponent(Project projectToUse, Actions actions)
	{
		super();
		
		project = projectToUse;		
		panelTextArea = new PanelTextArea(ObjectScrollingMultilineInputField.INITIAL_MULTI_LINE_TEXT_AREA_ROW_COUNT, AbstractObjectDataInputPanel.DEFAULT_TEXT_COLUM_COUNT);
		panelTextArea.setLineWrap(true);
		panelTextArea.setWrapStyleWord(true);
		
		panelTextArea.setForeground(EAM.EDITABLE_FOREGROUND_COLOR);
		panelTextArea.setBackground(EAM.EDITABLE_BACKGROUND_COLOR);
		
		new TextAreaRightClickMouseHandler(actions, panelTextArea);
		panelTextArea.addKeyListener(new UndoRedoKeyHandler(actions));
		panelTextArea.addFocusListener(new FocusHandler());
	}
	
	public void setObjectRefs(ORefList selectedHeirearchyToUse)
	{
		selectedHeirearchy = selectedHeirearchyToUse;

		updateText();
	}

	private void updateText()
	{
		ORef threatRef = getThreatRef();
		ORef targetRef = getTargetRef();
		String comment = getThreatRatingCommentsData().findComment(threatRef, targetRef);
		
		getTextArea().setText(comment);
		getTextArea().invalidate();
	}

	private ThreatRatingCommentsData getThreatRatingCommentsData()
	{
		return getProject().getSingletonThreatRatingCommentsData();
	}

	public JComponent getComponent()
	{
		if(scrollPane == null)
			scrollPane = new MiradiScrollPane(getTextArea());
		
		return scrollPane;
	}
	
	private ORef getTargetRef()
	{
		return getSeletedHeirarchy().getRefForType(Target.getObjectType());
	}

	private ORef getThreatRef()
	{
		return getSeletedHeirarchy().getRefForType(Cause.getObjectType());
	}
	
	private ORefList getSeletedHeirarchy()
	{
		if (selectedHeirearchy == null)
			return new ORefList();
		
		return selectedHeirearchy;
	}
	
	private PanelTextArea getTextArea()
	{
		return panelTextArea;
	}
		
	public Project getProject()
	{
		return project;
	}
	
	class FocusHandler implements FocusListener
	{
		public void focusGained(FocusEvent e)
		{
		}

		public void focusLost(FocusEvent e)
		{
			saveCommentsText();
		}
		
		private void saveCommentsText()
		{
			try
			{
				ThreatRatingCommentsData threatRatingCommentsData = getThreatRatingCommentsData();
				StringMap commentsMap = threatRatingCommentsData.getThreatRatingCommentsMap();
				String threatTargetKey = ThreatRatingCommentsData.createKey(getThreatRef(), getTargetRef());
				commentsMap.add(threatTargetKey, getTextArea().getText());
				CommandSetObjectData setComment = new CommandSetObjectData(threatRatingCommentsData.getRef(), threatRatingCommentsData.getThreatRatingCommentsMapTag(), commentsMap.toString());
				getProject().executeCommand(setComment);
			}
			catch (Exception  e)
			{
				EAM.logException(e);
			}
		}
	}

	private Project project;
	private MiradiScrollPane scrollPane;
	private PanelTextArea panelTextArea;
	private ORefList selectedHeirearchy;
}
