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

import java.awt.Point;
import java.util.AbstractCollection;
import java.util.Vector;

import org.miradi.commands.CommandSetObjectData;
import org.miradi.exceptions.CommandFailedException;
import org.miradi.exceptions.UnexpectedNonSideEffectException;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objects.Cause;
import org.miradi.objects.DiagramFactor;
import org.miradi.objects.DiagramLink;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Factor;
import org.miradi.objects.GroupBox;
import org.miradi.objects.Strategy;
import org.miradi.objects.Target;
import org.miradi.project.FactorCommandHelper;
import org.miradi.project.Project;
import org.miradi.views.diagram.LinkCreator;

public class MeglerArranger
{
	public MeglerArranger(DiagramObject diagramToArrange)
	{
		diagram = diagramToArrange;
		
	}

	public void arrange() throws Exception
	{
		extractFactorsOfInterest();
		segregateUnlinkedFactors();
		createGroupBoxes();
		setLocations();
	}

	private void segregateUnlinkedFactors()
	{
		unlinked = new Vector<DiagramFactor>();
		
		unlinked.addAll(extractUnlinkedDiagramFactors(strategies));
		unlinked.addAll(extractUnlinkedDiagramFactors(threats));
		unlinked.addAll(extractUnlinkedDiagramFactors(targets));
	}
	
	private void createGroupBoxes() throws Exception
	{
		createTargetGroupBoxes();
	}

	private void createTargetGroupBoxes() throws Exception
	{
		Vector<DiagramFactor> groupCandidates = new Vector<DiagramFactor>();
		groupCandidates.addAll(targets);

		while(groupCandidates.size() > 1)
		{
			Vector<DiagramFactor> groupedTargets = createBiggestPossibleGroup(new Vector<DiagramFactor>(groupCandidates));
			if(groupedTargets.size() == 0)
				break;
			groupCandidates.removeAll(groupedTargets);
		}
	}

	private Vector<DiagramFactor> createBiggestPossibleGroup(Vector<DiagramFactor> groupCandidates) throws Exception, UnexpectedNonSideEffectException, CommandFailedException
	{
		while(groupCandidates.size() > 1)
		{
			int wouldRemoveLinkCount = 0;
			ORefSet fromDiagramFactorRefs = getRefsOfFactorsThatLinkTo(groupCandidates);
			for(ORef fromRef : fromDiagramFactorRefs)
			{
				if(isLinkedToAll(fromRef, groupCandidates))
					wouldRemoveLinkCount += groupCandidates.size();
			}
			
			if(wouldRemoveLinkCount > 1)
			{
				createAndLinkToGroupBox(fromDiagramFactorRefs, groupCandidates);
				break;
			}
			
			groupCandidates.remove(groupCandidates.size() - 1);
		}
		
		return groupCandidates;
	}

	private void createAndLinkToGroupBox(ORefSet fromDiagramFactorRefs, Vector<DiagramFactor> groupCandidates) throws Exception, UnexpectedNonSideEffectException, CommandFailedException
	{
		ORefList childRefs = new ORefList(groupCandidates.toArray(new DiagramFactor[0]));
		FactorCommandHelper helper = new FactorCommandHelper(getProject(), diagram);
		ORef groupDiagramFactorRef = new ORef(DiagramFactor.getObjectType(), helper.createFactorAndDiagramFactor(GroupBox.getObjectType()).getCreatedId());
		CommandSetObjectData addChildren = new CommandSetObjectData(groupDiagramFactorRef, DiagramFactor.TAG_GROUP_BOX_CHILDREN_REFS, childRefs.toString());
		getProject().executeCommand(addChildren);
		LinkCreator linkCreator = new LinkCreator(getProject());
		for(ORef fromRef : fromDiagramFactorRefs)
			linkCreator.createFactorLinkAndDiagramLink(diagram, DiagramFactor.find(getProject(), fromRef), DiagramFactor.find(getProject(), groupDiagramFactorRef));
	}

	private boolean isLinkedToAll(ORef fromRef, AbstractCollection<DiagramFactor> groupCandidates)
	{
		for(DiagramFactor factor : groupCandidates)
		{
			if(!diagram.areDiagramFactorsLinkedFromToNonBidirectional(fromRef, factor.getRef()))
				return false;
		}
		
		return true;
	}

