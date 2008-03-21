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
package org.miradi.dialogs.viability;

import org.miradi.ids.FactorId;
import org.miradi.project.Project;


//TODO: There should be no need to re case this as a root...the system should be able to determine the root by some other means the a null return on this mehtod
public class TargetViabilityRoot extends TargetViabilityNode
{
	public TargetViabilityRoot(Project projectToUse, FactorId targetId) throws Exception
	{
		super(projectToUse, targetId);
	}

	public boolean isAlwaysExpanded()
	{
		return true;
	}
}
