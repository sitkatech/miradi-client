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

import org.miradi.commands.CommandSetThreatRating;
import org.miradi.dialogfields.ChoiceItemListSelectionEvent;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.ValueOption;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.*;
import org.miradi.schemas.CauseSchema;
import org.miradi.schemas.TargetSchema;
import org.miradi.schemas.ValueOptionSchema;
import org.miradi.utils.QuestionPopupEditorComponent;
import org.miradi.utils.SimpleThreatRatingQuestionPopupEditorComponent;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class SimpleThreatRatingDropdownsPanel extends ObjectDataInputPanel
{
	public SimpleThreatRatingDropdownsPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ORef.INVALID);

		setLayout(new OneRowGridLayout());
		createComponents();
		addComponents();
		updateFieldsFromProject();
	}
	
	@Override
	public void dispose()
	{
		disposeComponent(scopeEditorComponent);
		disposeComponent(severityEditorComponent);
		disposeComponent(irreversibilityEditorComponent);
		
		super.dispose();
	}
	
	private void disposeComponent(QuestionPopupEditorComponent componentToDispose)
	{
		if (componentToDispose != null)
		{
			componentToDispose.dispose();
			componentToDispose = null;
		}
	}

	private void addComponents()
	{
		addEditComponent(scopeEditorComponent);
		addEditComponent(severityEditorComponent);
		addEditComponent(irreversibilityEditorComponent);
	}

	private void createComponents() throws Exception
	{
		scopeEditorComponent = new SimpleThreatRatingQuestionPopupEditorComponent(getProject(), new ListSelectionHandler(getScopeId()), getScopeRatingQuestion(), EAM.text("Threat|Scope"));
		severityEditorComponent = new SimpleThreatRatingQuestionPopupEditorComponent(getProject(), new ListSelectionHandler(getSeverityId()), getSeverityRatingQuestion(), EAM.text("Severity"));
		irreversibilityEditorComponent = new SimpleThreatRatingQuestionPopupEditorComponent(getProject(), new ListSelectionHandler(getIrreversibilityId()), getIrreversibilityRatingQuestion(), EAM.text("Irreversibility"));
	}

	private void addEditComponent(JComponent component)
	{
		TwoColumnPanel panel = new TwoColumnPanel();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panel.setBorder(BorderFactory.createEtchedBorder());
		panel.add(component);
		add(panel);
	}

	private BaseId getScopeId()
	{
		return getFramework().getScopeCriterion().getId();
	}

	private BaseId getSeverityId()
	{
		return getFramework().getSeverityCriterion().getId();
	}

	private BaseId getIrreversibilityId()
	{
		return getFramework().getIrreversibilityCriterion().getId();
	}

	private ChoiceQuestion getRatingQuestion()
	{
		return StaticQuestionManager.getQuestion(ThreatRatingQuestion.class);
	}
	
	private ChoiceQuestion getScopeRatingQuestion()
	{
		return StaticQuestionManager.getQuestion(ScopeThreatRatingQuestion.class);
	}
	
	private ChoiceQuestion getSeverityRatingQuestion()
	{
		return StaticQuestionManager.getQuestion(SeverityThreatRatingQuestion.class);
	}
	
	private ChoiceQuestion getIrreversibilityRatingQuestion()
	{
		return StaticQuestionManager.getQuestion(IrreversibilityThreatRatingQuestion.class);
	}
	
	@Override
	public void updateFieldsFromProject()
	{
		super.updateFieldsFromProject();

		try
		{
			updateRatingComponent(getScopeRatingQuestion(), getScopeId(), scopeEditorComponent);
			updateRatingComponent(getRatingQuestion(), getSeverityId(), severityEditorComponent);
			updateRatingComponent(getRatingQuestion(), getIrreversibilityId(), irreversibilityEditorComponent);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	@Override
	public void setEnabled(boolean isEditable)
	{
		scopeEditorComponent.setEnabled(isEditable);
		severityEditorComponent.setEnabled(isEditable);
		irreversibilityEditorComponent.setEnabled(isEditable);
	}

	private ORef getTargetRef()
	{
		return getRefForType(TargetSchema.getObjectType());
	}
	
	private ORef getThreatRef()
	{
		return getRefForType(CauseSchema.getObjectType());
	}
		
	private void updateRatingComponent(ChoiceQuestion questionToUse, BaseId criterionId, SimpleThreatRatingQuestionPopupEditorComponent ratingComponent) throws Exception
	{
		ChoiceItem choice = getCurrentRating(questionToUse, criterionId);
		ratingComponent.setCode(choice.getCode());
		ratingComponent.setThreatRef(getThreatRef());
		ratingComponent.setTargetRef(getTargetRef());
	}
	
	private ChoiceItem getCurrentRating(ChoiceQuestion questionToUse, BaseId criterionId) throws Exception
	{
		ThreatRatingBundle bundle = getBundle(getThreatRef(), getTargetRef());
		ORef valueOptionRef = new ORef(ValueOptionSchema.getObjectType(), bundle.getValueId(criterionId));
		ValueOption valueOption = (ValueOption)getProject().findObject(valueOptionRef);
		int numeric = valueOption.getNumericValue();
		
		return questionToUse.findChoiceByNumericValue(numeric);
	}

	private ThreatRatingBundle getBundle(ORef threatRef, ORef targetRef)throws Exception
	{
		return getFramework().getBundle(threatRef, targetRef);
	}

	private SimpleThreatRatingFramework getFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	@Override
	public String getPanelDescription()
	{
		return "SimpleThreatRatingDropdowns";
	}
	
	private class ListSelectionHandler implements ListSelectionListener
	{
		public ListSelectionHandler(BaseId criterionIdToUse)
		{
			criterionId = criterionIdToUse;
		}

		public void valueChanged(ListSelectionEvent event)
		{
			try
			{
				saveRating(event);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}

		private void saveRating(ListSelectionEvent event) throws Exception
		{
			ORef threatRef = getThreatRef();
			ORef targetRef = getTargetRef();
			if(threatRef.isInvalid() || targetRef.isInvalid())
				return;

			String selectedCode = ((ChoiceItemListSelectionEvent)event).getCode();
			int selectedValue = 0;
			if(selectedCode.length() > 0)
				selectedValue = Integer.parseInt(selectedCode);
			
			ValueOption valueOption = getFramework().findValueOptionByNumericValue(selectedValue);
			BaseId valueId = valueOption.getId();
			ThreatRatingBundle bundle = getBundle(threatRef, targetRef);
			if(valueId.equals(bundle.getValueId(criterionId)))
				return;
			
			FactorId threatId = (FactorId)threatRef.getObjectId();
			FactorId targetId = (FactorId)targetRef.getObjectId();
			CommandSetThreatRating command = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
			getProject().executeCommand(command);
		}
		
		private BaseId criterionId;
	}

	private SimpleThreatRatingQuestionPopupEditorComponent scopeEditorComponent;
	private SimpleThreatRatingQuestionPopupEditorComponent severityEditorComponent;
	private SimpleThreatRatingQuestionPopupEditorComponent irreversibilityEditorComponent;
}
