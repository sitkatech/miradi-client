/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogs.base;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.miradi.commands.CommandDeleteObject;
import org.miradi.dialogfields.IndicatorRelevancyOverrideListField;
import org.miradi.dialogfields.ObjectCheckBoxField;
import org.miradi.dialogfields.ObjectChoiceField;
import org.miradi.dialogfields.ObjectClassificationChoiceField;
import org.miradi.dialogfields.ObjectCodeListDisplayField;
import org.miradi.dialogfields.ObjectCodeListField;
import org.miradi.dialogfields.ObjectCurrencyInputField;
import org.miradi.dialogfields.ObjectDataInputField;
import org.miradi.dialogfields.ObjectDateChooserInputField;
import org.miradi.dialogfields.ObjectExpandingMultilineInputField;
import org.miradi.dialogfields.ObjectIconChoiceField;
import org.miradi.dialogfields.ObjectMultilineDisplayField;
import org.miradi.dialogfields.ObjectNumericInputField;
import org.miradi.dialogfields.ObjectPercentageInputField;
import org.miradi.dialogfields.ObjectRadioButtonGroupField;
import org.miradi.dialogfields.ObjectRaitingChoiceField;
import org.miradi.dialogfields.ObjectReadonlyChoiceField;
import org.miradi.dialogfields.ObjectReadonlyObjectList;
import org.miradi.dialogfields.ObjectReadonlyTimestampField;
import org.miradi.dialogfields.ObjectScrollingMultilineInputField;
import org.miradi.dialogfields.ObjectStringInputField;
import org.miradi.dialogfields.ObjectStringMapInputField;
import org.miradi.dialogfields.RadioButtonsField;
import org.miradi.dialogfields.StrategyRelevancyOverrideListField;
import org.miradi.dialogs.treetables.TreeTableNode;
import org.miradi.ids.BaseId;
import org.miradi.main.AppPreferences;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.objectdata.BooleanData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.project.Project;
import org.miradi.questions.ChoiceQuestion;
import org.miradi.utils.CodeList;
import org.miradi.utils.ExportableTableInterface;
import org.miradi.views.MiradiTabContentsPanelInterface;
import org.miradi.views.umbrella.ObjectPicker;

abstract public class AbstractObjectDataInputPanel extends ModelessDialogPanel implements CommandExecutedListener, MiradiTabContentsPanelInterface
{
	
