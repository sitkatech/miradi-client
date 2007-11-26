/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.views.diagram.doers;

import java.text.ParseException;

import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objects.Factor;
import org.conservationmeasures.eam.objects.Stress;
import org.conservationmeasures.eam.objects.Target;
import org.conservationmeasures.eam.views.diagram.CreateAnnotationDoer;

public class CreateStressDoer extends CreateAnnotationDoer
{
	public boolean isAvailable()
	{
		//FIXME temporarly disabled the creation of stresses due to migration of threatStressRatings and new rules
		return false;
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
