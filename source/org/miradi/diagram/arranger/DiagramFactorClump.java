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

package org.miradi.diagram.arranger;

import java.util.HashSet;
import java.util.Set;

import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.GroupBox;
import org.miradi.project.Project;

public class DiagramFactorClump
{
	public DiagramFactorClump(DiagramObject diagramToUse, DiagramFactor diagramFactorOrGroup)
	{
		diagram = diagramToUse;
		diagramFactorMaybeGroup = diagramFactorOrGroup;
		incomingLinks = findLinks(DiagramLink.TO);
		outgoingLinks = findLinks(DiagramLink.FROM);
	}
	
	public int getTotalLinkCount()
	{
		int incomingLinkCount = getIncomingLinks().size();
		int outgoingLinkCount = getOutgoingLinks().size();
		return incomingLinkCount + outgoingLinkCount;
	}
	
	public Set<DiagramLink> getOutgoingLinks()
	{
		return outgoingLinks;
	}
	
	public Set<DiagramLink> getIncomingLinks()
	{
		return incomingLinks;
	}
	
	public Set<DiagramLink> getLinks(int direction)
	{
		if(direction == DiagramLink.FROM)
			return incomingLinks;
		if(direction == DiagramLink.TO)
			return outgoingLinks;
		throw new RuntimeException("Unrecognized direction: " + direction);
	}
	
	public int getRowCount()
	{
		if(!isGroup())
			return 1;
		
		return diagramFactorMaybeGroup.getGroupBoxChildrenRefs().size();
	}
	
	
	public DiagramFactor getDiagramFactor(int i)
	{
		if(!isGroup())
			return diagramFactorMaybeGroup;
		
		ORef ref = diagramFactorMaybeGroup.getGroupBoxChildrenRefs().get(i);
		return DiagramFactor.find(getProject(), ref);
	}

	private Set<DiagramLink> findLinks(int direction)
	{
		if(isGroup())
			return findLinksForGroupAndChildren(direction);
		
		return findLinksForPlainDiagramFactor(diagramFactorMaybeGroup, direction);
	}

	private Set<DiagramLink> findLinksForGroupAndChildren(int direction)
	{
		HashSet<DiagramLink> links = new HashSet<DiagramLink>();
		Project project = getProject();

		links.addAll(findLinksForPlainDiagramFactor(diagramFactorMaybeGroup, direction));

		ORefList childRefs = diagramFactorMaybeGroup.getGroupBoxChildrenRefs();
		for(int i = 0; i < childRefs.size(); ++i)
		{
			DiagramFactor child = DiagramFactor.find(project, childRefs.get(i));
			links.addAll(findLinksForPlainDiagramFactor(child, direction));
		}
		
		return links;
	}

	private Project getProject()
	{
		return diagram.getProject();
	}

	private Set<DiagramLink> findLinksForPlainDiagramFactor(DiagramFactor diagramFactor, int direction)
	{
		HashSet<DiagramLink> links = new HashSet<DiagramLink>();

		Project project = getProject();
		ORefList diagramLinkRefs = diagram.getAllDiagramLinkRefs();
		for(int i = 0; i < diagramLinkRefs.size(); ++i)
		{
			DiagramLink diagramLink = DiagramLink.find(project, diagramLinkRefs.get(i));
			if(isLinkRelevant(diagramLink, diagramFactor, direction))
				links.add(diagramLink);
		}
		
		return links;
	}

	private boolean isLinkRelevant(DiagramLink diagramLink, DiagramFactor diagramFactor, int direction)
	{
		if(diagramLink.isBidirectional() && diagramLink.isToOrFrom(diagramFactor))
			return true;
		
		if(diagramFactor.getRef().equals(diagramLink.getDiagramFactorRef(direction)))
			return true;
		
		return false;
	}

	private boolean isGroup()
	{
		return GroupBox.is(diagramFactorMaybeGroup.getWrappedORef());
	}
	
	@Override
	public int hashCode()
	{
		return diagramFactorMaybeGroup.getRef().getObjectId().asInt();
	}
	
	@Override
	public boolean equals(Object rawOther)
	{
		if(! (rawOther instanceof DiagramFactorClump))
			return false;
		
		DiagramFactorClump other = (DiagramFactorClump)rawOther;
		return diagramFactorMaybeGroup.getRef().equals(other.diagramFactorMaybeGroup.getRef());
	}
	
	@Override
	public String toString()
	{
		String result = "";
		if(isGroup())
			result = "Group: ";
		
		result += diagramFactorMaybeGroup.getRef() + ": ";
		result += diagramFactorMaybeGroup.getWrappedFactor().combineShortLabelAndLabel();
		return result;
	}

	private DiagramObject diagram;
	private DiagramFactor diagramFactorMaybeGroup;
	private Set<DiagramLink> incomingLinks;
	private Set<DiagramLink> outgoingLinks;
}
