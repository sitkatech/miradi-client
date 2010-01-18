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

package org.miradi.utils;

import javax.swing.event.ListSelectionListener;

import org.miradi.dialogfields.ControlPanelRadioButtonEditorComponent;
import org.miradi.dialogs.threatrating.upperPanel.ThreatNameColumnTableModel;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;

public class ThreatRatingQuestionPopupEditorComponent extends QuestionPopupEditorComponent
{
	public ThreatRatingQuestionPopupEditorComponent(Project projectToUse, ListSelectionListener selectionHandlerToUse, ChoiceQuestion questionToUse, String translatedPopupButtonText, ORef threatRefToUse, ORef targetRefToUse) throws Exception
	{
		super(selectionHandlerToUse, questionToUse, translatedPopupButtonText);
		
		selectionHandler = selectionHandlerToUse;
		project = projectToUse;
		threatRef = threatRefToUse;
		targetRef = targetRefToUse;
	}
	
	@Override
	protected void addAdditionalDescriptionPanel(OneColumnPanel panel)
	{
		ControlPanelFlexibleWidthHtmlViewer htmlArea = new ControlPanelFlexibleWidthHtmlViewer(EAM.getMainWindow(), getThreatTargetTitle());
		panel.add(htmlArea);
	}
	
	@Override
	protected ControlPanelRadioButtonEditorComponent createPopupEditorPanel()
	{
		return new ControlPanelRadioButtonEditorComponent(getQuestion(), selectionHandler);
	}
	
	private String getThreatTargetTitle()
	{
		Cause threat = Cause.find(getProject(), threatRef);
		Target target = Target.find(getProject(), targetRef);
		
		if (threat == null || target== null)
			return "";
		
		return "<html><B>" + getTranslatedPopupButtonText() + "</B><BR>" + threat.toString() + " <B>" +  ThreatNameColumnTableModel.RIGHT_ARROW + "</B> " + target.toString();
	}
	
	private Project getProject()
	{
		return project;
	}
	
	public void setTargetRef(ORef targetRefToUse)
	{
		targetRef = targetRefToUse;
	} 
	
	public void setThreatRef(ORef threatToUse)
	{
		threatRef = threatToUse;
	}
	
	private Project project;
	private ORef threatRef;
	private ORef targetRef;
	private ListSelectionListener selectionHandler;
}
