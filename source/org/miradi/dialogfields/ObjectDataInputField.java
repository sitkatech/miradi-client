/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.dialogfields;

import java.awt.Color;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.InvalidDateException;
import org.miradi.utils.InvalidNumberException;

abstract public class ObjectDataInputField implements FocusListener
{
	public ObjectDataInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		project = projectToUse;
		objectType = objectTypeToUse;
		objectId = objectIdToUse;
		tag = tagToUse;
		allowEdits = true;
	}
	
	public void dispose()
	{
	}
	
	abstract public JComponent getComponent();
	abstract public String getText();
	abstract public void setText(String newValue);
	
	public ORef getORef()
	{
		return new ORef(getObjectType(), getObjectId());
	}
	
	public int getObjectType()
	{
		return objectType;
	}
	
	public void setObjectType(int newType)
	{
		objectType = newType;
	}
	
	public BaseId getObjectId()
	{
		return objectId;
	}
	
	public void setObjectId(BaseId newId)
	{
		objectId = newId;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	void addFocusListener()
	{
		getComponent().addFocusListener(this);
	}
	
	public void focusGained(FocusEvent e)
	{
		EAM.logVerbose("focusGained " + tag);
		focusedField = this;
	}

	public void focusLost(FocusEvent e)
	{
		EAM.logVerbose("focusLost " + tag);
		saveIfNeeded();
		focusedField = null;
	}
	
	public static void saveFocusedFieldPendingEdits()
	{
		if(focusedField == null)
			return;
		focusedField.saveIfNeeded();
	}
	
	public void updateFromObject()
	{
		saveIfNeeded();
		updateEditableState();
		String text = "";
		if(isValidObject())
			text = project.getObjectData(objectType, objectId, tag);
		if (text.equals(getText()))
			return;
		setText(text);
	}
	
	public boolean allowEdits()
	{
		return allowEdits;
	}

	public boolean isValidObject()
	{
		return (!objectId.isInvalid());
	}
	
	public void setVisible(boolean isVisible)
	{
		getComponent().setVisible(isVisible);
	}
	
	public void setEditable(boolean newState)
	{
		allowEdits = newState;
		updateEditableState();
	}

	public void updateEditableState()
	{
		getComponent().setEnabled(false);
	}
	
	boolean needsToBeSaved()
	{
		return needsSave;
	}
	
	void setNeedsSave()
	{
		needsSave = true;
	}
	
	void clearNeedsSave()
	{
		needsSave = false;
	}
	
	public void saveIfNeeded()
	{
		if(!needsToBeSaved())
			return;
		
		clearNeedsSave();

		if(!isValidObject())
			return;
		
		String newValue = getText();
		String existingValue = getOldValue();
		if(existingValue.equals(newValue))
			return;

		CommandSetObjectData cmd = new CommandSetObjectData(objectType, objectId, tag, newValue);
		try
		{
			project.executeCommand(cmd);
			updateFromObject();
		}
		catch(CommandFailedException e)
		{
			EAM.logException(e);
			notifyUserOfFailure(e);
			setText(existingValue);
			getComponent().requestFocus();
		}
	}

	String getOldValue()
	{
		String existingValue = project.getObjectData(objectType, objectId, tag);
		return existingValue;
	}
	
	void setDefaultFieldBorder()
	{
		getComponent().setBorder(BorderFactory.createLineBorder(Color.BLACK));
	}
	
	private void notifyUserOfFailure(CommandFailedException cfe)
	{
		try
		{
			throw(cfe.getCause());
		}
		catch (InvalidDateException ide)
		{
			EAM.errorDialog(EAM.text("Text|Dates must be in YYYY-MM-DD format"));
		}
		catch (InvalidNumberException ine)
		{
			EAM.errorDialog(EAM.text(NUMERIC_ERROR_MESSEGE));
		}
		catch(NumberFormatException nfe)
		{
			EAM.errorDialog(EAM.text(NUMERIC_ERROR_MESSEGE));
		}
		catch(Throwable e)
		{
			EAM.errorDialog(EAM.text("Text|Unknown error prevented saving this data"));
		}
	}
	
	public Project getProject()
	{
		return project;
	}

	private static final String NUMERIC_ERROR_MESSEGE = "Text|Must be numeric";
	Project project;
	private int objectType;
	private BaseId objectId;
	private String tag;
	private boolean allowEdits;
	private boolean needsSave;
	
	public static ObjectDataInputField focusedField;
}
