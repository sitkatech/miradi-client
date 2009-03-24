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

public class ThreatStressRatingEnsurer implements CommandExecutedListener 
{
	public ThreatStressRatingEnsurer(Project projectToUse)
	{
		project = projectToUse;
	}
	
	public void disable()
	{
		getProject().removeCommandExecutedListener(this);
	}

	public void enable()
	{
		getProject().addCommandExecutedListener(this);
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
			HashSet<ThreatStressPair> thisThreatStressPairs = createThreatStressPairs(upstreamThreatRefs, stressRefs);
			threatStressPairs.addAll(thisThreatStressPairs);			
		}
		
		createOrDeleteThreatStressRatingsAsNeeded(threatStressPairs);
	}

	private void createOrDeleteThreatStressRatingsAsNeeded(HashSet<ThreatStressPair> desiredThreatStressPairs) throws Exception
	{
		HashSet<ThreatStressPair> existingThreatStressPairs = createThreatStressFromPoolPairs();
		createThreatStressRatings(desiredThreatStressPairs, existingThreatStressPairs);
		deleteThreatStressRatings(desiredThreatStressPairs, existingThreatStressPairs);
	}

	private void createThreatStressRatings(HashSet<ThreatStressPair> desiredThreatStressPairs, HashSet<ThreatStressPair> existingThreatStressPairs) throws Exception
	{
		HashSet<ThreatStressPair> toCreate = new HashSet(desiredThreatStressPairs);
		toCreate.removeAll(existingThreatStressPairs);
		for(ThreatStressPair threatStressPair : toCreate)
		{
			ORef stressRef = threatStressPair.getStressRef();
			ORef threatRef = threatStressPair.getThreatRef();
			CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(stressRef, threatRef);
			CommandCreateObject createThreatStressRatingCommand = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
			getProject().executeCommand(createThreatStressRatingCommand);
		}
	}
	
	private void deleteThreatStressRatings(HashSet<ThreatStressPair> desiredThreatStressPairs, HashSet<ThreatStressPair> existingThreatStressPairs) throws Exception
	{
		HashSet<ThreatStressPair> toDelete = new HashSet(existingThreatStressPairs);
		toDelete.removeAll(desiredThreatStressPairs);
		for(ThreatStressPair threatStressPair : toDelete)
		{		
			ORef threatStressRatingToDelete = threatStressPair.findMatchingThreatStressRating();
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingToDelete);
			deleteThreatStressRating(threatStressRating);
		}
	}
	
	private void deleteThreatStressRating(ThreatStressRating threatStressRating) throws Exception
	{
		Vector<Command> commandsToDeleteThreatStressRating = new Vector();
		commandsToDeleteThreatStressRating.addAll(threatStressRating.createCommandsToClearAsList());
		commandsToDeleteThreatStressRating.add(new CommandDeleteObject(threatStressRating));
		
		getProject().executeCommandsWithoutTransaction(commandsToDeleteThreatStressRating);
	}

	private HashSet<ThreatStressPair> createThreatStressFromPoolPairs()
	{
		ORefSet allThreatStressRatingRefs = getProject().getThreatStressRatingPool().getRefSet();
		HashSet<ThreatStressPair> threatStressPairs = new HashSet();
		for(ORef threatStressRatingRef : allThreatStressRatingRefs)
		{
			ThreatStressRating threatStressRating = ThreatStressRating.find(getProject(), threatStressRatingRef);
			threatStressPairs.add(new ThreatStressPair(threatStressRating));
		}
		
		return threatStressPairs;
	}
	
	private HashSet<ThreatStressPair> createThreatStressPairs(ORefSet threatRefs, ORefSet stressRefs)
	{
		HashSet<ThreatStressPair> threatStressPairs = new HashSet();
		for(ORef threatRef : threatRefs)
		{
			for(ORef stressRef : stressRefs)
			{
				threatStressPairs.add(new ThreatStressPair(threatRef, stressRef));
			}
		}
		
		return threatStressPairs;
	}

	public void commandExecuted(CommandExecutedEvent event)
	{
		try
		{
			if (isThreatStressRatingAffectingCommand(event))
				createOrDeleteThreatStressRatingsAsNeeded();
		}
		catch (Exception e)
		{
			EAM.logException(e);
		}
	}

	private boolean isThreatStressRatingAffectingCommand(CommandExecutedEvent event)
	{
		if (event.isDeleteCommandForThisType(FactorLink.getObjectType()))
			return true;
		
		if (event.isCreateCommandForThisType(FactorLink.getObjectType()))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(Target.getObjectType(), Target.TAG_STRESS_REFS))
			return true;
		
		if (event.isSetDataCommandWithThisTypeAndTag(Cause.getObjectType(), Cause.TAG_IS_DIRECT_THREAT))
			return true;
				
		return false;
	}
	
	private Project getProject()
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
			ORefList tsrReferrerRefsToStress = getTsrReferrerRefsToStress();
			ORefList tsrReferrerRefsToThreat = getTsrReferrerRefsToThreat();
			ORefList overLappingRefs = tsrReferrerRefsToStress.getOverlappingRefs(tsrReferrerRefsToThreat);
			
			return overLappingRefs.getRefForType(ThreatStressRating.getObjectType());
		}

		private ORefList getTsrReferrerRefsToStress()
		{
			Stress stress = Stress.find(getProject(), getStressRef());
			return stress.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		}
		
		private ORefList getTsrReferrerRefsToThreat()
		{
			Cause threat = Cause.find(getProject(), getThreatRef());
			return threat.findObjectsThatReferToUs(ThreatStressRating.getObjectType());
		}
		
		public ORef getThreatRef()
		{
			return threatRef;
		}
		
		public ORef getStressRef()
		{
			return stressRef;
		}
		
		@Override
		public boolean equals(Object rawOther)
		{
			if (!(rawOther instanceof ThreatStressPair))
				return false;
			
			ThreatStressPair other = (ThreatStressPair) rawOther;
			if (!getThreatRef().equals(other.getThreatRef()))
				return false;
			
			if (!getStressRef().equals(other.getStressRef()))
				return false;
			
			return true;
		}
		
		@Override
		public int hashCode()
		{
			return getThreatRef().hashCode() + getStressRef().hashCode();
		}
		
		//TODO,  remove this, was used for debugger output
		@Override
		public String toString()
		{
			return getThreatRef() + " - " + getStressRef();
		}
		
		private ORef threatRef;
		private ORef stressRef;
	}
	
	private Project project;
}
