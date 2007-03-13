/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.dialogs;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.SwingConstants;

import org.conservationmeasures.eam.commands.CommandDeleteObject;
import org.conservationmeasures.eam.dialogfields.ObjectAdjustableStringInputField;
import org.conservationmeasures.eam.dialogfields.ObjectCheckBoxField;
import org.conservationmeasures.eam.dialogfields.ObjectChoiceField;
import org.conservationmeasures.eam.dialogfields.ObjectClassificationChoiceField;
import org.conservationmeasures.eam.dialogfields.ObjectCodeListField;
import org.conservationmeasures.eam.dialogfields.ObjectDataInputField;
import org.conservationmeasures.eam.dialogfields.ObjectDateChooserInputField;
import org.conservationmeasures.eam.dialogfields.ObjectMultilineDisplayField;
import org.conservationmeasures.eam.dialogfields.ObjectMultilineInputField;
import org.conservationmeasures.eam.dialogfields.ObjectNumericInputField;
import org.conservationmeasures.eam.dialogfields.ObjectRaitingChoiceField;
import org.conservationmeasures.eam.dialogfields.ObjectReadonlyChoiceField;
import org.conservationmeasures.eam.dialogfields.ObjectStringInputField;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.CommandExecutedEvent;
import org.conservationmeasures.eam.main.CommandExecutedListener;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.EAMObject;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.questions.ChoiceQuestion;
import org.martus.swing.UiLabel;

import com.jhlabs.awt.Alignment;
import com.jhlabs.awt.GridLayoutPlus;

abstract public class ObjectDataInputPanel extends ModelessDialogPanel implements CommandExecutedListener
{
	
	public ObjectDataInputPanel(Project projectToUse, int objectType, BaseId idToUse)
	{
		this(projectToUse, new Vector(Arrays.asList(new ORef[] {new ORef(objectType, idToUse)})));
	}
	
	
	public ObjectDataInputPanel(Project projectToUse, ORef orefToUse)
	{
		this(projectToUse, new Vector(Arrays.asList(new ORef[] {orefToUse})));
	}
	
	
	public ObjectDataInputPanel(Project projectToUse, Vector orefsToUse)
	{
		GridLayoutPlus layout = new GridLayoutPlus(0, 2);
		layout.setColAlignment(0, Alignment.NORTHEAST);
		setLayout(layout);
		project = projectToUse;
		orefs = orefsToUse;
		fields = new Vector();
		project.addCommandExecutedListener(this);
	}
	
	public void dispose()
	{
		project.removeCommandExecutedListener(this);
		super.dispose();
	}

	public Project getProject()
	{
		return project;
	}
	

	//FIXME: should me shold not rely on existing OREF....change setObjectId to take an ORef
	public void setObjectId(ORef oref)
	{
		setObjectId(new Vector(Arrays.asList(new ORef[] {oref})));
	}
	
	
	public void setObjectId(Vector orefsToUse)
	{
		saveModifiedFields();
		orefs = orefsToUse;
		updateFieldsFromProject();
	}
	
	public void setFocusOnFirstField()
	{
		//TODO: should be first non read only field.
		if (fields.size()>0)
		{
			((ObjectDataInputField)fields.get(0)).getComponent().requestFocusInWindow();
			Rectangle rect = ((ObjectDataInputField)fields.get(0)).getComponent().getBounds();
			scrollRectToVisible(rect);
		}
	}
		
	public void addField(ObjectDataInputField field)
	{
		fields.add(field);
		addLabel(field.getObjectType(), field.getTag());
		addFieldComponent(field.getComponent());
	}

	public void addLabel(String translatedLabelText)
	{
		addLabel(getORef(0).getObjectType(), translatedLabelText);
	}
	
	public void addLabel(int objectType, String translatedLabelText)
	{
		UiLabel label = new UiLabel(EAM.fieldLabel(objectType, translatedLabelText));
		label.setVerticalAlignment(SwingConstants.TOP);
		add(label);
	}
	
