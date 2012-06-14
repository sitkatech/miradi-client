/* 
Copyright 2005-2012, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz2.xmpz2schema;

import org.miradi.xml.xmpz2.Xmpz2XmlConstants;

abstract public class Xmpz2GroupedConstants implements Xmpz2XmlConstants
{

	public static String[] getLinkableFactorNames()
	{
		return new String[]{
				BIODIVERSITY_TARGET,
				HUMAN_WELFARE_TARGET, 
				CAUSE,
				STRATEGY,
				THREAT_REDUCTION_RESULTS, 
				INTERMEDIATE_RESULTS,
				GROUP_BOX,
		};
	}

	public static String[] getWrappableFactorNames()
	{
		return new String[]{
				BIODIVERSITY_TARGET, 
				HUMAN_WELFARE_TARGET, 
				CAUSE, 
				STRATEGY, 
				THREAT_REDUCTION_RESULTS, 
				INTERMEDIATE_RESULTS,
				GROUP_BOX,
				TEXT_BOX,
				SCOPE_BOX,
				ACTIVITY,
				STRESS,
		};
	}

	public static String[] getHtmlTagsAsElementNames()
	{
		return new String[] {"br", "b", "i", "u", "strike", "a", "ul", "ol",};
	}
}
