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
package org.miradi.utils;

import java.util.HashSet;

import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Task;
import org.miradi.project.Project;

public class DiagramCorruptionDetector
{
	public static HashSet<DiagramObject> getCorruptedDiagrams(Project project)
	{
		HashSet<DiagramObject> corruptedDiagramObjects = new HashSet();
		ORefList diagramObjectRefs = project.getAllDiagramObjectRefs();
		for (int index = 0; index < diagramObjectRefs.size(); ++index)
		{
			DiagramObject diagramObject = DiagramObject.findDiagramObject(project, diagramObjectRefs.get(index));
			if (isDiagramCorrupted(project, diagramObject))
				corruptedDiagramObjects.add(diagramObject);
		}
		
		return corruptedDiagramObjects;
	}


	private static boolean isDiagramCorrupted(Project project, DiagramObject diagramObject)
	{
		if (hasCorruptedDiagramFactors(project, diagramObject))
			return true;		
		
		if (hasCorruptedDiagramLinks(project, diagramObject))
			return true;
		
		return false;
	}


	//FIXME this is a faulty method.  will be fixed.  needs GB L in ifs
	public static boolean hasCorruptedDiagramLinks(Project project, DiagramObject diagramObject)
	{
		ORefList diagramLinkRefs = diagramObject.getAllDiagramLinkRefs();
		for (int index = 0; index < diagramLinkRefs.size(); ++index)
		{
			DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRefs.get(index));
			DiagramFactor fromDiagramFactor = diagramLink.getFromDiagramFactor();
			DiagramFactor toDiagramFactor = diagramLink.getToDiagramFactor();
			FactorLink factorLink = FactorLink.find(project, diagramLink.getWrappedRef());
			if (factorLink == null)
				return true;
			
			if (fromDiagramFactor == null || toDiagramFactor == null)
				return true;
			
			if (fromDiagramFactor.getWrappedFactor() == null || toDiagramFactor.getWrappedFactor() == null)
				return true;	
		}
		
		return false;
	}


	public static boolean hasCorruptedDiagramFactors(Project project, DiagramObject diagramObject)
	{
		ORefList diagramFactorRefs = diagramObject.getAllDiagramFactorRefs();
		for (int index = 0; index < diagramFactorRefs.size(); ++index)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(index));
			if (diagramFactor == null)
				return true;
			
			final Factor factor = diagramFactor.getWrappedFactor();
			if (factor == null)
				return true;
			
			if (Task.is(factor) && !factor.isActivity())
				return true;
		}
		
		return false;
	}
}
