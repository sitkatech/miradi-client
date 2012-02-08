/* 
Copyright 2005-2011, Foundations of Success, Bethesda, Maryland 
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

import org.miradi.objects.Goal;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.ProjectMetadata;
import org.miradi.objects.Target;
import org.miradi.project.Project;
import org.miradi.utils.CodeList;

public class TargetViabilityRowColumnProvider extends AbstractViabilityRowColumnProvider
{
	public TargetViabilityRowColumnProvider(Project projectToUse)
	{
		super(projectToUse);
	}

	public CodeList getRowCodesToShow() throws Exception
	{
		return new CodeList(new String[] {
				ProjectMetadata.OBJECT_NAME, 
				Target.OBJECT_NAME,
				KeyEcologicalAttribute.OBJECT_NAME,
				Indicator.OBJECT_NAME,
				Measurement.OBJECT_NAME,
				Goal.OBJECT_NAME,
		});
	}
}
