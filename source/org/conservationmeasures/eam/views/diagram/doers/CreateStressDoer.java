/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.CreateThreatStressRatingParameter;
import org.conservationmeasures.eam.objecthelpers.FactorLinkSet;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.CreateAnnotationDoer;

public class CreateStressDoer extends CreateAnnotationDoer
{
	protected void doExtraWork(ORef newlyCreatedObjectRef) throws Exception
	{
		Factor selectedFactor = (Factor) getSelectedParent();
		createThreatStressRatingsForAttachedLinks(getProject(), newlyCreatedObjectRef, selectedFactor);
	}

	public static void createThreatStressRatingsForAttachedLinks(Project project, ORef newlyCreatedStressRef, Factor selectedFactor) throws Exception
	{
		if(!selectedFactor.isTarget())
			return;

		if (newlyCreatedStressRef.getObjectType() != Stress.getObjectType())
			return;
		
		Target target = (Target) selectedFactor;
		FactorLinkSet directThreatTargetLinks = target.getThreatTargetFactorLinks();
		for(FactorLink factorLink : directThreatTargetLinks)
		{
			CreateThreatStressRatingParameter extraInfo = new CreateThreatStressRatingParameter(newlyCreatedStressRef);
			CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType(), extraInfo);
			project.executeCommand(createThreatStressRating);
			
			CommandSetObjectData setLinkThreatStressRatingRefs = CommandSetObjectData.createAppendORefCommand(factorLink, FactorLink.TAG_THREAT_STRESS_RATING_REFS, createThreatStressRating.getObjectRef());
			project.executeCommand(setLinkThreatStressRatingRefs);			
		}
	}

	public int getAnnotationType()
	{
		return Stress.getObjectType();
	}
	public String getAnnotationListTag()
	{
		return Target.TAG_STRESS_REFS;
	}
	
	protected CommandSetObjectData createAppendCommand(Factor factor, ORef refToAppend) throws ParseException
	{
		return CommandSetObjectData.createAppendORefCommand(factor, getAnnotationListTag(), refToAppend);
	}
}
