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
package org.miradi.dialogs.base;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.LinkedHashMap;
import java.util.Set;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.martus.swing.UiLabel;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.dialogfields.AnalysisLevelsChooserField;
import org.miradi.dialogfields.CodeListPopupWithDescriptionPanelField;
import org.miradi.dialogfields.CodeToUserStringMapMultiLineEditor;
import org.miradi.dialogfields.DropDownChoiceField;
import org.miradi.dialogfields.EditableCodeListField;
import org.miradi.dialogfields.ExternalProjectsDisplayField;
import org.miradi.dialogfields.IndicatorRelevancyOverrideListField;
import org.miradi.dialogfields.ObjectCheckBoxField;
import org.miradi.dialogfields.ObjectChoiceField;
import org.miradi.dialogfields.ObjectCodeEditorField;
import org.miradi.dialogfields.ObjectCurrencyInputField;
import org.miradi.dialogfields.ObjectDataField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.ObjectDateChooserInputField;
import org.miradi.dialogfields.ObjectExpandingMultilineInputField;
import org.miradi.dialogfields.ObjectFloatingPointRestrictedInputField;
import org.miradi.dialogfields.ObjectIconChoiceField;
import org.miradi.dialogfields.ObjectMultilineDisplayField;
import org.miradi.dialogfields.ObjectMultilineShortDisplayField;
import org.miradi.dialogfields.ObjectOverridenListField;
import org.miradi.dialogfields.ObjectPercentageInputField;
import org.miradi.dialogfields.ObjectRadioButtonGroupField;
import org.miradi.dialogfields.ObjectReadonlyChoiceField;
import org.miradi.dialogfields.ObjectReadonlyObjectListField;
import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogfields.ObjectStringInputField;
import org.miradi.dialogfields.ObjectStringMapInputField;
import org.miradi.dialogfields.PopupQuestionEditorField;
import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogfields.ReadOnlyCodeListField;
import org.miradi.dialogfields.ReadonlyClickableLinkField;
import org.miradi.dialogfields.ReadonlyStaticClickableLinkField;
import org.miradi.dialogfields.SingleCodeEditableField;
import org.miradi.dialogfields.SingleXenodataProjectIdReadonlyField;
import org.miradi.dialogfields.StrategyGoalOverrideListField;
import org.miradi.dialogfields.StrategyObjectiveOverrideListField;
import org.miradi.dialogfields.StringMapProjectResourceFilterEditorField;
import org.miradi.dialogfields.TaxonomyEditorFields;
import org.miradi.dialogfields.WhoEditorField;
import org.miradi.dialogs.fieldComponents.PanelFieldLabel;
import org.miradi.dialogs.fieldComponents.PanelTitleLabel;
import org.miradi.dialogs.fieldComponents.PanelTitledBorder;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.layout.OneColumnPanel;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.main.MainWindow;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.questions.ResourceLeaderQuestionWithUnspecifiedChoice;
import org.miradi.questions.StaticQuestionManager;
import org.miradi.rtf.RtfWriter;
import org.miradi.schemas.GoalSchema;
import org.miradi.schemas.ObjectiveSchema;
import org.miradi.utils.CodeList;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.HtmlUtilities;
import org.miradi.utils.TableExporter;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class AbstractObjectDataInputPanel extends ModelessDialogPanel implements CommandExecutedListener, MiradiTabContentsPanelInterface
{
	public AbstractObjectDataInputPanel(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new ORef[] {orefToUse});
	}
	
	public AbstractObjectDataInputPanel(Project projectToUse, ORef[] orefsToUse)
	{
		project = projectToUse;
		fields = new Vector<ObjectDataField>();
		picker = new Picker();
		subPanels = new Vector<AbstractObjectDataInputPanel>();

		setObjectRefsWithoutUpdatingFields(orefsToUse);
		final int HORIZONTAL_MARGIN = 2;
		final int VERTICAL_MARGIN = 5;
		setBorder(new EmptyBorder(VERTICAL_MARGIN,HORIZONTAL_MARGIN,VERTICAL_MARGIN,HORIZONTAL_MARGIN));
		
		setBackground(AppPreferences.getDataPanelBackgroundColor());
	}
	
	@Override
	public void dispose()
	{
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.dispose();
		}
		
		int fieldSize = getFields().size();
		for(int i = 0; i < fieldSize; ++i)
		{
			ObjectDataField field = getFields().get(i);
			field.dispose();
		}
		
		super.dispose();
	}
	
	@Override
	public void becomeActive()
	{
		super.becomeActive();
		
		project.addCommandExecutedListener(this);
	}
	
	@Override
	public void becomeInactive()
	{
		project.removeCommandExecutedListener(this);
		
		super.becomeInactive();
	}
	
	protected ChoiceQuestion getQuestion(Class questionClass)
	{
		return StaticQuestionManager.getQuestion(questionClass);
	}

	public Project getProject()
	{
		return project;
	}
	
	public MainWindow getMainWindow()
	{
		// TODO: Should have mainWindow passed into the AODIP constructor
		return EAM.getMainWindow();
	}
	
	public ObjectPicker getPicker()
	{
		return picker;
	}

	public void reloadSelectedRefs()
	{
		setObjectRefs(getSelectedRefs().toArray());
	}
	
	public final void setObjectRef(ORef oref)
	{
		setObjectRefs(new ORef[] {oref});
	}
	
	public final void setObjectRefs(ORefList refsToUse)
	{
		setObjectRefs(refsToUse.toArray());
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		saveModifiedFields();
		setObjectRefsWithoutUpdatingFields(orefsToUse);
		updateFieldsFromProject();
	}

	public final void setObjectRefs(ORef[] orefsToUse, String tag)
	{
		setObjectRefs(orefsToUse);
		selectSectionForTag(tag);
	}

	private void setObjectRefsWithoutUpdatingFields(ORef[] orefsToUse)
	{
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.setObjectRefs(orefsToUse);
		}
		selectedRefs = new ORefList(orefsToUse);
		picker.setObjectRefs(selectedRefs.toArray());
	}
	
	public void selectSectionForTag(String tag)
	{
	}
		
	public DisposablePanelWithDescription getTabContentsComponent()
	{
		return this;
	}

	public Icon getIcon()
	{
		return null;
	}

	public String getTabName()
	{
		return getPanelDescription();
	}
	
	public boolean isImageAvailable()
	{
		return false;
	}

	public BufferedImage getImage(int scale)
	{
		return null;
	}
	
	public boolean isExportableTableAvailable()
	{
		return false;
	}
	
	public TableExporter getTableExporter() throws Exception
	{
		return null;
	}
	
	public JComponent getPrintableComponent() throws Exception
	{
		return null;
	}
	
	public boolean isPrintable()
	{
		return false;
	}

	public boolean isRtfExportable()
	{
		return false;
	}
	
	public void exportRtf(RtfWriter writer) throws Exception
	{
	}
	
	public void setFocusOnFirstField()
	{
		//TODO: should be first non read only field.
		if (getFields().size() > 0)
		{
			(getFields().get(0)).getComponent().requestFocusInWindow();
			Rectangle rect = (getFields().get(0)).getComponent().getBounds();
			scrollRectToVisible(rect);
		}
		
		if (getSubPanels().size() > 0)
		{
			AbstractObjectDataInputPanel subPanel = getSubPanels().get(0);
			subPanel.setFocusOnFirstField();
		}
	}
	
	public ObjectDataField addFieldWithoutLabelAlignment(ObjectDataField field)
	{
		addNonAlignedLabel(field.getObjectType(), field.getTag());
		addFieldWithoutLabel(field);

		return field;
	}
		
	public ObjectDataField addField(ObjectDataField field)
	{
		addLabel(field.getObjectType(), field.getTag());
		addFieldWithoutLabel(field);

		return field;
	}
	
	public void addFieldWithoutLabel(ObjectDataField field)
	{
		addFieldToList(field);
		addFieldComponent(field.getComponent());
	}
	
	public void addFieldWithDescriptionOnly(ObjectDataField field, String translatedText)
	{
		addHtmlWrappedLabel(translatedText);
		addTopAlignedLabel(new FillerLabel());
		addFieldWithoutLabel(field);
	}
	
	public ObjectDataField addFieldToList(ObjectDataField field)
	{
		getFields().add(field);
		return field;
	}

	public void addSubPanel(AbstractObjectDataInputPanel subPanel)
	{
		subPanels.add(subPanel);
	}
	
	public void addLabeledSubPanelWithoutBorder(AbstractObjectDataInputPanel subPanel, String labelString)
	{
		PanelTitleLabel label = new PanelTitleLabel(labelString);
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);	
		addSubPanelField(subPanel);
	}
	
	public void addSubPanelField(AbstractObjectDataInputPanel subPanel)
	{
		subPanels.add(subPanel);
		add(subPanel);
	}

	public void addSubPanelWithTitledBorder(AbstractObjectDataInputPanel subPanel)
	{
		CompoundBorder border = createTitledBorder(subPanel.getPanelDescription());
		addSubPanelWithBorder(subPanel, border);
	}
	
	public void addSubPanelWithLineBorder(AbstractObjectDataInputPanel subPanel)
	{
		CompoundBorder border = createTitledBorder("");
		addSubPanelWithBorder(subPanel, border);
	}

	private CompoundBorder createTitledBorder(String borderTitle)
	{
		PanelTitledBorder titledBorder = new PanelTitledBorder(borderTitle);
		final int TOP = 0;
		final int LEFT = 0;
		final int BOTTOM = 10;
		final int RIGHT = 0;
		Border cushion = BorderFactory.createEmptyBorder(TOP, LEFT, BOTTOM, RIGHT);
		
		return BorderFactory.createCompoundBorder(cushion, titledBorder);
	}
	
	protected void addFields(LinkedHashMap<ObjectDataInputField, String> fieldsToLabelMap)
	{
		JPanel fieldPanel = new OneColumnPanel();
		Set<ObjectDataInputField> fieldsToAdd = fieldsToLabelMap.keySet();
		fieldPanel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		for(ObjectDataInputField field : fieldsToAdd)
		{
			addFieldToList(field);
			add(new PanelTitleLabel(fieldsToLabelMap.get(field)));
			add(field.getComponent());
		}
	}		
	
	private void addSubPanelWithBorder(AbstractObjectDataInputPanel subPanel, AbstractBorder border)
	{
		subPanel.setBorder(border);
		addSubPanelWithoutTitledBorder(subPanel);
	}

	public void addSubPanelWithoutTitledBorder(AbstractObjectDataInputPanel subPanel)
	{
		addSubPanel(subPanel);
		add(subPanel);
	}
	
	public ObjectDataInputField createCheckBoxField(String tag)
	{
		return createCheckBoxField(tag, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE);
	}
	
	public ObjectDataInputField createCheckBoxField(String tag, String on, String off)
	{
		return createCheckBoxField(getFirstSelectedRef().getObjectType(), tag, on, off);
	}
	
	public ObjectDataInputField createCheckBoxField(int objectType, String tag, String on, String off)
	{
		ObjectCheckBoxField objectCheckBoxField = new ObjectCheckBoxField(project, getRefForType(objectType), tag, on, off);
		objectCheckBoxField.getComponent().setBackground(AppPreferences.getDataPanelBackgroundColor());
		return objectCheckBoxField;
	}
	
	public ObjectDataInputField createStringField(String tag) throws Exception
	{
		return new ObjectStringInputField(getMainWindow(), getFirstSelectedRef(), tag, 50);
	}
	
	public ObjectDataInputField createStringField(int objectType, String tag) throws Exception
	{
		return new ObjectStringInputField(getMainWindow(), getRefForType(objectType), tag, 50);
	}
	
	public ObjectDataInputField createShortStringField(int objectType, String tag) throws Exception
	{
		return createStringField(objectType, tag, 5);
	}
	
	public ObjectDataInputField createShortStringField(String tag) throws Exception
	{
		return createStringField(tag, 5);
	}
	
	public ObjectDataInputField createMediumStringField(int objectType, String tag) throws Exception
	{
		return createStringField(objectType, tag, 15);
	}
	
	public ObjectDataInputField createMediumStringField(String tag) throws Exception
	{
		return createMediumStringField(getFirstSelectedRef().getObjectType(), tag);
	}
	
	public ObjectDataInputField createSingleStringToRefField(String tag, String mapKeyToUse) throws Exception
	{
		return new SingleXenodataProjectIdReadonlyField(getMainWindow(), getFirstSelectedRef(), tag, mapKeyToUse);
	}
	
	public ObjectDataInputField createExternalProjectIdsDisplayField(String tag) throws Exception
	{
		return new ExternalProjectsDisplayField(getMainWindow(), getFirstSelectedRef(), tag);
	}
	
	public ObjectDataInputField createStringField(String tag, int column) throws Exception
	{
		return new ObjectStringInputField(getMainWindow(), getFirstSelectedRef(), tag, column);
	}
	
	public ObjectDataInputField createStringField(int objectType, String tag, int column) throws Exception
	{
		return new ObjectStringInputField(getMainWindow(), getRefForType(objectType), tag, column);
	}

	public ObjectDataInputField createStringMapField(int objectType, String tag, String code, int length) throws Exception
	{
		return new ObjectStringMapInputField(getMainWindow(), getRefForType(objectType), tag, code, length);
	}
	
	public ObjectDataInputField createStringMapField(ORef refToUse, String tag, String code) throws Exception
	{
		return new CodeToUserStringMapMultiLineEditor(getMainWindow(), refToUse, tag, code);
	}
	
	public ObjectDataInputField createStringCodeListField(ORef refToUse, String tagToUse, String mapKeyCodeToUse, ChoiceQuestion choiceQuestionToUse) throws Exception
	{
		return new CodeToCodeListMapEditorField(getProject(), refToUse, tagToUse, choiceQuestionToUse, mapKeyCodeToUse);
	}
	
	public ObjectDataInputField createDateChooserField(String tag)
	{
		return new ObjectDateChooserInputField(project,  getFirstSelectedRef(), tag);
	}
	
	public ObjectDataInputField createDateChooserField(int objectType, String tag)
	{
		return new ObjectDateChooserInputField(project, getRefForType(objectType), tag);
	}
	
	public ObjectDataInputField createNumericField(String tag, int column) throws Exception
	{
		return new ObjectFloatingPointRestrictedInputField(getMainWindow(), getFirstSelectedRef(), tag, column);
	}
	
	public ObjectDataInputField createCurrencyField(String tag) throws Exception
	{
		return new ObjectCurrencyInputField(getMainWindow(), getFirstSelectedRef(), tag, 10);
	}

	public ObjectDataInputField createNumericField(String tag) throws Exception
	{
		return new ObjectFloatingPointRestrictedInputField(getMainWindow(), getFirstSelectedRef(), tag);
	}
	
	public ObjectDataInputField createNumericField(int objectType, String tag) throws Exception
	{
		return new ObjectFloatingPointRestrictedInputField(getMainWindow(), getRefForType(objectType), tag);
	}

	public ObjectDataInputField createPercentageField(String tag) throws Exception
	{
		return new ObjectPercentageInputField(getMainWindow(), getFirstSelectedRef(), tag);
	}
	
	public ObjectDataInputField createMultilineField(String tag) throws Exception
	{
		return createMultilineField(getFirstSelectedRef().getObjectType(), tag);
	}
	
	public ObjectDataInputField createMultilineField(int objectType, String tag) throws Exception
	{
		return createMultilineField(objectType, tag, DEFAULT_TEXT_COLUM_COUNT);
	}
	
	public ObjectDataInputField createMultilineField(int objectType, String tag, int columns) throws Exception
	{
		return new ObjectScrollingMultilineInputField(getMainWindow(), getRefForType(objectType), tag, columns);
	}
	
	public ObjectDataInputField createExpandableField(String tag) throws Exception
	{
		return createExpandableField(getFirstSelectedRef().getObjectType(), tag);
	}
	
	public ObjectDataInputField createExpandableField(int objectType, String tag) throws Exception
	{
		return createExpandableField(objectType, tag, 25);
	}
	
	public ObjectDataInputField createExpandableField(int objectType, String tag, int columns)  throws Exception
	{
		return new ObjectExpandingMultilineInputField(getMainWindow(), getRefForType(objectType), tag, columns);
	}

	public ObjectOverridenListField createOverridenObjectListField(String tag, ChoiceQuestion question)
	{
		return new ObjectOverridenListField(project, getFirstSelectedRef().getObjectType(), getObjectIdForType(getFirstSelectedRef().getObjectType()), tag, question);
	}
	
	public ObjectDataInputField createIndicatorRelevancyOverrideListField(ChoiceQuestion question)
	{
		return new IndicatorRelevancyOverrideListField(project, getFirstSelectedRef().getObjectType(), getObjectIdForType(getFirstSelectedRef().getObjectType()), question);
	}
	
	public ObjectDataField createGoalRelevancyOverrideListField(int objectType)
	{
		return new StrategyGoalOverrideListField(getProject(), getRefForType(objectType), GoalSchema.getObjectType());
	}
	
	public ObjectDataField createObjectiveRelevancyOverrideListField(int objectType)
	{
		return new StrategyObjectiveOverrideListField(getProject(), getRefForType(objectType), ObjectiveSchema.getObjectType());
	}

	public ObjectDataInputField createSingleColumnCodeListField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return createEditableCodeListField(objectType, tagToUse, question);
	}
	
	public ObjectDataInputField createEditableCodeListField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new EditableCodeListField(project, getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createEditableCodeListField(String tagToUse, ChoiceQuestion question)
	{
		return new EditableCodeListField(project, getFirstSelectedRef(), tagToUse, question);
	}
	
	public ObjectDataInputField createReadOnlyCodeListField(int objctType, String tagToUse, ChoiceQuestion question)
	{
		return new ReadOnlyCodeListField(getProject(), getRefForType(objctType), tagToUse, question);
	}
	
	public ObjectCodeEditorField createMultiCodeField(String tagToUse, ChoiceQuestion question, int columnCount)
	{
		return new ObjectCodeEditorField(project, getFirstSelectedRef(), tagToUse, question, columnCount);
	}
	
	public ObjectDataInputField createMultiCodeEditorField(int objectType, String tagToUse, ChoiceQuestion question, int columnCount)
	{
		return new ObjectCodeEditorField(project, getRefForType(objectType), tagToUse, question, columnCount);
	}

	public ObjectDataInputField createMultiCodeEditorField(String tagToUse, ChoiceQuestion question, CodeList disabledChoices, int columnCount)
	{
		ObjectCodeEditorField objectCodeListField = createMultiCodeField(tagToUse, question, columnCount);
		objectCodeListField.setDisabledCodes(disabledChoices);
		
		return objectCodeListField;
	}
	
	public ObjectDataInputField createConfigureAnalysisRowsField(ORef refToUse, String tagToUse, ChoiceQuestion question)
	{
		return new AnalysisLevelsChooserField(getProject(), refToUse, tagToUse, question);
	}
	
	public ObjectDataInputField createWorkPlanBudgetColumnCodeListEditor(int objectType, String tagToUse, ChoiceQuestion question, String budgetColumnsCodelistKey)
	{
		return new CodeToCodeListMapEditorField(getProject(), getRefForType(objectType), tagToUse, question, budgetColumnsCodelistKey);
	}
	
	public ObjectDataInputField createStringMapProjectResourceFilterCodeListEditor(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new StringMapProjectResourceFilterEditorField(getProject(), objectType, getObjectIdForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createReadonlyTextField(String tag) throws Exception
	{
		return new ObjectMultilineDisplayField(getMainWindow(), getFirstSelectedRef(), tag);
	}
	
	public ObjectDataInputField createReadonlyShortTextField(int objectType, String tag) throws Exception
	{
		return new ObjectMultilineShortDisplayField(getMainWindow(), getRefForType(objectType), tag);
	}
	
	public ObjectDataInputField createReadonlyTextField(int objectType, String tag) throws Exception
	{
		return new ObjectMultilineDisplayField(getMainWindow(), getRefForType(objectType), tag);
	}
	
	public ObjectDataInputField createChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectChoiceField(project, getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createQuestionFieldWithDescriptionPanel(int objectType, String tagToUse, ChoiceQuestion question) throws Exception
	{
		return new CodeListPopupWithDescriptionPanelField(getMainWindow(), getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createRatingChoiceField(String tagToUse, ChoiceQuestion question)
	{
		return new DropDownChoiceField(project,  getFirstSelectedRef(), tagToUse, question);
	}
	
	public ObjectDataInputField createRatingChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new DropDownChoiceField(project, getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createLeaderDropDownField(int objectType, String tagToUse)
	{
		return new ResourceLeaderDropDownField(getProject(), getRefForType(objectType), tagToUse, new ResourceLeaderQuestionWithUnspecifiedChoice(getProject()));
	}
	
	public ObjectDataInputField createDropdownWithIconField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectIconChoiceField(project, getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createRadioButtonEditorField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new SingleCodeEditableField(getMainWindow(), getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createTaxonomyFields(int objectType)
	{
		return new TaxonomyEditorFields(getProject(), objectType);
	}
	
	public ObjectDataInputField createReadOnlyChoiceField(String tagToUse, ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project,  getFirstSelectedRef(), tagToUse, question);
	}
	
	public ObjectDataInputField createReadOnlyChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project, getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createReadOnlyObjectList(int objectType, String tag)
	{
		return new ObjectReadonlyObjectListField(getMainWindow(), getRefForType(objectType), tag, createUniqueIdentifierForTable(objectType, tag)); 
	}

	private String createUniqueIdentifierForTable(int objectType, String tag)
	{
		final String TABLE_TAG = "Table";
		return TABLE_TAG + objectType + tag;
	}

	public ObjectDataInputField createRadioChoiceField(ORef refToUse, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectRadioButtonGroupField(project, refToUse, tagToUse, question);
	}
	
	public ObjectDataInputField createRadioChoiceField(int objectType, String tagToUse, Class questionClass)
	{
		return new ObjectRadioButtonGroupField(project, getRefForType(objectType), tagToUse, getQuestion(questionClass));
	}
	
	public RadioButtonsField createRadioButtonsField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new RadioButtonsField(project, getRefForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createDashboardProgressEditorField(ORef refToUse, String tagToUse, ChoiceQuestion questionToUse, String mapCodeToUse) throws Exception
	{
		return new DashboardProgressEditorField(getProject(), refToUse, tagToUse, questionToUse, mapCodeToUse);
	}
	
	public PopupQuestionEditorField createPopupQuestionEditor(JDialog parentDialog, int objectType, String tagToUse, Class questionClass) throws Exception
	{
		return new PopupQuestionEditorField(parentDialog, getProject(), getRefForType(objectType), tagToUse, StaticQuestionManager.getQuestion(questionClass));
	}
	
	public WhoEditorField createWhoEditorField(ORef refToUse)
	{
		return new WhoEditorField(getMainWindow(), refToUse);
	}
	
	public ObjectDataInputField createReadonlyClickableLinkField(int objectType, String tag)
	{
		return new ReadonlyClickableLinkField(getMainWindow(), getRefForType(objectType), tag);
	}
	
	public ObjectDataField createStaticReadonlyClickableLinkField(int objectType, String htmlText, String tabIdentifier) throws Exception
	{
		return new ReadonlyStaticClickableLinkField(getMainWindow(), getRefForType(objectType), htmlText, tabIdentifier);
	}
	
	public ORef getRefForType(int objectType)
	{
		return new ORef(objectType, getObjectIdForType(objectType));
	}
	
	public BaseId getObjectIdForType(int objectType)
	{
		for (int i=0; i<selectedRefs.size(); ++i)
		{
			int type = selectedRefs.get(i).getObjectType();
			if (objectType == type)
				return  selectedRefs.get(i).getObjectId();
		}
		return BaseId.INVALID;
	}
	
	private ORef getFirstSelectedRef()
	{
		return selectedRefs.getFirstElement();
	}
	
	public void saveModifiedFields()
	{
		for(int i = 0; i < getFields().size(); ++i)
		{
			ObjectDataField field = getFields().get(i);
			field.saveIfNeeded();
		}
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.saveModifiedFields();
		}
	}
	
	public void updateFieldsFromProject()
	{
		setFieldObjectIds();
		for(int i = 0; i < getFields().size(); ++i)
		{
			ObjectDataField field = getFields().get(i);
			field.updateFromObject();
		}
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.updateFieldsFromProject();
		}
	}

	public Vector<ObjectDataField> getFields()
	{
		return fields;
	}
	
	private void setFieldObjectIds()
	{
		for(int i = 0; i < getFields().size(); ++i)
		{
			ObjectDataField field = getFields().get(i);
			field.setObjectRef(getRefForType(field.getObjectType()));
		}
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.setFieldObjectIds();
		}
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		if(wasOurObjectJustDeleted(event))
		{
			CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
			deleteObjectFromList(cmd.getObjectRef());
			setFieldObjectIds();
			return;
		}
		
		if (!getProject().isInTransaction())
		{
			updateFieldsFromProject();
		}
	}
	
	private void deleteObjectFromList(ORef deletedObjectRef)
	{
		selectedRefs.remove(deletedObjectRef);
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.deleteObjectFromList(deletedObjectRef);
		}
	}

	private boolean wasOurObjectJustDeleted(CommandExecutedEvent event)
	{
		if(event.isDeleteObjectCommand())
		{
			CommandDeleteObject deleteCommand = (CommandDeleteObject)event.getCommand();
			return deleteCommand.getObjectRef().equals(getRefForType(deleteCommand.getObjectType()));
		}
		
		return false;
	}

	@Override
	public BaseObject getObject()
	{
		return null;
	}

	public ORefList getSelectedRefs()
	{
		return new ORefList(selectedRefs);
	}
	
	class Picker implements ObjectPicker
	{
		public Picker()
		{
			listeners = new Vector<ListSelectionListener>();
		}

		public ORefList[] getSelectedHierarchies()
		{
			return new ORefList[] {getSelectionHierarchy()};
		}

		public BaseObject[] getSelectedObjects()
		{
			Vector<BaseObject> objects = new Vector<BaseObject>();
			for(int i = 0; i < selectedRefs.size(); ++i)
			{
				final ORef selectedObjectRef = selectedRefs.get(i);
				if (selectedObjectRef.isValid())
					objects.add(getProject().findObject(selectedObjectRef));
			}
			
			return objects.toArray(new BaseObject[0]);
		}

		public ORefList getSelectionHierarchy()
		{
			return new ORefList(selectedRefs);
		}

		public void clearSelection()
		{
		}

		public void ensureOneCopyOfObjectSelectedAndVisible(ORef ref)
		{
		}
		
		public void setObjectRefs(ORef[] refs)
		{
			ListSelectionEvent event = new ListSelectionEvent(this, -1, -1, false);
			for(ListSelectionListener listener : listeners)
			{
				listener.valueChanged(event);
			}
		}

		public void addSelectionChangeListener(ListSelectionListener listener)
		{
			listeners.add(listener);
		}

		public void removeSelectionChangeListener(ListSelectionListener listener)
		{
			listeners.remove(listener);
		}
		
		public void expandTo(int typeToExpandTo) throws Exception
		{
		}
		
		public void expandAll() throws Exception
		{
		}
		
		public void collapseAll() throws Exception
		{	
		}

		public boolean isActive()
		{
			return isActive;
		}
		
		public void becomeActive()
		{
			isActive = true;
		}

		public void becomeInactive()
		{
			isActive = false;
		}

		public void valueChanged(ListSelectionEvent e)
		{
			throw new RuntimeException("Not supported");
		}
		
		public TreeTableNode[] getSelectedTreeNodes()
		{
			throw new RuntimeException("Not supported");
		}

		private Vector<ListSelectionListener> listeners;
		private boolean isActive;
	}

	protected Vector<AbstractObjectDataInputPanel> getSubPanels()
	{
		return subPanels; 
	}
	
	public void addFieldComponent(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.setBackground(AppPreferences.getDataPanelBackgroundColor());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		add(panel);
	}
	
	public void addLabelWithIcon(String translatedString, Icon icon)
	{
		UiLabel label = new PanelTitleLabel(translatedString, icon);
		addTopAlignedLabel(label);
	}

	public UiLabel addHtmlWrappedLabel(String translatedText)
	{
		translatedText = HtmlUtilities.wrapInHtmlTags(translatedText);
		UiLabel label = new PanelTitleLabel(translatedText);
		addTopAlignedLabel(label);
		
		return label;
	}

	public void addLabel(int objectType, String fieldTag)
	{
		UiLabel label = new PanelFieldLabel(objectType, fieldTag);
		addTopAlignedLabel(label);
	}

	public void addTopAlignedLabel(UiLabel label)
	{
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);
	}
	
	public void addNonAlignedLabel(int objectType, String fieldTag)
	{
		UiLabel label = new PanelFieldLabel(objectType, fieldTag);
		add(label);
	}
	
	protected boolean doesSectionContainFieldWithTag(String tagToUse)
	{
		Vector<ObjectDataField> thisFields = getFields();
		for(ObjectDataField field : thisFields)
		{
			if (tagToUse.equals(field.getTag()))
				return true;
		}
		
		return false;
	}

	public static int STD_SHORT = 5;
	public static final int DEFAULT_TEXT_COLUM_COUNT = 50;
	
	private Project project;
	private Picker picker;
	private ORefList selectedRefs;
	private Vector<ObjectDataField> fields;
	private Vector<AbstractObjectDataInputPanel> subPanels;
}

