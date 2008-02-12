/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.diagram;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Slide;
import org.miradi.objects.SlideShow;
import org.miradi.views.ObjectsDoer;

public class CreateSlideDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return isInDiagram();
	}

	public void doIt() throws CommandFailedException
	{
		if(!isAvailable())
			return;
		
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			BaseObject object = getDiagramView().getSlideShow();
			DiagramObject diagramObject = getDiagramView().getDiagramPanel().getDiagramObject();
			
			ORef slideRef = createSlide();
			int position = new ORefList(object.getData(SlideShow.TAG_SLIDE_REFS)).size()+1;
			getProject().executeCommand(new CommandSetObjectData(slideRef, Slide.TAG_LABEL, "[SLIDE-"+position+"]"));
			getProject().executeCommand(new CommandSetObjectData(slideRef, Slide.TAG_DIAGRAM_OBJECT_REF, diagramObject.getRef()));
			getProject().executeCommand(new CommandSetObjectData(slideRef, Slide.TAG_DIAGRAM_LEGEND_SETTINGS, getLegendSettings()));
			getProject().executeCommand(CommandSetObjectData.createAppendORefCommand(object, SlideShow.TAG_SLIDE_REFS, slideRef));
			
			//TODO: for this to work...we need to implment this method in ObjectListTable to select the newly inserted row
			getPicker().ensureObjectVisible(slideRef);
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

	public String getLegendSettings()
	{
		return getDiagramView().getDiagramPanel().getDiagramLegendPanel().getLegendSettings().toString();
	}

	protected ORef createSlide() throws CommandFailedException
	{
		CommandCreateObject create = new CommandCreateObject(Slide.getObjectType());
		getProject().executeCommand(create);
		return create.getObjectRef();
	}
	
}
