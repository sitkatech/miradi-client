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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JComponent;
import javax.swing.border.Border;

import org.martus.swing.UiComboBox;
import org.miradi.commands.CommandSetThreatRating;
import org.miradi.dialogfields.ThreatStressRatingValueReadonlyComponent;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.layout.OneRowGridLayout;
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
import org.miradi.questions.ThreatRatingQuestion;

public class SimpleThreatRatingDropdownsPanel extends ObjectDataInputPanel
{
	public SimpleThreatRatingDropdownsPanel(Project projectToUse)
	{
		super(projectToUse, ORef.INVALID);
		setLayout(new OneRowGridLayout());
		
		PanelTitleLabel scopeLabel = new PanelTitleLabel(EAM.text("Scope"));
		scopeComponent = createDropdown(getScopeId());
		PanelTitleLabel severityLabel = new PanelTitleLabel(EAM.text("Severity"));
		severityComponent = createDropdown(getSeverityId());
		PanelTitleLabel irreversibilityLabel = new PanelTitleLabel(EAM.text("Irreversibility"));
		irreversibilityComponent = createDropdown(getIrreversibilityId());
		PanelTitleLabel rollupLabel = new PanelTitleLabel(EAM.text("<html><b>Summary<br>Target-Threat<br>Rating"));
		rollupField = new ThreatStressRatingValueReadonlyComponent(getProject());
		add(rollupField);

		add(createGridCell(scopeLabel, scopeComponent));
		add(createGridCell(severityLabel, severityComponent));
		add(createGridCell(irreversibilityLabel, irreversibilityComponent));
		add(createGridCell(rollupLabel, rollupField.getComponent()));
		
		updateFieldsFromProject();
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

	private ChoiceItemComboBox createDropdown(BaseId criterionId)
	{
		ChoiceQuestion question = getRatingQuestion();
		ChoiceItemComboBox dropdown = new ChoiceItemComboBox(question.getChoices());
		dropdown.addActionListener(new DropdownChangeHandler(criterionId));
		return dropdown;
	}

	private ChoiceQuestion getRatingQuestion()
	{
		return getProject().getQuestion(ThreatRatingQuestion.class);
	}
	
	@Override
	public void updateFieldsFromProject()
	{
		super.updateFieldsFromProject();

		updateDropdownFromProject(getScopeId(), scopeComponent);
		updateDropdownFromProject(getSeverityId(), severityComponent);
		updateDropdownFromProject(getIrreversibilityId(), irreversibilityComponent);
		
		rollupField.setObjectRefs(getSelectedRefs());
	}

	private ORef getTargetRef()
	{
		return getRefForType(Target.getObjectType());
	}
	
	private ORef getThreatRef()
	{
		return getRefForType(Cause.getObjectType());
	}
	
	private void updateDropdownFromProject(BaseId criterionId, UiComboBox dropdown)
	{
		ORef threatRef = getThreatRef();
		ORef targetRef = getTargetRef();
		if(threatRef.isInvalid() || targetRef.isInvalid())
		{
			dropdown.setSelectedIndex(0);
			dropdown.setEnabled(false);
			return;
		}
		
		dropdown.setEnabled(true);

		try
		{
			ThreatRatingBundle bundle = getBundle(threatRef, targetRef);
			ORef valueOptionRef = new ORef(ValueOption.getObjectType(), bundle.getValueId(criterionId));
			ValueOption valueOption = (ValueOption)getProject().findObject(valueOptionRef);
			int numeric = valueOption.getNumericValue();
			String code = "";
			if(numeric > 0)
				code = Integer.toString(numeric);
			ChoiceItem choice = getRatingQuestion().findChoiceByCode(code);
			dropdown.setSelectedItem(choice);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			dropdown.setEnabled(false);
			return;
		}
	}

	private ThreatRatingBundle getBundle(ORef threatRef, ORef targetRef)throws Exception
	{
		return getFramework().getBundle(threatRef, targetRef);
	}

	private SimpleThreatRatingFramework getFramework()
	{
		return getProject().getSimpleThreatRatingFramework();
	}
	
	private class DropdownChangeHandler implements ActionListener
	{
		public DropdownChangeHandler(BaseId criterionIdToUse)
		{
			criterionId = criterionIdToUse;
		}

		public void actionPerformed(ActionEvent event)
		{
			try
			{
				ORef threatRef = getThreatRef();
				ORef targetRef = getTargetRef();
				if(threatRef.isInvalid() || targetRef.isInvalid())
					return;

				ChoiceItemComboBox source = (ChoiceItemComboBox)event.getSource();
				ChoiceItem selected = (ChoiceItem)source.getSelectedItem();
				String selectedCode = selected.getCode();
				int selectedValue = 0;
				if(selectedCode.length() > 0)
					selectedValue = Integer.parseInt(selectedCode);
				
				ValueOption valueOption = getFramework().findValueOptionByNumericValue(selectedValue);
				BaseId valueId = valueOption.getId();
				
				FactorId threatId = (FactorId)threatRef.getObjectId();
				FactorId targetId = (FactorId)targetRef.getObjectId();
				ThreatRatingBundle bundle = getBundle(threatRef, targetRef);
				if(valueId.equals(bundle.getValueId(criterionId)))
					return;
				
				CommandSetThreatRating command = new CommandSetThreatRating(threatId, targetId, criterionId, valueId);
				getProject().executeCommand(command);
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		private BaseId criterionId;
	}

	@Override
	public String getPanelDescription()
	{
		return "SimpleThreatRatingDropdowns";
	}
	
	private ChoiceItemComboBox scopeComponent;
	private ChoiceItemComboBox severityComponent;
	private ChoiceItemComboBox irreversibilityComponent;
	private ThreatStressRatingValueReadonlyComponent rollupField;
}
