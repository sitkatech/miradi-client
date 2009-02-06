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
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.fieldComponents.ChoiceItemComboBox;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.ids.BaseId;
import org.miradi.ids.FactorId;
import org.miradi.layout.OneRowGridLayout;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.FactorLink;
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
		ObjectDataInputField rollupField = createReadOnlyChoiceField(FactorLink.getObjectType(), FactorLink.PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, new ThreatRatingQuestion());
		addRawField(rollupField);

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
	}

	private FactorLink getLink()
	{
		ORef linkRef = getRefForType(FactorLink.getObjectType());
		if(linkRef.isInvalid())
			return null;
		FactorLink link = (FactorLink)getProject().findObject(linkRef);
		return link;
	}

	private void updateDropdownFromProject(BaseId criterionId, UiComboBox dropdown)
	{
		FactorLink link = getLink();
		if(link == null)
		{
			dropdown.setSelectedIndex(0);
			dropdown.setEnabled(false);
			return;
		}
		
		dropdown.setEnabled(true);

		try
		{
			ThreatRatingBundle bundle = getBundle(link);
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

	private ThreatRatingBundle getBundle(FactorLink link) throws Exception
	{
		return getFramework().getBundle(link.getUpstreamThreatRef(), link.getDownstreamTargetRef());
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
				FactorLink link = getLink();
				if(link == null)
				{
					return;
				}

				ChoiceItemComboBox source = (ChoiceItemComboBox)event.getSource();
				ChoiceItem selected = (ChoiceItem)source.getSelectedItem();
				String selectedCode = selected.getCode();
				int selectedValue = 0;
				if(selectedCode.length() > 0)
					selectedValue = Integer.parseInt(selectedCode);
				
				FactorId threatId = (FactorId)link.getUpstreamThreatRef().getObjectId();
				FactorId targetId = (FactorId)link.getDownstreamTargetRef().getObjectId();
				ValueOption valueOption = getFramework().findValueOptionByNumericValue(selectedValue);
				BaseId valueId = valueOption.getId();
				ThreatRatingBundle bundle = getBundle(link);
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
}
