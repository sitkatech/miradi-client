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
package org.miradi.objects;

import java.util.Vector;

import org.miradi.ids.FactorId;
import org.miradi.ids.FactorLinkId;
import org.miradi.main.EAM;
import org.miradi.objectdata.BooleanData;
import org.miradi.objectdata.ORefData;
import org.miradi.objectdata.ORefListData;
import org.miradi.objectdata.StringData;
import org.miradi.objecthelpers.CreateFactorLinkParameter;
import org.miradi.objecthelpers.CreateObjectParameter;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefList;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.project.threatrating.SimpleThreatRatingFramework;
import org.miradi.project.threatrating.ThreatRatingBundle;
import org.miradi.utils.EnhancedJsonObject;
import org.miradi.utils.Utility;

public class FactorLink extends BaseObject
{
	public FactorLink(ObjectManager objectManager, FactorLinkId id, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		super(objectManager, id);
		clear();
		setData(TAG_FROM_REF, fromFactorRef.toString());
		setData(TAG_TO_REF, toFactorRef.toString());
	}

	public FactorLink(ObjectManager objectManager, int idAsInt, EnhancedJsonObject jsonObject) throws Exception 
	{
		super(objectManager, new FactorLinkId(idAsInt), jsonObject);
	}
	
	public int getType()
	{
		return getObjectType();
	}

	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	public static int getObjectType()
	{
		return ObjectType.FACTOR_LINK;
	}
	
	
	public static boolean canOwnThisType(int type)
	{
		return false;
	}
	
	
	public ORefList getAllObjectsToDeepCopy()
	{
		ORefList deepObjectRefsToCopy = super.getAllObjectsToDeepCopy();
		deepObjectRefsToCopy.addAll(getThreatStressRatingRefs());
		
		return deepObjectRefsToCopy;
	}
			
	public Factor getFromFactor()
	{
		return Factor.findFactor(getProject(), getFromFactorRef());
	}
	
	public ORef getFromFactorRef()
	{
		ORef rawRef = fromRef.getRawRef();
		return new ORef(rawRef.getObjectType(), new FactorId(rawRef.getObjectId().asInt()));
	}
	
	public Factor getToFactor()
	{
		return Factor.findFactor(getProject(), getToFactorRef());
	}
	
	public ORef getToFactorRef()
	{
		ORef rawRef = toRef.getRawRef();
		return new ORef(rawRef.getObjectType(), new FactorId(rawRef.getObjectId().asInt()));
	}
	
	public FactorLinkId getFactorLinkId()
	{
		return new FactorLinkId(getId().asInt());
	}
	
	public ORef getDownstreamTargetRef() throws Exception
	{
		if (getToFactorRef().getObjectType() == Target.getObjectType())
			return getToFactorRef();
		
		if (getFromFactorRef().getObjectType() == Target.getObjectType() && isBidirectional())
			return getFromFactorRef();
		
		throw new Exception();
	}
	
	public ORef getUpstreamThreatRef() throws Exception
	{
		if (Cause.is(getFromFactorRef()))
			return getFromFactorRef();
		
		if (Cause.is(getToFactorRef()) && isBidirectional())
			return getToFactorRef();
		
		throw new Exception();
	}
	
	public boolean isThreatTargetLink()
	{
		if (isThreatToTarget(getFromFactorRef(), getToFactorRef()))
			return true;
		
		if (isThreatToTarget(getToFactorRef(), getFromFactorRef()) && isBidirectional())
			return true;
		
		return false;
	}
	
	private boolean isThreatToTarget(ORef upstreamRef, ORef downstreamRef)
	{
		if (upstreamRef.getObjectType() != Cause.getObjectType())
			return false;
		
		if (downstreamRef.getObjectType() != Target.getObjectType())
			return false;
		
		return true;
	}
	
	public boolean isBidirectional()
	{
		return bidirectionalLink.get().equals(BIDIRECTIONAL_LINK);
	}
	
	public boolean isTargetLink()
	{
		 if (((Factor) objectManager.findObject(getToFactorRef())).isTarget())
			 return true;
		 
		 if (!isBidirectional())
			 return false;
			 
		 return (((Factor) objectManager.findObject(getFromFactorRef())).isTarget());
	}
	
	public boolean isRefList(String tag)
	{
		if (tag.equals(TAG_THREAT_STRESS_RATING_REFS))
			return true;
			
		return false;
	}
	
	public int getAnnotationType(String tag)
	{
		if (tag.equals(TAG_THREAT_STRESS_RATING_REFS))
			return ThreatStressRating.getObjectType();
		
		return super.getAnnotationType(tag);
	}

	public CreateObjectParameter getCreationExtraInfo()
	{
		Factor fromFactor = (Factor) objectManager.findObject(getFromFactorRef());
		Factor toFactor = (Factor) objectManager.findObject(getToFactorRef());
		return new CreateFactorLinkParameter(fromFactor.getRef(), toFactor.getRef());
	}

	public String getPseudoData(String fieldTag)
	{
		if (fieldTag.equals(PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE))
			return getCalculatedThreatRatingBundleValue();
			
		return super.getPseudoData(fieldTag);
	}
	
	public String getCalculatedThreatRatingBundleValue()
	{
		try
		{
			int calculatedThreatRatingBundleValue = calculateThreatRatingBundleValue();
			if (calculatedThreatRatingBundleValue == 0)
				return "";
			
			return Integer.toString(calculatedThreatRatingBundleValue);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return "Error";
		}
	}
	
