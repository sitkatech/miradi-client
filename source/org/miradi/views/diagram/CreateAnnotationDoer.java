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
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.AnnotationSelectionDlg;
import org.miradi.dialogs.base.ObjectTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objects.BaseObject;
import org.miradi.utils.BaseObjectDeepCopierUsingCommands;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.umbrella.ObjectPicker;

import java.text.ParseException;

public abstract class CreateAnnotationDoer extends ObjectsDoer
{	
	@Override
	public boolean isAvailable()
	{
		return (getSelectedParentFactor() != null);
	}

	@Override
	protected void doIt() throws Exception
	{
		if(!isAvailable())
			return;
		
		BaseObject parent = getSelectedParentFactor();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORef createRef = createObject();
			CommandSetObjectData appendCommand = createAppendCommand(parent, createRef, getAnnotationListTag());
			getProject().executeCommand(appendCommand);
			doExtraWork(createRef);
		
			ensureObjectVisible(getPicker(), createRef);
		}
		catch(Exception e)
		{
			EAM.logException(e);
			throw new CommandFailedException(e);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	public static void ensureObjectVisible(ObjectPicker picker, ORef createRef)
	{
		if(picker != null)
			picker.ensureOneCopyOfObjectSelectedAndVisible(createRef);
	}

	protected void doExtraWork(ORef newlyCreatedObjectRef) throws Exception
	{
	}

	public static CommandSetObjectData createAppendCommand(BaseObject parent, ORef refToAppend, String listTag) throws ParseException
	{
		if (parent.isRefList(listTag))
			return CommandSetObjectData.createAppendORefCommand(parent, listTag, refToAppend);
		
		return CommandSetObjectData.createAppendIdCommand(parent, listTag, refToAppend.getObjectId());
	}

	protected ORef createObject() throws Exception
	{
		CommandCreateObject create = new CommandCreateObject(getAnnotationType());
		getProject().executeCommand(create);
		if (objectToClone!=null)
		{
			BaseObject baseObjectToFill = BaseObject.find(getProject(), create.getObjectRef());
			BaseObjectDeepCopierUsingCommands deepCopier = new BaseObjectDeepCopierUsingCommands(getProject());
			deepCopier.copyBaseObject(objectToClone, baseObjectToFill);
		}
		return create.getObjectRef();
	}
	
	private BaseObject displayAnnotationList(String title, ObjectTablePanel tablePanel)
	{
		AnnotationSelectionDlg list = new AnnotationSelectionDlg(getMainWindow(), title, tablePanel);
		list.setVisible(true);
		return list.getSelectedObject();
	}
	
	private void setAnnotationToClone(BaseObject baseObject)
	{
		objectToClone = baseObject;
	}
	
	protected BaseObject getAnnotationToClone()
	{
		return objectToClone;
	}
	
	protected boolean validUserChoiceForObjectToClone(ObjectTablePanel tablePanel, String panelText) throws CommandFailedException
	{
		try
		{
			BaseObject cloneAnnotation = displayAnnotationList(panelText, tablePanel);	
			if (cloneAnnotation == null)
				return false;

			setAnnotationToClone(cloneAnnotation);
			return true;
		}
		finally
		{
			tablePanel.dispose();
		}
	}
	
	public abstract int getAnnotationType();
	public abstract String getAnnotationListTag();

	private BaseObject objectToClone;
}