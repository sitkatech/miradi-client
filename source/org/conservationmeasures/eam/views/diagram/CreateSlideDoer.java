/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram;

import org.conservationmeasures.eam.commands.CommandBeginTransaction;
import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandEndTransaction;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.exceptions.CommandFailedException;
import org.conservationmeasures.eam.ids.BaseId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objectpools.EAMObjectPool;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.views.ObjectsDoer;
import org.conservationmeasures.eam.views.umbrella.ObjectPicker;

public class CreateSlideDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return isDiagramView();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			BaseObject object = getSlideShow();
			DiagramObject diagramObject = getDiagramView().getDiagramPanel().getDiagramObject();
			
			BaseObject createdSlide = createObject();
			BaseId createdId = createdSlide.getId();
			getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(object, SlideShow.TAG_SLIDE_REFS, createdId));
			getProject().executeCommand(CommandSetObjectData.createAppendIdCommand(createdSlide, Slide.TAG_DIAGRAM_OBJECT_REF, diagramObject.getId()));
			
			ORef ref = new ORef(createdSlide.getType(), createdId);
			ObjectPicker picker = getPicker();
			if(picker != null)
				picker.ensureObjectVisible(ref);
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

	protected BaseObject createObject() throws CommandFailedException
	{
		CommandCreateObject create = new CommandCreateObject(Slide.getObjectType());
		getProject().executeCommand(create);
		return getProject().findObject(new ORef(Slide.getObjectType(),create.getCreatedId()));
	}
	
	
	private BaseObject getSlideShow() throws CommandFailedException
	{
		EAMObjectPool pool = getProject().getPool(SlideShow.getObjectType());
		if (pool.size()==0)
		{
			throw new CommandFailedException("Slide Show not found in pool");
		}

		return getProject().findObject(SlideShow.getObjectType(), pool.getIds()[0]);
	}
}
