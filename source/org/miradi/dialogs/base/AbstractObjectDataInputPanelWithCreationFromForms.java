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

package org.miradi.dialogs.base;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogs.fieldComponents.HtmlFormViewer;
import org.miradi.forms.*;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.DataPanelFlexibleWidthHtmlViewer;
import org.miradi.utils.FillerLabel;
import org.miradi.utils.FlexibleWidthHtmlViewer;

abstract public class AbstractObjectDataInputPanelWithCreationFromForms extends	AbstractObjectDataInputPanelWithActivation
{
	public AbstractObjectDataInputPanelWithCreationFromForms(Project projectToUse, ORef[] orefsToUse)
	{
		super(projectToUse, orefsToUse);
		
		GridLayoutPlus layout = new GridLayoutPlus(0, 2, HGAP, VGAP);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		setLayout(layout);
	}

	public AbstractObjectDataInputPanelWithCreationFromForms(Project projectToUse, ORef[] orefsToUse, FieldPanelSpec formToUse) throws Exception
	{
		this(projectToUse, orefsToUse);
		
		createFieldsFromForm(formToUse);

		updateFieldsFromProject();
	}

	protected void createFieldsFromForm(FieldPanelSpec form) throws Exception
	{
		for(int row = 0; row < form.getFieldRowCount(); ++row)
		{
			FormRow formRow = form.getFormRow(row);
			addLabelFromForm(formRow);
			addFieldFromForm(formRow);
		}
	}
	
	protected void addLabelAndFieldFromForm(FormRow formRow) throws Exception
	{
		addLabelFromForm(formRow);
		addFieldFromForm(formRow);
	}

	protected void addLabelFromForm(FormRow formRow)
	{
		if(formRow.getLeftFormItemsCount() != 1)
			throw new RuntimeException(getClass().getName()	+ ": Don't know how to add multiple labels");

		FormItem formItem = formRow.getLeftFormItem(0);
		if(formItem.isFormConstant())
		{
			FormConstant fc = (FormConstant) formItem;
			addHtmlWrappedLabel(fc.getConstant());
			return;
		}
		
		if(!formItem.isFormFieldLabel())
			throw new RuntimeException(getClass().getName()	+ ": Don't know how to add non-label " + formItem);
		
		FormFieldLabel label = (FormFieldLabel) formItem;
		int objectType = label.getObjectType();
		String fieldTag = label.getObjectTag();
		addLabel(objectType, fieldTag);
	}

	protected void addFieldFromForm(FormRow formRow) throws Exception
	{
		if(formRow.getRightFormItemsCount() != 1)
			throw new RuntimeException(getClass().getName()	+ ": Don't know how to add multiple fields");

		FormItem formItem = formRow.getRightFormItem(0);
		if(formItem.isFormConstant())
		{
			FormConstant fc = (FormConstant) formItem;
			String text = fc.getConstant();
			FlexibleWidthHtmlViewer viewer = new DataPanelFlexibleWidthHtmlViewer(getMainWindow(), text);
			addFieldComponent(viewer);
			return;
		}
		
		if(formItem.isExternalProjectIdFieldData())
		{
			FormFieldExternalProjectIdData data = (FormFieldExternalProjectIdData) formItem;
			String fieldTag = data.getObjectTag();
			String externalProjectIdCode = data.getExternalProjectIdCode();
			addFieldWithoutLabel(createSingleStringToRefField(fieldTag, externalProjectIdCode));
			
			return;
		}
		if (formItem.isMultipleTaxonomyWithEditButtonFields())
		{
			FieldRelatedFormItem data = (FieldRelatedFormItem) formItem;
			int objectType = data.getObjectType();

			add(new FillerLabel());
			addTaxonomyFields(objectType);
			return;
		}
		if (formItem.isFormQuestionFieldData())
		{
			AbstractFormFieldQuestionData data = (AbstractFormFieldQuestionData) formItem;
			addFieldWithoutLabel(createChoiceField(data.getObjectType(), data.getObjectTag(), data.getQuestion()));
			
			return;
		}
		if (formItem.isDateFieldData())
		{
			FieldRelatedFormItem data = (FieldRelatedFormItem) formItem;
			addFieldWithoutLabel(createDateChooserField(data.getObjectType(), data.getObjectTag()));
			
			return;
		}
		if (formItem.isDisplayFieldData())
		{
			FormFieldDisplayData data = (FormFieldDisplayData) formItem;
			addFieldWithoutLabel(createReadonlyTextField(data.getObjectType(), data.getObjectTag()));
			return;
		}
		if (formItem.isHtmlPanel())
		{
			FormFieldHtmlStaticText data = (FormFieldHtmlStaticText) formItem;
			add(new HtmlFormViewer(getMainWindow(), data.getHtmlText(), getMainWindow().getHyperlinkHandler()));
			
			return;
		}
		
		if (formItem.isReadonlyClickableLinkField())
		{
			FormFieldReadonlyClickableLinkField data = (FormFieldReadonlyClickableLinkField) formItem;
			addFieldWithoutLabel(createReadonlyClickableLinkField(data.getObjectType(), data.getObjectTag()));
			
			return;
		}
		
		
		if(!formItem.isFormFieldData())
			throw new RuntimeException(getClass().getName()	+ ": Don't know how to add non-field " + formItem);
		
		FormFieldData data = (FormFieldData) formItem;
		int objectType = data.getObjectType();
		String fieldTag = data.getObjectTag();
		int dataEntryType = data.getDataEntryType();
		addFieldWithoutLabel(createDataField(objectType, fieldTag, dataEntryType));
	}

	private ObjectDataInputField createDataField(int objectType, String fieldTag, int dataEntryType) throws Exception
	{
		if(dataEntryType == PropertiesPanelSpec.TYPE_VERY_SHORT_STRING)
			return createShortStringField(objectType, fieldTag);
		
		if(dataEntryType == PropertiesPanelSpec.TYPE_FAIRLY_SHORT_STRING)
			return createMediumStringField(objectType, fieldTag);
		
		if(dataEntryType == PropertiesPanelSpec.TYPE_SINGLE_LINE_STRING)
			return createStringField(objectType, fieldTag);
		
		if(dataEntryType == PropertiesPanelSpec.TYPE_EXPANDING_STRING)
			return createExpandableField(objectType, fieldTag);
		
		if(dataEntryType == PropertiesPanelSpec.TYPE_MULTILINE_STRING)
			return createMultilineField(objectType, fieldTag);

		if(dataEntryType == PropertiesPanelSpec.TYPE_SINGLE_LINE_READONLY_STRING)
			return createReadonlyTextField(objectType, fieldTag);
		
		throw new RuntimeException(getClass().getName()	+ ": Cannot yet create data entry field of type: " + dataEntryType);
	}
	
	static final int HGAP = 6;
	static final int VGAP = 6;
}
