/* 
Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
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
package org.miradi.views.diagram;

import java.text.ParseException;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.dialogs.AnnotationSelectionDlg;
import org.miradi.dialogs.base.ObjectTablePanel;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.Factor;
import org.miradi.views.ObjectsDoer;
import org.miradi.views.umbrella.ObjectPicker;

public abstract class CreateAnnotationDoer extends ObjectsDoer
{	
	public boolean isAvailable()
	{
		return (getSelectedParent() != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		BaseObject parent = getSelectedParent();
		ObjectPicker picker = getPicker();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORef createRef = createObject();
			CommandSetObjectData appendCommand = createAppendCommand(parent, createRef, getAnnotationListTag());
			getProject().executeCommand(appendCommand);
			doExtraWork(createRef);
			
			if(picker != null)
				picker.ensureObjectVisible(createRef);
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

	protected void doExtraWork(ORef newlyCreatedObjectRef) throws Exception
	{
	}

	public static CommandSetObjectData createAppendCommand(BaseObject parent, ORef refToAppend, String listTag) throws ParseException
	{
		if (parent.isRefList(listTag))
			return CommandSetObjectData.createAppendORefCommand(parent, listTag, refToAppend);
		
		return CommandSetObjectData.createAppendIdCommand(parent, listTag, refToAppend.getObjectId());
	}

	protected ORef createObject() throws CommandFailedException
	{
		CommandCreateObject create = new CommandCreateObject(getAnnotationType());
		getProject().executeCommand(create);
		if (objectToClone!=null)
		{
			CommandSetObjectData[]  commands = objectToClone.createCommandsToClone(create.getCreatedId());
			getProject().executeCommandsWithoutTransaction(commands);
		}
		return create.getObjectRef();
	}
	
	protected BaseObject displayAnnotationList(String title, ObjectTablePanel tablePanel)
	{
		AnnotationSelectionDlg list = new AnnotationSelectionDlg(getMainWindow(), title, tablePanel);
		list.setVisible(true);
		return list.getSelectedObject();
	}
	
	protected void setAnnotationToClone(BaseObject baseObject)
	{
		objectToClone = baseObject;
	}
	
	protected BaseObject getAnnotationToClone()
	{
		return objectToClone;
	}
	
	public BaseObject getSelectedParent()
	{		
		for (int refListIndex = 0; refListIndex < getSelectedHierarchies().length; ++refListIndex)
		{
			ORefList selectedHierarchyRefs =  getSelectedHierarchies()[refListIndex];
			for (int refIndex = 0; refIndex <  selectedHierarchyRefs.size(); ++refIndex)
			{
				if (selectedHierarchyRefs.get(refIndex) != null && Factor.isFactor(selectedHierarchyRefs.get(refIndex)))
				{
					Factor factor = (Factor) getProject().findObject(selectedHierarchyRefs.get(refIndex));
					//FIXME this is a hack,  we need to exclude factors that are not parents
					if (!factor.isStress())
						return factor;
				}
			}
		}
		
		return null;
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