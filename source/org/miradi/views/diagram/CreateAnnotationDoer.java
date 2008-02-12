/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
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
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORef createRef = createObject();
			CommandSetObjectData appendCommand = createAppendCommand(parent, createRef);
			getProject().executeCommand(appendCommand);
			doExtraWork(createRef);
			
			ObjectPicker picker = getPicker();
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

	protected CommandSetObjectData createAppendCommand(BaseObject parent, ORef refToAppend) throws ParseException
	{
		if (parent.isRefList(getAnnotationListTag()))
			return CommandSetObjectData.createAppendORefCommand(parent, getAnnotationListTag(), refToAppend);
		
		return CommandSetObjectData.createAppendIdCommand(parent, getAnnotationListTag(), refToAppend.getObjectId());
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
		for (int i = 0; i < getSelectedHierarchies().length; ++i)
		{
			ORefList selectedHierarchyRefs =  getSelectedHierarchies()[i];
			for (int j = 0; j <  selectedHierarchyRefs.size(); ++j)
			{
				if (selectedHierarchyRefs.get(j) != null && Factor.isFactor(selectedHierarchyRefs.get(j)))
				{
					Factor factor = (Factor) getProject().findObject(selectedHierarchyRefs.get(j));
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