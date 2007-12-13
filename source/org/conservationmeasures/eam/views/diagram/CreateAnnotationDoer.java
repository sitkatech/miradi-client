/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.dialogs.AnnotationSelectionDlg;
import org.conservationmeasures.eam.dialogs.base.ObjectPoolTablePanel;
import org.conservationmeasures.eam.dialogs.base.ObjectTablePanel;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public abstract class CreateAnnotationDoer extends ObjectsDoer
{	
	public boolean isAvailable()
	{
		return (getSelectedFactor() != null);
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		Factor factor = getSelectedFactor();
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			ORef createRef = createObject();
			CommandSetObjectData appendCommand = createAppendCommand(factor, createRef);
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

	protected CommandSetObjectData createAppendCommand(Factor factor, ORef refToAppend) throws ParseException
	{
		if (factor.isRefList(getAnnotationListTag()))
			return CommandSetObjectData.createAppendORefCommand(factor, getAnnotationListTag(), refToAppend);
		
		return CommandSetObjectData.createAppendIdCommand(factor, getAnnotationListTag(), refToAppend.getObjectId());
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
	
	public Factor getSelectedFactor()
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
	
	protected boolean validUserChoiceForObjectToClone(ObjectPoolTablePanel poolTablePanel, String panelText) throws CommandFailedException
	{
		try
		{
			BaseObject cloneAnnotation = displayAnnotationList(panelText, poolTablePanel);	
			if (cloneAnnotation == null)
				return false;

			setAnnotationToClone(cloneAnnotation);
			return true;
		}
		finally
		{
			poolTablePanel.dispose();
		}
	}
	
	public abstract int getAnnotationType();
	public abstract String getAnnotationListTag();

	private BaseObject objectToClone;
}