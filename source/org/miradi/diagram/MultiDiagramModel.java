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
package org.miradi.diagram;

import java.util.Vector;

import org.miradi.diagram.layeredDiagramModels.TargetLayerModel;
import org.miradi.project.Project;

public class MultiDiagramModel extends DiagramModel
{
	public MultiDiagramModel(Project projectToUse)
	{
		super(projectToUse);
		
		reloadModels();
	}
	
	private void reloadModels()
	{
		models = new Vector();
		
		models.add(new TargetLayerModel(getProject()));
	}

	@Override
	public boolean shouldSaveChangesToDisk()
	{
		return true;
	}
	
	@Override
	public int getRootCount()
	{
		int totalCount = 0;
		for (int index = 0; index < models.size(); ++index)
		{
			DiagramModel diagramModel = models.get(index);
			totalCount += diagramModel.getRootCount();
		}
		
		return totalCount;
	}
	
	private Vector<DiagramModel> models;
}
