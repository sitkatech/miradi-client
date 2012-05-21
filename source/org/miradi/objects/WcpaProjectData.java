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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objecthelpers.ORef;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.schemas.WcpaProjectDataSchema;

public class WcpaProjectData extends BaseObject
{
	public WcpaProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id, createSchema());
	}

	public static WcpaProjectDataSchema createSchema()
	{
		return new WcpaProjectDataSchema();
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static boolean canReferToThisType(int type)
	{
		return false;
	}
	
	public static WcpaProjectData find(ObjectManager objectManager, ORef wcpsProjectDataRef)
	{
		return (WcpaProjectData) objectManager.findObject(wcpsProjectDataRef);
	}
	
	public static WcpaProjectData find(Project project, ORef wcpaProjectDataRef)
	{
		return find(project.getObjectManager(), wcpaProjectDataRef);
	}
	
	public final static String TAG_LEGAL_STATUS = "LegalStatus";
	public final static String TAG_LEGISLATIVE = "LegislativeContext";
	public final static String TAG_PHYSICAL_DESCRIPTION = "PhysicalDescription";
	public final static String TAG_BIOLOGICAL_DESCRIPTION = "BiologicalDescription";
	public final static String TAG_SOCIO_ECONOMIC_INFORMATION = "SocioEconomicInformation";
	public final static String TAG_HISTORICAL_DESCRIPTION = "HistoricalDescription";
	public final static String TAG_CULTURAL_DESCRIPTION = "CulturalDescription";
	public final static String TAG_ACCESS_INFORMATION = "AccessInformation";
	public final static String TAG_VISITATION_INFORMATION = "VisitationInformation";
	public final static String TAG_CURRENT_LAND_USES = "CurrentLandUses";
	public final static String TAG_MANAGEMENT_RESOURCES = "ManagementResources";
}
