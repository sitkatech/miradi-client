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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandBeginTransaction;
import org.miradi.commands.CommandEndTransaction;
import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.TaggedObjectSet;
import org.miradi.schemas.ConceptualModelDiagramSchema;
import org.miradi.schemas.DiagramFactorSchema;
import org.miradi.schemas.ResultsChainDiagramSchema;
import org.miradi.views.umbrella.doers.DeletePoolObjectDoer;

import java.text.ParseException;

public class DeleteTaggedObjectSetDoer extends DeletePoolObjectDoer
{
	@Override
	protected String getCustomText()
	{
		return EAM.text("Tag");
	}
	
	@Override
	protected void doWork(BaseObject objectToDelete) throws Exception
	{
		getProject().executeCommand(new CommandBeginTransaction());
		try
		{
			removeTaggedObjectSetRefFromDiagramObjects(objectToDelete);
			removeTaggedObjectSetRefFromDiagramFactors(objectToDelete);
		}
		finally
		{
			getProject().executeCommand(new CommandEndTransaction());
		}
	}

	private void removeTaggedObjectSetRefFromDiagramObjects(BaseObject taggedObjectSetToDelete) throws ParseException, CommandFailedException
	{
		ORefList referringDiagramObjectRefs = taggedObjectSetToDelete.findObjectsThatReferToUs(ResultsChainDiagramSchema.getObjectType());
		referringDiagramObjectRefs.addAll(taggedObjectSetToDelete.findObjectsThatReferToUs(ConceptualModelDiagramSchema.getObjectType()));
		for (int index = 0; index < referringDiagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), referringDiagramObjectRefs.get(index));
			CommandSetObjectData removeTagFromSelection =  CommandSetObjectData.createRemoveORefCommand(diagramObject, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, taggedObjectSetToDelete.getRef());
			getProject().executeCommand(removeTagFromSelection);
		}
	}

	private void removeTaggedObjectSetRefFromDiagramFactors(BaseObject taggedObjectSetToDelete) throws ParseException, CommandFailedException
	{
		ORefList referringDiagramFactorRefs = taggedObjectSetToDelete.findObjectsThatReferToUs(DiagramFactorSchema.getObjectType());
		for (int index = 0; index < referringDiagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(getProject(), referringDiagramFactorRefs.get(index));
			CommandSetObjectData removeTagFromDiagramFactor =  CommandSetObjectData.createRemoveORefCommand(diagramFactor, DiagramFactor.TAG_TAGGED_OBJECT_SET_REFS, taggedObjectSetToDelete.getRef());
			getProject().executeCommand(removeTagFromDiagramFactor);
		}
	}

	@Override
	protected boolean canDelete(BaseObject singleSelectedObject)
	{
		return TaggedObjectSet.is(singleSelectedObject);
	}
}