	public int calculateThreatRatingBundleValue() throws Exception
	{
		if(getProject().isStressBaseMode())
			return calculateStressBasedThreatRating();

		return calculateSimpleThreatRating();
	}

	private int calculateSimpleThreatRating() throws Exception
	{
		SimpleThreatRatingFramework framework = getProject().getSimpleThreatRatingFramework();
		ORef threatRef = getUpstreamThreatRef();
		ORef targetRef = getDownstreamTargetRef();
		ThreatRatingBundle bundle = framework.getBundle(threatRef, targetRef);
		ValueOption valueOption = framework.getBundleValue(bundle);
		return valueOption.getNumericValue();
	}

	private int calculateStressBasedThreatRating()
	{
		ORefList ratingRefs = getThreatStressRatingRefs();
		Vector<Integer> ratingBundleValues = new Vector();
		for (int i = 0; i < ratingRefs.size(); ++i)
		{
			ThreatStressRating rating = ThreatStressRating.find(getObjectManager(), ratingRefs.get(i));
			if (rating.isActive())
				ratingBundleValues.add(rating.calculateThreatRating());
		}

		return getProject().getStressBasedThreatFormula().getHighestRatingRule(Utility.convertToIntArray(ratingBundleValues));
	}
	
	public ORef findThreatStressRatingReferringToStress(ORef stressRef) throws Exception
	{
		ORefList threatStressRatingRefsToUse = getThreatStressRatingRefs();
		for(int index = 0; index < threatStressRatingRefsToUse.size(); ++index)
		{
			ORef threatStressRatingRef = threatStressRatingRefsToUse.get(index);
			ThreatStressRating threatStressRating = (ThreatStressRating) getProject().findObject(threatStressRatingRef);
			if (stressRef.equals(threatStressRating.getStressRef()))
				return threatStressRatingRef;
		}
		
		throw new Exception("Stress has no matching Threat Stress Rating.  Stress ref = " + stressRef); 
	}
	
	public static String getCommentTagForMode(Project project)
	{
		if (project.isStressBaseMode())
			return FactorLink.TAG_COMMENT;
		
		return FactorLink.TAG_SIMPLE_THREAT_RATING_COMMENT;
	}
	
	public ORef getFactorRef(int direction)
	{
		if(direction == FROM)
			return getFromFactorRef();
		if(direction == TO)
			return getToFactorRef();
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	
	public ORef getOppositeFactorRef(int direction)
	{
		if(direction == FROM)
			return getFactorRef(TO);
		if(direction == TO)
			return getFactorRef(FROM);
		throw new RuntimeException("Link: Unknown direction " + direction);
	}
	
	public ORefList getThreatStressRatingRefs()
	{
		return threatStressRatingRefs.getORefList();
	}
	
	public static boolean is(BaseObject object)
	{
		return is(object.getType());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static FactorLink find(ObjectManager objectManager, ORef factorLinkRef)
	{
		return (FactorLink) objectManager.findObject(factorLinkRef);
	}
	
	public static FactorLink find(Project project, ORef factorLinkRef)
	{
		return find(project.getObjectManager(), factorLinkRef);
	}
	
	void clear()
	{
		super.clear();
		fromRef = new ORefData(TAG_FROM_REF);
		toRef = new ORefData(TAG_TO_REF);
		bidirectionalLink = new BooleanData(TAG_BIDIRECTIONAL_LINK);
		threatStressRatingRefs = new ORefListData(TAG_THREAT_STRESS_RATING_REFS);
		comment = new StringData(TAG_COMMENT);
		simpleThreatRatingComment = new StringData(TAG_SIMPLE_THREAT_RATING_COMMENT);
		pseudoThreatRatingBundleValue = new PseudoStringData(PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE);
		
		addNoClearField(TAG_FROM_REF, fromRef);
		addNoClearField(TAG_TO_REF, toRef);
		addField(TAG_BIDIRECTIONAL_LINK, bidirectionalLink);
		addField(TAG_THREAT_STRESS_RATING_REFS, threatStressRatingRefs);
		addField(TAG_COMMENT, comment);
		addField(TAG_SIMPLE_THREAT_RATING_COMMENT, simpleThreatRatingComment);
		addField(PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, pseudoThreatRatingBundleValue);
	}
	
	
	public static final String TAG_FROM_REF = "FromRef";
	public static final String TAG_TO_REF = "ToRef";
	public static final String TAG_BIDIRECTIONAL_LINK = "BidirectionalLink";
	public static final String TAG_THREAT_STRESS_RATING_REFS = "ThreatStressRatingRefs";
	public static final String TAG_COMMENT = "Comment";
	public static final String TAG_SIMPLE_THREAT_RATING_COMMENT = "SimpleThreatRatingComment";
	public static final String PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE = "PseudoThreatRatingBundleValue";
	
	public static final String OBJECT_NAME = "Link";
	public static final String OBJECT_NAME_TARGETLINK = "Targetlink";
	public static final String OBJECT_NAME_STRESS = "Stress";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	public static final String BIDIRECTIONAL_LINK = BooleanData.BOOLEAN_TRUE;
	
	private ORefData fromRef;
	private ORefData toRef;
	private BooleanData bidirectionalLink;
	private ORefListData threatStressRatingRefs;
	private StringData comment;
	private StringData simpleThreatRatingComment;
	private PseudoStringData pseudoThreatRatingBundleValue;
}