	public void addFieldComponent(Component component)
	{
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(component, BorderLayout.BEFORE_LINE_BEGINS);
		add(panel);
	}
	
	
	public ObjectDataInputField createCheckBoxField(String tag, String on, String off)
	{
		return new ObjectCheckBoxField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag, on, off);
	}
	
	public ObjectDataInputField createCheckBoxField(int objectType, String tag, String on, String off)
	{
		return new ObjectCheckBoxField(project, objectType, getObjecIdForType(objectType), tag, on, off);
	}
	
	
	public ObjectDataInputField createStringField(String tag)
	{
		return new ObjectStringInputField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createStringField(int objectType, String tag)
	{
		return new ObjectStringInputField(project, objectType, getObjecIdForType(objectType), tag);
	}
	
	
	public ObjectDataInputField createStringField(String tag, int column)
	{
		return new ObjectAdjustableStringInputField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag, column);
	}
	
	public ObjectDataInputField createStringField(int objectType, String tag, int column)
	{
		return new ObjectAdjustableStringInputField(project, objectType, getObjecIdForType(objectType), tag, column);
	}
	
	
	public ObjectDataInputField createDateChooserField(String tag)
	{
		return new ObjectDateChooserInputField(project,  getORef(0).getObjectType(), getObjecIdForType( getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createDateChooserField(int objectType, String tag)
	{
		return new ObjectDateChooserInputField(project, objectType, getObjecIdForType(objectType), tag);
	}
	
	
	public ObjectDataInputField createNumericField(String tag, int column)
	{
		return new ObjectNumericInputField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag, column);
	}
	
	public ObjectDataInputField createNumericField(int objectType, String tag, int column)
	{
		return new ObjectNumericInputField(project, objectType, getObjecIdForType(objectType), tag, column);
	}
	

	public ObjectDataInputField createNumericField(String tag)
	{
		return new ObjectNumericInputField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createNumericField(int objectType, String tag)
	{
		return new ObjectNumericInputField(project, objectType, getObjecIdForType(objectType), tag);
	}

	public ObjectDataInputField createMultilineField(String tag)
	{
		return new ObjectMultilineInputField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createMultilineField(int objectType, String tag)
	{
		return new ObjectMultilineInputField(project, objectType, getObjecIdForType(objectType), tag);
	}
	
	public ObjectDataInputField createMultiCodeField(ChoiceQuestion question)
	{
		return new ObjectCodeListField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), question);
	}
	
	public ObjectDataInputField createMultiCodeField(int objectType, ChoiceQuestion question)
	{
		return new ObjectCodeListField(project, objectType, getObjecIdForType(objectType), question);
	}

	public ObjectDataInputField createReadonlyTextField(String tag)
	{
		return new ObjectMultilineDisplayField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), tag);
	}
	
	public ObjectDataInputField createReadonlyTextField(int objectType, String tag)
	{
		return new ObjectMultilineDisplayField(project, objectType, getObjecIdForType(objectType), tag);
	}
	
	public ObjectDataInputField ObjectChoiceField(int objectType, ChoiceQuestion question)
	{
		return new ObjectChoiceField(project, objectType, getObjecIdForType(objectType), question);
	}
	
	public ObjectDataInputField createClassificationChoiceField(ChoiceQuestion question)
	{
		return new ObjectClassificationChoiceField(project, getORef(0).getObjectType(), getObjecIdForType(getORef(0).getObjectType()), question);
	}
	
	public ObjectDataInputField createClassificationChoiceField(int objectType, ChoiceQuestion question)
	{
		return new ObjectClassificationChoiceField(project, objectType, getObjecIdForType(objectType), question);
	}
	
	public ObjectDataInputField createRatingChoiceField(ChoiceQuestion question)
	{
		return new ObjectRaitingChoiceField(project,  getORef(0).getObjectType(), getObjecIdForType( getORef(0).getObjectType()), question);
	}
	
	public ObjectDataInputField createRatingChoiceField(int objectType, ChoiceQuestion question)
	{
		return new ObjectRaitingChoiceField(project, objectType, getObjecIdForType(objectType), question);
	}

	public ObjectDataInputField createReadOnlyChoiceField(ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project,  getORef(0).getObjectType(), getObjecIdForType( getORef(0).getObjectType()), question);
	}
	
	public ObjectDataInputField createReadOnlyChoiceField(int objectType, ChoiceQuestion question)
	{
		return new ObjectReadonlyChoiceField(project, objectType, getObjecIdForType(objectType), question);
	}
	
	
	private BaseId getObjecIdForType(int objectType)
	{
		for (int i=0; i<orefs.size(); ++i)
		{
			int type = getORef(i).getObjectType();
			if (objectType == type)
				return  getORef(i).getObjectId();
		}
		return BaseId.INVALID;
	}
	
	
	public ORef getORef(int index)
	{
		return (ORef) orefs.get(index);
	}
	
	
	public void saveModifiedFields()
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
			field.saveIfNeeded();
		}
	}
	
	public void updateFieldsFromProject()
	{
		setFieldObjectIds();
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
			field.updateFromObject();
		}
	}
	
	public void setFieldObjectIds()
	{
		for(int i = 0; i < fields.size(); ++i)
		{
			ObjectDataInputField field = (ObjectDataInputField)fields.get(i);
			field.setObjectId(getObjecIdForType(field.getObjectType()));
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
		for (int i=0; i<orefs.size(); ++i)
		{
			BaseId objectId = getORef(i).getObjectId();
			if (objectId.equals(baseId))
				orefs.remove(i);
		}
	}

	boolean wasOurObjectJustDeleted(CommandExecutedEvent event)
	{
		if(!event.isDeleteObjectCommand())
			return false;
		
		CommandDeleteObject cmd = (CommandDeleteObject)event.getCommand();
		if(!cmd.getObjectId().equals(getObjecIdForType(cmd.getObjectType())))
			return false;
		return true;
	}

	public EAMObject getObject()
	{
		return null;
	}

	public BaseId getObjectId()
	{
		return getORef(orefs.size()-1).getObjectId();
	}


	private Project project;
	private Vector orefs;
	private Vector fields;
}
