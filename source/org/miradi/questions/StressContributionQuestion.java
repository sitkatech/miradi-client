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
package org.miradi.questions;

import org.miradi.main.EAM;

public class StressContributionQuestion extends ThreatRatingQuestion
{
	public StressContributionQuestion()
	{
		super(getDescription());
	}

	@Override
	protected String getLowRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Low:</b> The source is a <b>low contributor</b> of the particular stress.</html>");
	}
	
	@Override
	protected String getMediumRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Medium:</b> The source is a <b>moderate contributor</b> of the particular stress.</html>");
	}

	@Override
	protected String getHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>High:</b> The source is a <b>large contributor</b> of the particular stress.</html>");
	}

	@Override
	protected String getVeryHighRatingChoiceItemDescription()
	{
		return EAM.text("<html><b>Very High:</b> The source is a <b>very large contributor</b> of the particular stress.</html>");
	}
	
	private static String getDescription()
	{
		return EAM.text("<html><strong>Contribution - </strong>The expected contribution of " +
						"the source, acting alone, to the full expression of a stress (as determined " +
						"in the stress assessment) under current circumstances (i.e., given the " +
						"continuation of the existing management/conservation situation).</html>");
	}
	
	public static final String LOW_CODE = "1";
	public static final String MEDIUM_CODE = "2";
	public static final String HIGH_CODE = "3";
	public static final String VERY_HIGH_CODE = "4";	
}
