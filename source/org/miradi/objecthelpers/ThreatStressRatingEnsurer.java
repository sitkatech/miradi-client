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
package org.miradi.objecthelpers;

import java.util.HashSet;
import java.util.Vector;

import org.miradi.commands.Command;
import org.miradi.commands.CommandCreateObject;
import org.miradi.commands.CommandDeleteObject;
import org.miradi.diagram.ThreatTargetChainObject;
import org.miradi.main.CommandExecutedEvent;
import org.miradi.main.CommandExecutedListener;
import org.miradi.main.EAM;
import org.miradi.objects.Cause;
import org.miradi.objects.FactorLink;
import org.miradi.objects.Stress;
import org.miradi.objects.Target;
import org.miradi.objects.ThreatStressRating;
import org.miradi.project.Project;

//FIXME this class under development along with its test
public class ThreatStressRatingEnsurer implements CommandExecutedListener 
{
	public ThreatStressRatingEnsurer(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void dispose()
	{
		getProject().removeCommandExecutedListener(this);
	}
	
	public void createOrDeleteThreatStressRatingsAsNeeded() throws Exception
	{
		HashSet<ThreatStressPair> threatStressPairs = new HashSet();
		ThreatTargetChainObject chainObject = new ThreatTargetChainObject(getProject());
		ORefList allTargetRefs = getProject().getTargetPool().getRefList();
		for (int index = 0; index < allTargetRefs.size(); ++index)
		{
			Target target = Target.find(getProject(), allTargetRefs.get(index));
			ORefSet upstreamThreatRefs = chainObject.getUpstreamThreatRefsFromTarget(target);
			ORefSet stressRefs = new ORefSet(target.getStressRefs());
			threatStressPairs.addAll(createThreatStressHolders(upstreamThreatRefs, stressRefs));			
		}
		
		createOrDeleteThreatStressRatingsAsNeeded(threatStressPairs);
	}

	private void createOrDeleteThreatStressRatingsAsNeeded(HashSet<ThreatStressPair> createdThreatStressHolders) throws Exception
	{
		getProject().endCommandSideEffectMode();
		try
		{
			ORefSet allThreatStressRatingRefs = getProject().getThreatStressRatingPool().getRefSet();
			HashSet<ThreatStressPair> existingThreatStressHolders = createThreatStressHolders(allThreatStressRatingRefs);
			if (createdThreatStressHolders.size() > existingThreatStressHolders.size())
				createThreatStressRatings(createdThreatStressHolders, existingThreatStressHolders);

			if (createdThreatStressHolders.size() < existingThreatStressHolders.size())
				deleteThreatStressRatings(createdThreatStressHolders, existingThreatStressHolders);
		}
		finally
		{
			getProject().beginCommandSideEffectMode();
		}
	}

	private void createThreatStressRatings(HashSet<ThreatStressPair> createdThreatStressHolders, HashSet<ThreatStressPair> existingThreatStressHolders) throws Exception
	{
		createdThreatStressHolders.removeAll(existingThreatStressHolders);
		for(ThreatStressPair threatStressHolder : createdThreatStressHolders)
		{
			ORef stressRef = threatStressHolder.getStressRef();
			ORef threatRef = threatStressHolder.getThreatRef();
			CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef, threatRef);
			CommandCreateObject createThreatStressRatingCommand = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
			getProject().executeCommand(createThreatStressRatingCommand);
		}
	}
	
	private void deleteThreatStressRatings(HashSet<ThreatStressPair> createdThreatStressHolders, HashSet<ThreatStressPair> existingThreatStressHolders) throws Exception
	{
		existingThreatStressHolders.removeAll(createdThreatStressHolders);
		for(ThreatStressPair threatStressHolder : existingThreatStressHolders)
		{		
			ORef threatStressRatingToDelete = threatStressHolder.findMatchingThreatStressRating();
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingToDelete);
			deleteThreatStressRating(threatStressRating);
		}
	}

	private HashSet<ThreatStressPair> createThreatStressHolders(ORefSet threatStressRatingRefs)
	{
		HashSet<ThreatStressPair> threatStressHolders = new HashSet();
		for(ORef threatStressRatingRef : threatStressRatingRefs)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingRef);
			threatStressHolders.add(new ThreatStressPair(threatStressRating));
		}
		
		return threatStressHolders;
	}
	
	private HashSet<ThreatStressPair> createThreatStressHolders(ORefSet threatRefs, ORefSet stressRefs)
	{
		HashSet<ThreatStressPair> threatStressHolders = new HashSet();
		for(ORef threatRef : threatRefs)
		{
			for(ORef stressRef : stressRefs)
			{
				threatStressHolders.add(new ThreatStressPair(threatRef, stressRef));
			}
		}
		
		return threatStressHolders;
	}

	private void deleteThreatStressRating(ThreatStressRating threatStressRating) throws Exception
	{
		Vector<Command> commansToDeleteThreatStressRating = new Vector();
		commansToDeleteThreatStressRating.addAll(threatStressRating.createCommandsToClearAsList());
		commansToDeleteThreatStressRating.add(new CommandDeleteObject(threatStressRating));

		getProject().executeCommandsWithoutTransaction(commansToDeleteThreatStressRating);
	}
	
	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			//FIXME need to add other types and create events here
			if (event.isDeleteCommandForThisType(FactorLink.getObjectType()))
				createOrDeleteThreatStressRatingsAsNeeded();
			if (event.isCreateCommandForThisType(FactorLink.getObjectType()))
				createOrDeleteThreatStressRatingsAsNeeded();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}
	
	public Project getProject()
	{
		return project;
	}
	
	class ThreatStressPair 
	{		
		public ThreatStressPair(ThreatStressRating threatStressRating)
		{
			this(threatStressRating.getThreatRef(), threatStressRating.getStressRef());
		}

		public ThreatStressPair(ORef threatRefToUse, ORef stressRefToUse)
		{
			threatRefToUse.ensureType(Cause.getObjectType());
			stressRefToUse.ensureType(Stress.getObjectType());
			
			threatRef = threatRefToUse;
			stressRef = stressRefToUse;
		}
		
		public ORef findMatchingThreatStressRating()
		{
			Stress stress = Stress.find(getProject(), getStressRef());
			ORefList tsrReferrerRefsToStress = stress.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
			
			Cause threat = Cause.find(getProject(), getThreatRef());
			ORefList tsrReferrerRefsToThreat = threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
			
			tsrReferrerRefsToStress.removeAll(tsrReferrerRefsToThreat);
			
			return tsrReferrerRefsToStress.getRefForType(ThreatStressRating.getObjectType());
		}
		
		public ORef getThreatRef()
		{
			return threatRef;
		}
		
		public ORef getStressRef()
		{
			return stressRef;
		}
		
		private ORef threatRef;
		private ORef stressRef;
	}
	
	private Project project;
}
