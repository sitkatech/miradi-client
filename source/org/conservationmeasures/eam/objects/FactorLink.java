/* 
* Copyright 2005-2007, Wildlife Conservation Society, 
* Bronx, New York (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.conservationmeasures.eam.objects;

import java.util.Set;
import java.util.Vector;

import org.conservationmeasures.eam.commands.Command;
import org.conservationmeasures.eam.commands.CommandSetObjectData;
import org.conservationmeasures.eam.ids.FactorId;
import org.conservationmeasures.eam.ids.FactorLinkId;
import org.conservationmeasures.eam.main.EAM;
import org.conservationmeasures.eam.objectdata.BooleanData;
import org.conservationmeasures.eam.objectdata.ORefData;
import org.conservationmeasures.eam.objectdata.ORefListData;
import org.conservationmeasures.eam.objectdata.StringData;
import org.conservationmeasures.eam.objecthelpers.CreateFactorLinkParameter;
import org.conservationmeasures.eam.objecthelpers.CreateObjectParameter;
import org.conservationmeasures.eam.objecthelpers.ORef;
import org.conservationmeasures.eam.objecthelpers.ORefList;
import org.conservationmeasures.eam.objecthelpers.ObjectType;
import org.conservationmeasures.eam.project.ObjectManager;
import org.conservationmeasures.eam.project.Project;
import org.conservationmeasures.eam.utils.EnhancedJsonObject;

public class FactorLink extends BaseObject
{
	public FactorLink(ObjectManager objectManager, FactorLinkId id, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		super(objectManager, id);
		clear();
		setData(TAG_FROM_REF, fromFactorRef.toString());
		setData(TAG_TO_REF, toFactorRef.toString());
	}

	public FactorLink(FactorLinkId id, ORef fromFactorRef, ORef toFactorRef) throws Exception
	{
		super(id);
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
	
	
	public static boolean canReferToThisType(int type)
	{
		switch(type)
		{
			case ObjectType.THREAT_REDUCTION_RESULT:
			case ObjectType.INTERMEDIATE_RESULT:
			case ObjectType.CAUSE:
			case ObjectType.STRATEGY:
			case ObjectType.TARGET:
			case ObjectType.THREAT_STRESS_RATING:
				return true;
			default:
				return false;
		}
	}
	
	public Set<String> getReferencedObjectTags()
	{
		Set<String> set = super.getReferencedObjectTags();
		set.add(TAG_FROM_REF);
		set.add(TAG_TO_REF);
		set.add(TAG_THREAT_STRESS_RATING_REFS);
		return set;
	}
	
	public ORef getFromFactorRef()
	{
		ORef rawRef = fromRef.getRawRef();
		return new ORef(rawRef.getObjectType(), new FactorId(rawRef.getObjectId().asInt()));
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
	
	public String getStressLabel()
	{
		return stressLabel.get();
	}
	
	public ORef getDownstreamTargetRef() throws Exception
	{
		if (getToFactorRef().getObjectType() == Target.getObjectType())
			return getToFactorRef();
		
		if (getFromFactorRef().getObjectType() == Target.getObjectType() && isBidirectional())
			return getFromFactorRef();
		
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
	
	public Command[] createCommandsToLoadFromJson(EnhancedJsonObject json) throws Exception
	{
		Vector commandsToLoadFromJson = new Vector();
		CommandSetObjectData setBidirectionalCommand = new CommandSetObjectData(getRef(), TAG_BIDIRECTIONAL_LINK, json.getString(TAG_BIDIRECTIONAL_LINK));
		commandsToLoadFromJson.add(setBidirectionalCommand);

		CommandSetObjectData setStressCommand = new CommandSetObjectData(getRef(), TAG_STRESS_LABEL, json.getString(TAG_STRESS_LABEL));
		commandsToLoadFromJson.add(setStressCommand);

		return (Command[]) commandsToLoadFromJson.toArray(new Command[0]);
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
		Target target = Target.findTarget(getObjectManager(), getDownstreamTargetRef());
		ORefList stresses = target.getStressRefs();
		if (stresses.size() == 0)
			return 0;
		
		int totalStressRatings = 0;
		for (int i = 0; i < stresses.size(); ++i)
		{
			Stress stress = Stress.findStress(getObjectManager(), stresses.get(i));
			totalStressRatings = totalStressRatings + stress.calculateStressRating();
		}

		return totalStressRatings/stresses.size();
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
	
	public static FactorLink findFactorLink(ObjectManager objectManager, ORef factorLinkRef)
	{
		return (FactorLink) objectManager.findObject(factorLinkRef);
	}
	
	public static FactorLink findFactorLink(Project project, ORef factorLinkRef)
	{
		return findFactorLink(project.getObjectManager(), factorLinkRef);
	}
	
	void clear()
	{
		super.clear();
		fromRef = new ORefData();
		toRef = new ORefData();
		stressLabel = new StringData();
		bidirectionalLink = new BooleanData();
		threatStressRatingRefs = new ORefListData();
		comment = new StringData();
		pseudoThreatRatingBundleValue = new PseudoStringData(PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE);
		
		addNoClearField(TAG_FROM_REF, fromRef);
		addNoClearField(TAG_TO_REF, toRef);
		addField(TAG_STRESS_LABEL, stressLabel);
		addField(TAG_BIDIRECTIONAL_LINK, bidirectionalLink);
		addField(TAG_THREAT_STRESS_RATING_REFS, threatStressRatingRefs);
		addField(TAG_COMMENT, comment);
		addField(PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE, pseudoThreatRatingBundleValue);
	}
	
	
	public static final String TAG_FROM_REF = "FromRef";
	public static final String TAG_TO_REF = "ToRef";
	public static final String TAG_STRESS_LABEL = "StressLabel";
	public static final String TAG_BIDIRECTIONAL_LINK = "BidirectionalLink";
	public static final String TAG_THREAT_STRESS_RATING_REFS = "ThreatStressRatingRefs";
	public static final String TAG_COMMENT = "Comment";
	public static final String PSEUDO_TAG_THREAT_RATING_BUNDLE_VALUE = "PseudoThreatRatingBundleValue";
	
	public static final String OBJECT_NAME = "Link";
	public static final String OBJECT_NAME_TARGETLINK = "Targetlink";
	public static final String OBJECT_NAME_STRESS = "Stress";
	
	public static final int FROM = 1;
	public static final int TO = 2;
	public static final String BIDIRECTIONAL_LINK = BooleanData.BOOLEAN_TRUE;
	
	private ORefData fromRef;
	private ORefData toRef;
	private StringData stressLabel;
	private BooleanData bidirectionalLink;
	private ORefListData threatStressRatingRefs;
	private StringData comment;
	private PseudoStringData pseudoThreatRatingBundleValue;
}