	private ORefSet getRefsOfFactorsThatLinkTo(AbstractCollection<DiagramFactor> groupCandidates)
	{
		ORefSet allFroms = new ORefSet();
		for(DiagramFactor factor : groupCandidates)
			allFroms.addAll(getRefsOfFactorsThatLinkTo(factor));
		
		return allFroms;
	}

	private ORefSet getRefsOfFactorsThatLinkTo(DiagramFactor factor)
	{
		ORefList linkRefs = factor.findObjectsThatReferToUs(DiagramLink.getObjectType());
		ORefSet froms = new ORefSet();
		for(int i = 0; i < linkRefs.size(); ++i)
		{
			DiagramLink link = DiagramLink.find(getProject(), linkRefs.get(i));
			froms.add(link.getFromDiagramFactorRef());
		}
		return froms;
	}

	private Vector<DiagramFactor> extractUnlinkedDiagramFactors(Vector<DiagramFactor> candidates)
	{
		Vector<DiagramFactor> unlinkedDiagramFactors = new Vector<DiagramFactor>();
		for(DiagramFactor diagramFactor : candidates)
		{
			ORefList linkRefs = diagramFactor.findObjectsThatReferToUs(DiagramLink.getObjectType());
			if(linkRefs.size() == 0)
				unlinkedDiagramFactors.add(diagramFactor);
		}
		candidates.removeAll(unlinkedDiagramFactors);
		return unlinkedDiagramFactors;
	}

	private void setLocations() throws Exception
	{
		moveFactorsToFinalLocations(unlinked, UNLINKED_COLUMN_X, TOP_Y);
		moveFactorsToFinalLocations(strategies, STRATEGY_COLUMN_X, TOP_Y);
		moveFactorsToFinalLocations(threats, THREAT_COLUMN_X, TOP_Y);
		moveFactorsToFinalLocations(targets, TARGET_COLUMN_X, TOP_Y);
	}

	private void moveFactorsToFinalLocations(Vector<DiagramFactor> factors, int x, int initialY) throws Exception
	{
		int y = initialY;
		FactorCommandHelper helper = new FactorCommandHelper(getProject(), diagram);
		for(DiagramFactor diagramFactor : factors)
		{
			Point newLocation = new Point(x, y);
			helper.setDiagramFactorLocation(diagramFactor.getDiagramFactorId(), newLocation);
			
			y += DELTA_Y;
		}
	}
	
	private Project getProject()
	{
		return diagram.getProject();
	}

	private void extractFactorsOfInterest()
	{
		strategies = new Vector<DiagramFactor>();
		threats = new Vector<DiagramFactor>();
		targets = new Vector<DiagramFactor>();
		
		Project project = diagram.getProject();
		ORefList diagramFactorRefs = diagram.getAllDiagramFactorRefs();
		for(int i = 0; i < diagramFactorRefs.size(); ++i)
		{
			DiagramFactor diagramFactor = DiagramFactor.find(project, diagramFactorRefs.get(i));
			if(isAlreadyInGroup(diagramFactor))
				continue;
			Factor factor = diagramFactor.getWrappedFactor();
			if(Strategy.is(factor))
				strategies.add(diagramFactor);
			if(Target.is(factor))
				targets.add(diagramFactor);
			if(Cause.isDirectThreat(factor))
				threats.add(diagramFactor);
		}
	}

	private boolean isAlreadyInGroup(DiagramFactor diagramFactor)
	{
		ORef childRef = diagramFactor.getRef();
		ORefList likelyGroupRefs = diagramFactor.findObjectsThatReferToUs(DiagramFactor.getObjectType());
		for(int i = 0; i < likelyGroupRefs.size(); ++i)
		{
			DiagramFactor possibleGroup = DiagramFactor.find(getProject(), likelyGroupRefs.get(i));
			if(possibleGroup.getGroupBoxChildrenRefs().contains(childRef))
				return true;
		}
		
		return false;
	}

	private static final int UNLINKED_COLUMN_X = 30;
	private static final int STRATEGY_COLUMN_X = 180;
	private static final int THREAT_COLUMN_X = 330;
	private static final int TARGET_COLUMN_X = 480;
	
	private static final int TOP_Y = 30;
	private static final int DELTA_Y = 90;

	private DiagramObject diagram;
	private Vector<DiagramFactor> strategies;
	private Vector<DiagramFactor> threats;
	private Vector<DiagramFactor> targets;
	private Vector<DiagramFactor> unlinked;
	
}
