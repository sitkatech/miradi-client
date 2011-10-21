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

package org.miradi.dialogs.planning.upperPanel.rebuilder;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.Vector;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objects.AbstractTarget;
import org.miradi.objects.BaseObject;
import org.miradi.objects.DiagramObject;
import org.miradi.objects.Indicator;
import org.miradi.objects.KeyEcologicalAttribute;
import org.miradi.objects.Measurement;
import org.miradi.objects.PlanningTreeRowColumnProvider;
import org.miradi.objects.ProjectMetadata;
import org.miradi.project.Project;
import org.miradi.utils.BaseObjectDateDescendingAndIdComparator;

//FIXME urgent - Make new target viability tree table work
public class TargetViabilityTreeRebuilder extends AbstractTreeRebuilder
{
	public TargetViabilityTreeRebuilder(Project projectToUse, PlanningTreeRowColumnProvider rowColumnProviderToUse)
	{
		super(projectToUse, rowColumnProviderToUse);
	}

	@Override
	protected ORefList getChildRefs(ORef parentRef, DiagramObject diagram) throws Exception
	{
		if(ProjectMetadata.is(parentRef))
			return getChildrenOfProjectNode(parentRef);
		
		if(AbstractTarget.isAbstractTarget(parentRef))
			return getChildrenOfAbstractTargetNode(parentRef);
		
		if(KeyEcologicalAttribute.is(parentRef))
			return getChildrenOfKea(parentRef);
		
		if(Indicator.is(parentRef))
			return getChildrenOfIndicator(parentRef);
		
		EAM.logDebug("Don't know how to get children of " + parentRef);
		return new ORefList();
	}
	
	private ORefList getChildrenOfKea(ORef parentRef)
	{
		ORefList childRefs = new ORefList();
		KeyEcologicalAttribute kea = KeyEcologicalAttribute.find(getProject(), parentRef);
		childRefs.addAll(kea.getIndicatorRefs());
		
		return childRefs;
	}

	private ORefList getChildrenOfIndicator(ORef parentRef)
	{
		ORefList childRefs = new ORefList();
		Indicator indicator = Indicator.find(getProject(), parentRef);
		childRefs.addAll(getSortedByDateMeasurementRefs(indicator));
		//FIXME urgent - must add goal node here (Future status)
		
		return childRefs;
	}

	public ORefList getSortedByDateMeasurementRefs(Indicator indicator)
	{
		final ORefList measurementRefs = indicator.getMeasurementRefs();
		Vector<Measurement> measurements = new Vector<Measurement>();
		for(int index = 0; index < measurementRefs.size(); ++index)
		{
			measurementRefs.add(Measurement.find(getProject(), measurementRefs.get(index)));
		}
		
		Collections.sort(measurements, new MeasurementDateComparator());
		
		return new ORefList(measurements);
	}

	private ORefList getChildrenOfAbstractTargetNode(ORef parentRef) throws Exception
	{
		AbstractTarget abtractTarget = AbstractTarget.findTarget(getProject(), parentRef);
		if (abtractTarget.isViabilityModeTNC())
			return getSortedKeaRefs(abtractTarget);
		
		return abtractTarget.getOnlyDirectIndicatorRefs();
	}
	
	private ORefList getSortedKeaRefs(AbstractTarget target) throws Exception
	{
		Project project = target.getProject();
		ORefList keaRefs = target.getKeyEcologicalAttributeRefs();
		Vector<KeyEcologicalAttribute> keyEcologicalAttributesVector = new Vector<KeyEcologicalAttribute>();
		for(int index = 0; index < keaRefs.size(); ++index)
		{
			KeyEcologicalAttribute kea = (KeyEcologicalAttribute)project.findObject(keaRefs.get(index));
			keyEcologicalAttributesVector.add(kea);
		}
		
		Collections.sort(keyEcologicalAttributesVector, new KeaComparator());
		
		return new ORefList(keyEcologicalAttributesVector);
	}
	
	private ORefList getChildrenOfProjectNode(ORef parentRef) throws Exception
	{
		ORefList childRefs = new ORefList();
		childRefs.addAll(getProject().getTargetPool().getRefList());
		if(getProject().getMetadata().isHumanWelfareTargetMode())
			childRefs.addAll(getProject().getHumanWelfareTargetPool().getRefList());

		return childRefs;
	}
	
	private class KeaComparator implements Comparator<KeyEcologicalAttribute>
	{
		public int compare(KeyEcologicalAttribute kea1, KeyEcologicalAttribute kea2)
		{
			String type1 =kea1.getKeyEcologicalAttributeType();
			String type2 =kea2.getKeyEcologicalAttributeType();
			Collator myCollator = Collator.getInstance();

			return myCollator.compare(type1, type2);
		}
	}

	private class MeasurementDateComparator implements Comparator<BaseObject>
	{
		public int compare(BaseObject baseObject1, BaseObject baseObject2)
		{
			return BaseObjectDateDescendingAndIdComparator.compare(baseObject1, baseObject2, Measurement.TAG_DATE);
		}	
	}
}
