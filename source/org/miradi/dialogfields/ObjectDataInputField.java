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

abstract public class ObjectDataInputField extends DataField
{
	public ObjectDataInputField(Project projectToUse, ORef refToUse, String tagToUse)
	{
		this(projectToUse, refToUse.getObjectType(), refToUse.getObjectId(), tagToUse);
	}
	
	public ObjectDataInputField(Project projectToUse, int objectTypeToUse, BaseId objectIdToUse, String tagToUse)
	{
		super(projectToUse);
		
		ref = new ORef(objectTypeToUse, objectIdToUse);
		tag = tagToUse;
		allowEdits = true;
	}
	
	public void forceSave()
	{
		setNeedsSave();
		saveIfNeeded();
	}
	
	abstract public String getText();
	abstract public void setText(String newValue);
	
	public ORef getORef()
	{
		return new ORef(getObjectType(), getObjectId());
	}
	
	public int getObjectType()
	{
		return ref.getObjectType();
	}
	
	public void setTag(String tagToUse)
	{
		tag = tagToUse;
	}
	
	public BaseId getObjectId()
	{
		return ref.getObjectId();
	}
	
	public void setObjectId(ORef refToUse)
	{
		ref = refToUse;
	}
	
	public String getTag()
	{
		return tag;
	}
	
	public void updateFromObject()
	{
		saveIfNeeded();
		updateEditableState();
		String text = "";
		if(isValidObject())
			text = getProject().getObjectData(ref, tag);
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
		return (!ref.isInvalid());
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
	
	@Override
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

		CommandSetObjectData cmd = new CommandSetObjectData(ref, tag, newValue);
		try
		{
			getProject().executeCommand(cmd);
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
		String existingValue = getProject().getObjectData(ref, tag);
		return existingValue;
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

	private ORef ref;
	private String tag;
	private boolean allowEdits;
	private boolean needsSave;
}
