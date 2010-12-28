/* 
Copyright 2005-2010, Foundations of Success, Bethesda, Maryland 
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

package org.miradi.xml.xmpz;

import org.miradi.objecthelpers.ORef;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.Goal;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.SubTarget;
import org.miradi.questions.StatusQuestion;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

abstract public class AbstractTargetPoolImporter extends FactorPoolImporter
{
	public AbstractTargetPoolImporter(XmpzXmlImporter importerToUse, String poolNameToUse, int objectTypeToImportToUse)
	{
		super(importerToUse, poolNameToUse, objectTypeToImportToUse);
	}
	
	@Override
	protected void importFields(Node node, ORef destinationRef)	throws Exception
	{
		super.importFields(node, destinationRef);

		importCodeField(node, destinationRef, AbstractTarget.TAG_TARGET_STATUS, new StatusQuestion());
		importCodeField(node, destinationRef, AbstractTarget.TAG_VIABILITY_MODE, new ViabilityModeQuestion());
		importField(node, destinationRef, AbstractTarget.TAG_CURRENT_STATUS_JUSTIFICATION);
		importRefs(node, XmpzXmlConstants.SUB_TARGET_IDS_ELEMENT, destinationRef, AbstractTarget.TAG_SUB_TARGET_REFS, SubTarget.getObjectType(), XmpzXmlConstants.SUB_TARGET);
		importIds(node, destinationRef, AbstractTarget.TAG_KEY_ECOLOGICAL_ATTRIBUTE_IDS, KeyEcologicalAttribute.getObjectType(), XmpzXmlConstants.KEY_ECOLOGICAL_ATTRIBUTE);
		importIds(node, destinationRef, AbstractTarget.TAG_GOAL_IDS, Goal.getObjectType(), XmpzXmlConstants.GOAL);	
		importIndicatorIds(node, destinationRef);
	}	
}
