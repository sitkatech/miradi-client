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
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.BaseObject;
import org.conservationmeasures.eam.objects.DiagramObject;
import org.conservationmeasures.eam.objects.Slide;
import org.conservationmeasures.eam.objects.SlideShow;
import org.conservationmeasures.eam.views.ObjectsDoer;

public class CreateSlideDoer extends ObjectsDoer
{
	public boolean isAvailable()
	{
		return inInDiagram();
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
