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

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.commands.CommandSetThreatRating;
import org.miradi.dialogfields.ChoiceItemListSelectionEvent;
import org.miradi.dialogfields.ThreatStressRatingValueReadonlyComponent;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.layout.TwoColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.Cause;
import org.miradi.objects.Target;
import org.miradi.objects.ValueOption;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.questions.ChoiceItem;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.IrreversibilityThreatRatingQuestion;
import org.miradi.questions.ScopeThreatRatingQuestion;
import org.miradi.questions.SeverityThreatRatingQuestion;
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.QuestionPopupEditorComponent;
import org.miradi.utils.SimpleThreatRatingQuestionPopupEditorComponent;

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
		add(rollupField);
		addEditComponent(scopeEditorComponent);
		addEditComponent(severityEditorComponent);
		addEditComponent(irreversibilityEditorComponent);
		PanelTitleLabel rollupLabel = new PanelTitleLabel(EAM.text("<html><b>Summary<br>Target-Threat<br>Rating"));
		add(createGridCell(rollupLabel, rollupField.getComponent()));
	}

	private void createComponents() throws Exception
	{
		rollupField = new ThreatStressRatingValueReadonlyComponent(getProject());
		scopeEditorComponent = new SimpleThreatRatingQuestionPopupEditorComponent(getProject(), new ListSelectionHandler(getScopeId()), getScopeRatingQuestion(), EAM.text("Scope"));
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
	
	private JComponent createGridCell(JComponent label, JComponent field)
	{
		Box cellPanel = Box.createHorizontalBox();
		cellPanel.add(label);
		cellPanel.add(Box.createHorizontalStrut(5));
		cellPanel.add(field);
		final Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		final Border emptyBorder = BorderFactory.createEmptyBorder(5,5,5,5);
		cellPanel.setBorder(BorderFactory.createCompoundBorder(lineBorder, emptyBorder));
		return cellPanel;
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
		return getProject().getQuestion(ThreatRatingQuestion.class);
	}
	
	private ChoiceQuestion getScopeRatingQuestion()
	{
		return getProject().getQuestion(ScopeThreatRatingQuestion.class);
	}
	
	private ChoiceQuestion getSeverityRatingQuestion()
	{
		return getProject().getQuestion(SeverityThreatRatingQuestion.class);
	}
	
	private ChoiceQuestion getIrreversibilityRatingQuestion()
	{
		return getProject().getQuestion(IrreversibilityThreatRatingQuestion.class);
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
			rollupField.setObjectRefs(getSelectedRefs());
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	private ORef getTargetRef()
	{
		return getRefForType(Target.getObjectType());
	}
	
	private ORef getThreatRef()
	{
		return getRefForType(Cause.getObjectType());
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
		ORef valueOptionRef = new ORef(ValueOption.getObjectType(), bundle.getValueId(criterionId));
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
	
	private ThreatStressRatingValueReadonlyComponent rollupField;
	private SimpleThreatRatingQuestionPopupEditorComponent scopeEditorComponent;
	private SimpleThreatRatingQuestionPopupEditorComponent severityEditorComponent;
	private SimpleThreatRatingQuestionPopupEditorComponent irreversibilityEditorComponent;
}
