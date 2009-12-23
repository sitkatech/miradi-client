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
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.Utilities;
import org.miradi.commands.CommandSetThreatRating;
import org.miradi.dialogfields.SingleItemSelectableCodeListEditorComponent;
import org.miradi.dialogfields.ThreatStressRatingValueReadonlyComponent;
import org.miradi.dialogs.base.ObjectDataInputPanel;
import org.miradi.dialogs.base.UndecoratedModelessDialogWithClose;
import org.miradi.dialogs.fieldComponents.PanelButton;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.icons.RatingIcon;
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
import org.miradi.questions.ThreatRatingQuestion;
import org.miradi.utils.CodeList;

public class SimpleThreatRatingDropdownsPanel extends ObjectDataInputPanel
{
	public SimpleThreatRatingDropdownsPanel(Project projectToUse) throws Exception
	{
		super(projectToUse, ORef.INVALID);
		setLayout(new OneRowGridLayout());
		
		PanelTitleLabel rollupLabel = new PanelTitleLabel(EAM.text("<html><b>Summary<br>Target-Threat<br>Rating"));
		rollupField = new ThreatStressRatingValueReadonlyComponent(getProject());
		add(rollupField);

		scopeEditorComponent = createEditorComponent(getScopeId());
		addEditComponent(scopeEditorComponent, EAM.text("Scope"));
		
		severityEditorComponent = createEditorComponent(getSeverityId());
		addEditComponent(severityEditorComponent, EAM.text("Severity"));
		
		irreversibilityEditorComponent = createEditorComponent(getIrreversibilityId());
		addEditComponent(irreversibilityEditorComponent, EAM.text("Irreversibility"));
		
		add(createGridCell(rollupLabel, rollupField.getComponent()));
		
		updateFieldsFromProject();
	}

	private PanelButton createEditorComponent(BaseId baseId)
	{
		PanelButton editorComponent = new PanelButton("");
		editorComponent.addActionListener(new PopUpEditorHandler(baseId));
		return editorComponent;
	}

	private void addEditComponent(JComponent component, String translatedText)
	{
		TwoColumnPanel panel = new TwoColumnPanel();
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panel.setBorder(BorderFactory.createEtchedBorder());
		PanelTitleLabel label = new PanelTitleLabel(translatedText);
		panel.add(label);
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
	
	@Override
	public void updateFieldsFromProject()
	{
		super.updateFieldsFromProject();

		try
		{
			updateRatingComponent(getScopeId(), scopeEditorComponent);
			updateRatingComponent(getSeverityId(), severityEditorComponent);
			updateRatingComponent(getIrreversibilityId(), irreversibilityEditorComponent);
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
		
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
		
	private void updateRatingComponent(BaseId criterionId, PanelButton ratingComponent) throws Exception
	{
		ChoiceItem choice = getCurrentRating(criterionId);
		ratingComponent.setIcon(new RatingIcon(choice));
		ratingComponent.setText(choice.getLabel());
	}
	
	private ChoiceItem getCurrentRating(BaseId criterionId) throws Exception
	{
		ThreatRatingBundle bundle = getBundle(getThreatRef(), getTargetRef());
		ORef valueOptionRef = new ORef(ValueOption.getObjectType(), bundle.getValueId(criterionId));
		ValueOption valueOption = (ValueOption)getProject().findObject(valueOptionRef);
		int numeric = valueOption.getNumericValue();
		String code = "";
		if(numeric > 0)
			code = Integer.toString(numeric);
		
		return getRatingQuestion().findChoiceByCode(code);
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
	
	private class PopUpEditorHandler implements ActionListener
	{
		public PopUpEditorHandler(BaseId criterionIdToUse)
		{
			criterionId = criterionIdToUse;
		}
		
		public void actionPerformed(ActionEvent event)
		{
			SingleItemSelectableCodeListEditorComponent editorPanel = new SingleItemSelectableCodeListEditorComponent(getRatingQuestion(), new ListSelectionHandler(criterionId));
			selectRating(editorPanel);
			
			editorDialog = new UndecoratedModelessDialogWithClose(EAM.getMainWindow(), EAM.text("Select")); 
			editorDialog.add(editorPanel);
			editorDialog.pack();
			Utilities.centerFrame(editorDialog);
			editorDialog.setVisible(true);	
		}

		private void selectRating(SingleItemSelectableCodeListEditorComponent editorPanel)
		{
			try
			{
				String code = getCurrentRating(criterionId).getCode();
				CodeList codeList = new CodeList();
				codeList.add(code);
				editorPanel.setText(codeList.toString());
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}
		
		private BaseId criterionId;
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
				closeEditorDialog();
			}
			catch(Exception e)
			{
				EAM.logException(e);
			}
		}

		private void closeEditorDialog()
		{
			editorDialog.setVisible(false);
		}

		private void saveRating(ListSelectionEvent event) throws Exception
		{
			ORef threatRef = getThreatRef();
			ORef targetRef = getTargetRef();
			if(threatRef.isInvalid() || targetRef.isInvalid())
				return;

			String selectedCode = (String) event.getSource();
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
	private PanelButton scopeEditorComponent;
	private PanelButton severityEditorComponent;
	private PanelButton irreversibilityEditorComponent;
	private UndecoratedModelessDialogWithClose editorDialog;
}
