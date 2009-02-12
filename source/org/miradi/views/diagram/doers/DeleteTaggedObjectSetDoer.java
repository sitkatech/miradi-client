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
package org.miradi.views.diagram.doers;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.BaseObject;
import org.miradi.objects.ConceptualModelDiagram;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.ResultsChainDiagram;
import org.miradi.views.umbrella.doers.DeletePoolObjectDoer;

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
		ORefList referringDiagramObjectRefs = objectToDelete.findObjectsThatReferToUs(ResultsChainDiagram.getObjectType());
		referringDiagramObjectRefs.addAll(objectToDelete.findObjectsThatReferToUs(ConceptualModelDiagram.getObjectType()));
		for (int index = 0; index < referringDiagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(getProject(), referringDiagramObjectRefs.get(index));
			CommandSetObjectData removeTagFromSelection =  CommandSetObjectData.createRemoveORefCommand(diagramObject, DiagramObject.TAG_SELECTED_TAGGED_OBJECT_SET_REFS, objectToDelete.getRef());
			getProject().executeCommand(removeTagFromSelection);
		}
	}
}
