/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.text.ParseException;
import java.util.Vector;

import org.conservationmeasures.eam.commands.CommandCreateObject;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objects.Cause;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.FactorLink;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.objects.ThreatStressRating;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.views.diagram.CreateAnnotationDoer;

public class CreateStressDoer extends CreateAnnotationDoer
{
	protected void doExtraWork() throws Exception
	{
		Factor target = getSelectedFactor();
		if(!target.isTarget())
			return;
		
		Vector<FactorLink> directThreatTargetLinks = getDirectThreatTargetFactorLinks(getProject(), target);		
		for (int i = 0; i < directThreatTargetLinks.size(); ++i)
		{
			CommandCreateObject createThreatStressRating = new CommandCreateObject(ThreatStressRating.getObjectType());
			getProject().executeCommand(createThreatStressRating);
			
			FactorLink factorLink = directThreatTargetLinks.get(i);
			CommandSetObjectData setLinkThreatStressRatingRefs = CommandSetObjectData.createAppendORefCommand(factorLink, FactorLink.TAG_THREAT_STRESS_RATING_REFS, createThreatStressRating.getObjectRef());
			getProject().executeCommand(setLinkThreatStressRatingRefs);
		}
	}

	public static Vector<FactorLink> getDirectThreatTargetFactorLinks(Project project, Factor target)
	{
		ORefList linkRefsThatReferToUs = target.findObjectsThatReferToUs(FactorLink.getObjectType());
		Vector<FactorLink> directThreatTargetLinks = new Vector<FactorLink>();
		for (int i = 0; i < linkRefsThatReferToUs.size(); ++i)
		{
			ORef linkRef = linkRefsThatReferToUs.get(i);
			FactorLink factorLink = (FactorLink) project.findObject(linkRef);
			if (factorLink.getToFactorRef().getObjectType() == Cause.getObjectType())
				directThreatTargetLinks.add(factorLink);
		}
		return directThreatTargetLinks;
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