	public AbstractObjectDataInputPanel(Project projectToUse, int objectType, BaseId idToUse)
	{
		this(projectToUse,new ORef(objectType, idToUse));
	}
	
	
	public AbstractObjectDataInputPanel(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new ORef[] {orefToUse});
	}
	
	
	public AbstractObjectDataInputPanel(Project projectToUse, ORef[] orefsToUse)
	{
		project = projectToUse;
		fields = new Vector();
		picker = new Picker();
		subPanels = new Vector<AbstractObjectDataInputPanel>();

		setObjectRefsWithoutUpdatingFields(orefsToUse);
		project.addCommandExecutedListener(this);
		final int HORIZONTAL_MARGIN = 10;
		final int VERTICAL_MARGIN = 5;
		setBorder(new EmptyBorder(VERTICAL_MARGIN,HORIZONTAL_MARGIN,VERTICAL_MARGIN,HORIZONTAL_MARGIN));
		
		setBackground(AppPreferences.getDataPanelBackgroundColor());

	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.dispose();
		}
		
		int fieldSize = getFields().size();
		for(int i = 0; i < fieldSize; ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField) getFields().get(i);
			field.dispose();
		}
		
		super.dispose();
	}

	public Project getProject()
	{
		return project;
	}
	
	public ObjectPicker getPicker()
	{
		return picker;
	}
	
	public void setObjectRef(ORef oref)
	{
		setObjectRefs(new ORef[] {oref});
	}
	
	public void setObjectRefs(ORefList refsToUse)
	{
		setObjectRefs(refsToUse.toArray());
	}
	
	public void setObjectRefs(ORef[] orefsToUse)
	{
		saveModifiedFields();
		setObjectRefsWithoutUpdatingFields(orefsToUse);
		updateFieldsFromProject();
	}


	private void setObjectRefsWithoutUpdatingFields(ORef[] orefsToUse)
	{
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.setObjectRefs(orefsToUse);
		}
		orefs = orefsToUse;
		picker.setObjectRefs(orefs);
	}
	
	public DisposablePanel getTabContentsComponent()
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

	public BufferedImage getImage()
	{
		return null;
	}
	
	public boolean isExportableTableAvailable()
	{
		return false;
	}
	
	public ExportableTableInterface getExportableTable() throws Exception
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

	public void setFocusOnFirstField()
	{
		//TODO: should be first non read only field.
		if (getFields().size() > 0)
		{
			((ObjectDataInputField)getFields().get(0)).getComponent().requestFocusInWindow();
			Rectangle rect = ((ObjectDataInputField) getFields().get(0)).getComponent().getBounds();
			scrollRectToVisible(rect);
		}
	}
		
	public ObjectDataInputField addField(ObjectDataInputField field)
	{
		getFields().add(field);
		return field;
	}

	abstract public void addFieldComponent(Component component);
	
	public void addSubPanel(AbstractObjectDataInputPanel subPanel)
	{
		subPanels.add(subPanel);
	}
	
	public void addSubPanelWithTitledBorder(ObjectDataInputPanel subPanel)
	{
		subPanel.setBorder(BorderFactory.createTitledBorder(subPanel.getPanelDescription()));
		addSubPanel(subPanel);
		add(subPanel);
	}
	
	public ObjectDataInputField createCheckBoxField(String tag)
	{
		return createCheckBoxField(tag, BooleanData.BOOLEAN_TRUE, BooleanData.BOOLEAN_FALSE);
	}
	
	public ObjectDataInputField createCheckBoxField(String tag, String on, String off)
	{
		return new ObjectCheckBoxField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, on, off);
	}
	
	public ObjectDataInputField createCheckBoxField(int objectType, String tag, String on, String off)
	{
		return new ObjectCheckBoxField(project, objectType, getObjectIdForType(objectType), tag, on, off);
	}
	
	
	public ObjectDataInputField createStringField(String tag)
	{
		return new ObjectStringInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, 50);
	}
	
	public ObjectDataInputField createStringField(int objectType, String tag)
	{
		return new ObjectStringInputField(project, objectType, getObjectIdForType(objectType), tag, 50);
	}
	
	public ObjectDataInputField createShortStringField(int objectType, String tag)
	{
		return createStringField(objectType, tag, 5);
	}
	
	public ObjectDataInputField createShortStringField(String tag)
	{
		return createStringField(tag, 5);
	}
	
	public ObjectDataInputField createMediumStringField(String tag)
	{
		return createStringField(tag, 15);
	}
	
	
	public ObjectDataInputField createStringField(String tag, int column)
	{
		return new ObjectStringInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, column);
	}
	
	public ObjectDataInputField createStringField(int objectType, String tag, int column)
	{
		return new ObjectStringInputField(project, objectType, getObjectIdForType(objectType), tag, column);
	}

	public ObjectDataInputField createStringMapField(int objectType, String tag, String code)
	{
		return new ObjectStringMapInputField(project, objectType, getObjectIdForType(objectType), tag, code, 20);
	}
		
	public ObjectDataInputField createDateChooserField(String tag)
	{
		return new ObjectDateChooserInputField(project,  getORef(0).getObjectType(), getObjectIdForType( getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createDateChooserField(int objectType, String tag)
	{
		return new ObjectDateChooserInputField(project, objectType, getObjectIdForType(objectType), tag);
	}
	
	public ObjectDataInputField createReadonlyTimestampField(String tag)
	{
		return new ObjectReadonlyTimestampField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag);
	}
	
	
	public ObjectDataInputField createNumericField(String tag, int column)
	{
		return new ObjectNumericInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, column);
	}
	
	public ObjectDataInputField createNumericField(int objectType, String tag, int column)
	{
		return new ObjectNumericInputField(project, objectType, getObjectIdForType(objectType), tag, column);
	}

	public ObjectDataInputField createCurrencyField(int objectType, String tag)
	{
		return new ObjectCurrencyInputField(project, objectType, getObjectIdForType(objectType), tag, 10);
	}
	
	public ObjectDataInputField createCurrencyField(String tag)
	{
		return new ObjectCurrencyInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, 10);
	}

	public ObjectDataInputField createReadonlyCurrencyField(String tag)
	{
		ObjectCurrencyInputField field = new ObjectCurrencyInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, 10);
		field.setEditable(false);
		return field;
	}

	public ObjectDataInputField createNumericField(String tag)
	{
		return new ObjectNumericInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createNumericField(int objectType, String tag)
	{
		return new ObjectNumericInputField(project, objectType, getObjectIdForType(objectType), tag);
	}

	public ObjectDataInputField createPercentageField(String tag)
	{
		return new ObjectPercentageInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createMultilineField(String tag)
	{
		return new ObjectScrollingMultilineInputField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag, 50);
	}
	
	public ObjectDataInputField createMultilineField(int objectType, String tag)
	{
		return new ObjectScrollingMultilineInputField(project, objectType, getObjectIdForType(objectType), tag, 50);
	}
	
	public ObjectDataInputField createMultilineField(int objectType, String tag, int columns)
	{
		return new ObjectScrollingMultilineInputField(project, objectType, getObjectIdForType(objectType), tag, columns);
	}
	
	
	public ObjectDataInputField createExpandableField(String tag)
	{
		return createExpandableField(getORef(0).getObjectType(), tag);
	}
	
	public ObjectDataInputField createExpandableField(int objectType, String tag)
	{
		return createExpandableField(objectType, tag, 25);
	}
	
	public ObjectDataInputField createExpandableField(int objectType, String tag, int columns)
	{
		return new ObjectExpandingMultilineInputField(project, objectType, getObjectIdForType(objectType), tag, columns);
	}
	
	public ObjectDataInputField createIndicatorRelevancyOverrideListField(ChoiceQuestion question)
	{
		return new IndicatorRelevancyOverrideListField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), question);
	}
	
	public ObjectDataInputField createStrategyRelevancyOverrideListField(ChoiceQuestion question)
	{
		return new StrategyRelevancyOverrideListField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), question);
	}
	
	public ObjectDataInputField createMultiCodeField(String tagToUse, ChoiceQuestion question, int columnCount)
	{
		return new ObjectCodeListField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tagToUse, question, columnCount);
	}
	
	public ObjectDataInputField createMultiCodeField(int objectType, String tagToUse, ChoiceQuestion question, int columnCount)
	{
		return new ObjectCodeListField(project, objectType, getObjectIdForType(objectType), tagToUse, question, columnCount);
	}

	public ObjectDataInputField createMultiCodeField(String tagToUse, ChoiceQuestion question, CodeList disabledChoices, int columnCount)
	{
		ObjectCodeListField objectCodeListField = (ObjectCodeListField) createMultiCodeField(tagToUse, question, columnCount);
		objectCodeListField.setDisabledCodes(disabledChoices);
		
		return objectCodeListField;
	}
	
	public ObjectDataInputField createReadonlyMultiCodeField(int objectType, String tagToUse, ChoiceQuestion question, int columnCount)
	{
		return new ObjectCodeListDisplayField(project, objectType, getObjectIdForType(objectType), tagToUse, question, columnCount);
	}
	
	public ObjectDataInputField createReadonlyTextField(String tag)
	{
		return new ObjectMultilineDisplayField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createReadonlyTextField(int objectType, String tag)
	{
		return new ObjectMultilineDisplayField(project, objectType, getObjectIdForType(objectType), tag);
	}
	
	public ObjectDataInputField createReadonlyTextField(int objectType, String tag, int columnCount)
	{
		return new ObjectMultilineDisplayField(project, objectType, getObjectIdForType(objectType), tag, columnCount);
	} 
	
	public ObjectDataInputField createChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectChoiceField(project, objectType, getObjectIdForType(objectType), tagToUse, question);
	}
	
	
	public ObjectDataInputField createClassificationChoiceField(String tagToUse, ChoiceQuestion question)
	{
		return new ObjectClassificationChoiceField(project, getORef(0).getObjectType(), getObjectIdForType(getORef(0).getObjectType()), tagToUse, question);
	}
	
	public ObjectDataInputField createRatingChoiceField(String tagToUse, ChoiceQuestion question)
	{
		return new ObjectRaitingChoiceField(project,  getORef(0).getObjectType(), getObjectIdForType( getORef(0).getObjectType()), tagToUse, question);
	}
	
	public ObjectDataInputField createRatingChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectRaitingChoiceField(project, objectType, getObjectIdForType(objectType), tagToUse, question);
	}

	public ObjectDataInputField createIconChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectIconChoiceField(project, objectType, getObjectIdForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createReadOnlyChoiceField(String tagToUse, ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project,  getORef(0).getObjectType(), getObjectIdForType( getORef(0).getObjectType()), tagToUse, question);
	}
	
	public ObjectDataInputField createReadOnlyChoiceField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project, objectType, getObjectIdForType(objectType), tagToUse, question);
	}
	
	public ObjectDataInputField createReadOnlyObjectList(int objectType, String tag)
	{
		return new ObjectReadonlyObjectList(project, objectType, getObjectIdForType(objectType), tag); 
	}
	
	public ObjectDataInputField createRadioChoiceField(int objectType, BaseId objectId, String tagToUse, ChoiceQuestion question)
	{
		return new ObjectRadioButtonGroupField(project, objectType, objectId, tagToUse, question);
	}
	
	public RadioButtonsField createRadioButtonsField(int objectType, String tagToUse, ChoiceQuestion question)
	{
		return new RadioButtonsField(project, objectType, getObjectIdForType(objectType), tagToUse, question);
	}
	
	public BaseId getObjectIdForType(int objectType)
	{
		for (int i=0; i<orefs.length; ++i)
		{
			int type = getORef(i).getObjectType();
			if (objectType == type)
				return  getORef(i).getObjectId();
		}
		return BaseId.INVALID;
	}
	
	public ORef getORef(int index)
	{
		return orefs[index];
	}
	
	
	public void saveModifiedFields()
	{
		for(int i = 0; i < getFields().size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField) getFields().get(i);
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
			ObjectDataInputField field = (ObjectDataInputField) getFields().get(i);
			field.updateFromObject();
		}
		for(AbstractObjectDataInputPanel subPanel : subPanels)
		{
			subPanel.updateFieldsFromProject();
		}

	}

	public Vector getFields()
	{
		return fields;
	}
	
	public void setFieldObjectIds()
	{
		for(int i = 0; i < getFields().size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField) getFields().get(i);
			field.setObjectId(getObjectIdForType(field.getObjectType()));
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
			deleteObjectFromList(cmd.getObjectId());
			setFieldObjectIds();
			return;
		}
		updateFieldsFromProject();
	}
	

	public void deleteObjectFromList(BaseId baseId)
	{
		Vector orefList = new Vector(Arrays.asList(orefs));
		for (int i=0; i<orefs.length; ++i)
		{
			BaseId objectId = getORef(i).getObjectId();
			if (objectId.equals(baseId))
				orefList.remove(i);
		}
		orefs = (ORef[])orefList.toArray(new ORef[0]);
	}

	boolean wasOurObjectJustDeleted(CommandExecutedEvent event)
	{
		if(!event.isDeleteObjectCommand())
			return false;
		
		CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
		if(!cmd.getObjectId().equals(getObjectIdForType(cmd.getObjectType())))
			return false;
		return true;
	}

	public BaseObject getObject()
	{
		return null;
	}

	public BaseId getObjectId()
	{
		return getORef(orefs.length-1).getObjectId();
	}
	
	protected ORefList getSelectedRefs()
	{
		return new ORefList(orefs);
	}
	
	class Picker implements ObjectPicker
	{
		public Picker()
		{
			listeners = new Vector<ListSelectionListener>();
		}

		public ORefList[] getSelectedHierarchies()
		{
			return new ORefList[] {new ORefList(orefs)};
		}

		public BaseObject[] getSelectedObjects()
		{
			Vector<BaseObject> objects = new Vector<BaseObject>();
			for(int i = 0; i < orefs.length; ++i)
				objects.add(getProject().findObject(orefs[i]));
			
			return objects.toArray(new BaseObject[0]);
		}

		public ORefList getSelectionHierarchy()
		{
			return new ORefList(orefs);
		}

		public void clearSelection()
		{
		}

		public void ensureObjectVisible(ORef ref)
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

		public void valueChanged(ListSelectionEvent e)
		{
			throw new RuntimeException("Not supported");
		}
		
		public TreeTableNode[] getSelectedTreeNodes()
		{
			throw new RuntimeException("Not supported");
		}

		private Vector<ListSelectionListener> listeners;
	}

	public static int STD_SHORT = 5;
	
	private Project project;
	private Picker picker;
	private ORef[] orefs;
	private Vector fields;
	private Vector<AbstractObjectDataInputPanel> subPanels;
}

