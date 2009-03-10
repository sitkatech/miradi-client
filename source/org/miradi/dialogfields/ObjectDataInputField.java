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
package org.miradi.dialogfields;

import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.Project;
import org.miradi.utils.InvalidDateException;
import org.miradi.utils.InvalidNumberException;

abstract public class ObjectDataInputField extends SavableField
{
	public ObjectDataInputField(Project projectToUse, ORef refToUse, String tagToUse)
	{
		this(projectToUse, refToUse.getObjectType(), refToUse.getObjectId(), tagToUse);
	}
	
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
	
	public void forceSave()
	{
		setNeedsSave();
		saveIfNeeded();
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
	
	public void setTag(String tagToUse)
	{
		tag = tagToUse;
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
	
	public boolean needsToBeSaved()
	{
		return needsSave;
	}
	
	public void setNeedsSave()
	{
		if(getORef().isInvalid())
			return;
		
		boolean currentDataMatchesSaved = getOldValue().equals(getText());
		needsSave = !currentDataMatchesSaved;
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
		getComponent().setBorder(createLineBorderWithMargin());
	}

	public static CompoundBorder createLineBorderWithMargin()
	{
		Border lineBorder = BorderFactory.createLineBorder(Color.BLACK);
		Border margin = BorderFactory.createEmptyBorder(2, 2, 2, 2);
		CompoundBorder coumpoundBorder = BorderFactory.createCompoundBorder(lineBorder, margin);
		return coumpoundBorder;
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
			EAM.errorDialog(getNumericErrorMessage());
		}
		catch(NumberFormatException nfe)
		{
			EAM.errorDialog(getNumericErrorMessage());
		}
		catch(Throwable e)
		{
			EAM.errorDialog(EAM.text("Text|Unknown error prevented saving this data"));
		}
	}
	
	private String getNumericErrorMessage()
	{
		return "<html>" + EAM.text("This value must be numeric<br><br>" +
		"Currency symbols and percent signs are not allowed");
	}

	public Project getProject()
	{
		return project;
	}
	
	public DocumentEventHandler createDocumentEventHandler()
	{
		return new DocumentEventHandler();
	}

	public class DocumentEventHandler implements DocumentListener
	{
		public void changedUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}

		public void insertUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}

		public void removeUpdate(DocumentEvent arg0)
		{
			setNeedsSave();
		}
	}

	Project project;
	private int objectType;
	private BaseId objectId;
	private String tag;
	private boolean allowEdits;
	private boolean needsSave;
}
