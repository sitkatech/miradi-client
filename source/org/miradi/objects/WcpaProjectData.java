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
package org.miradi.objects;

import org.miradi.ids.BaseId;
import org.miradi.objectdata.StringData;
import org.miradi.objectdata.UserTextData;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.utils.EnhancedJsonObject;

public class WcpaProjectData extends BaseObject
{
	public WcpaProjectData(ObjectManager objectManager, BaseId id)
	{
		super(objectManager, id);
		clear();
	}
	
	public WcpaProjectData(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new BaseId(idAsInt), jsonObject);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}

	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.WCPA_PROJECT_DATA;
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
	
	@Override
	void clear()
	{
		super.clear();
		
		legalStatus = new UserTextData(TAG_LEGAL_STATUS);
		legislativeContext = new UserTextData(TAG_LEGISLATIVE);
		physicalDescription = new UserTextData(TAG_PHYSICAL_DESCRIPTION);
		biologicalDescription = new UserTextData(TAG_BIOLOGICAL_DESCRIPTION);
		socioEconomicInformation = new UserTextData(TAG_SOCIO_ECONOMIC_INFORMATION);
		historicalDescription = new UserTextData(TAG_HISTORICAL_DESCRIPTION);
		culturalDescription = new UserTextData(TAG_CULTURAL_DESCRIPTION);
		accessInformation = new UserTextData(TAG_ACCESS_INFORMATION);
		visitationInformation = new UserTextData(TAG_VISITATION_INFORMATION);
		currentLandUses = new UserTextData(TAG_CURRENT_LAND_USES);
		managementResources = new UserTextData(TAG_MANAGEMENT_RESOURCES);
		
		addField(TAG_LEGAL_STATUS, legalStatus);
		addField(TAG_LEGISLATIVE, legislativeContext);
		addField(TAG_PHYSICAL_DESCRIPTION, physicalDescription);
		addField(TAG_BIOLOGICAL_DESCRIPTION, biologicalDescription);
		addField(TAG_SOCIO_ECONOMIC_INFORMATION, socioEconomicInformation);
		addField(TAG_HISTORICAL_DESCRIPTION, historicalDescription);
		addField(TAG_CULTURAL_DESCRIPTION, culturalDescription);
		addField(TAG_ACCESS_INFORMATION, accessInformation);
		addField(TAG_VISITATION_INFORMATION, visitationInformation);
		addField(TAG_CURRENT_LAND_USES, currentLandUses);
		addField(TAG_MANAGEMENT_RESOURCES, managementResources);
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
		
	private StringData legalStatus;
	private StringData legislativeContext;
	private StringData physicalDescription;
	private StringData biologicalDescription;
	private StringData socioEconomicInformation;
	private StringData historicalDescription;
	private StringData culturalDescription;
	private StringData accessInformation;
	private StringData visitationInformation;
	private StringData currentLandUses;
	private StringData managementResources;
	
	public static final String OBJECT_NAME = "WCPAProjectData";
}
