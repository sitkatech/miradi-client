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

package org.miradi.objects;

import java.util.Vector;

import org.miradi.diagram.ThreatTargetChainWalker;
import org.miradi.dialogs.threatrating.upperPanel.TargetThreatLinkTableModel;
import org.miradi.ids.BaseId;
import org.miradi.main.EAM;
import org.miradi.objecthelpers.ORef;
import org.miradi.objecthelpers.ORefSet;
import org.miradi.objecthelpers.ObjectType;
import org.miradi.objecthelpers.ThreatTargetVirtualLinkHelper;
import org.miradi.project.ObjectManager;
import org.miradi.project.Project;
import org.miradi.questions.ViabilityModeQuestion;
import org.miradi.utils.EnhancedJsonObject;

public class Dashboard extends BaseObject
{
	public Dashboard(ObjectManager objectManager, BaseId idToUse)
	{
		super(objectManager, idToUse);
		
		clear();
	}
		
	public Dashboard(ObjectManager objectManager, int idAsInt, EnhancedJsonObject json) throws Exception
	{
		super(objectManager, new BaseId(idAsInt), json);
	}
	
	@Override
	public int getType()
	{
		return getObjectType();
	}
	
	@Override
	public int[] getTypesThatCanOwnUs()
	{
		return NO_OWNERS;
	}
	
	public static int getObjectType()
	{
		return ObjectType.DASHBOARD;
	}
	
	@Override
	public String getTypeName()
	{
		return OBJECT_NAME;
	}

	@Override
	public String getPseudoData(String fieldTag)
	{
		try
		{
			if (fieldTag.equals(PSEUDO_TEAM_MEMBER_COUNT))
				return getObjectPoolCount(ProjectResource.getObjectType());

			if (fieldTag.equals(PSEUDO_PROJECT_SCOPE_WORD_COUNT))
				return getProjectScopeWordCount();

			if (fieldTag.equals(PSEUDO_TARGET_COUNT))
				return getObjectPoolCount(Target.getObjectType());

			if (fieldTag.equals(PSEUDO_HUMAN_WELFARE_TARGET_COUNT))
				return getObjectPoolCount(HumanWelfareTarget.getObjectType());

			if (fieldTag.equals(PSEUDO_TARGET_WITH_KEA_COUNT))
				return getTargetWithKeaCount();

			if (fieldTag.equals(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT))
				return getTargetWithSimpleViabilityCount();

			if (fieldTag.equals(PSEUDO_THREAT_COUNT))
				return getThreatCount();

			if (fieldTag.equals(PSEUDO_THREAT_WITH_TAXONOMY_COUNT))
				return getThreatWithTaxonomyCount();

			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_COUNT))
				return getThreatTargetLinkCount();

			if (fieldTag.equals(PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT))
				return getThreatTargetLinkWithRatingCount();

			return super.getPseudoData(fieldTag);
		}
		catch (Exception e)
		{
			EAM.logException(e);
			return EAM.text("Error Retrieving Data");
		}
	}
	
	private String getThreatTargetLinkCount()
	{
		Vector<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		int threatTargetCount = 0;
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			threatTargetCount += upstreamThreats.size();		
		}
		
		return Integer.toString(threatTargetCount);
	}
	
	private String getThreatTargetLinkWithRatingCount() throws Exception
	{
		ThreatTargetVirtualLinkHelper helper = new ThreatTargetVirtualLinkHelper(getProject());
		Vector<Target> targets = TargetThreatLinkTableModel.getOnlyTargetsInConceptualModelDiagrams(getProject());
		ThreatTargetChainWalker chain = new ThreatTargetChainWalker(getProject());
		int threatTargetWithRatingCount = 0;
		for(Target target : targets)
		{
			ORefSet upstreamThreats = chain.getUpstreamThreatRefsFromTarget(target);
			for(ORef threatRef : upstreamThreats)
			{
				int ratingValue = helper.calculateThreatRatingBundleValue(threatRef, target.getRef());
				if (ratingValue > 0)
					++threatTargetWithRatingCount;
			}
		}
		
		return Integer.toString(threatTargetWithRatingCount);
	}

	private String getThreatWithTaxonomyCount()
	{
		Vector<Cause> threats = getProject().getCausePool().getDirectThreatsAsVector();
		int count = 0;
		for(Cause threat : threats)
		{
			if (threat.getData(Cause.TAG_TAXONOMY_CODE).length() > 1)
				++count;
		}
		
		return Integer.toString(count);
	}

	private String getThreatCount()
	{
		int count = getProject().getCausePool().getDirectThreatsAsVector().size();
		
		return Integer.toString(count);
	}

	private String getTargetWithSimpleViabilityCount()
	{
		return getTargetCountForMode(ViabilityModeQuestion.SIMPLE_MODE_CODE);
	}

	private String getTargetWithKeaCount()
	{
		return getTargetCountForMode(ViabilityModeQuestion.TNC_STYLE_CODE);
	}

	private String getTargetCountForMode(String tNCSTYLECODE)
	{
		int count = 0;
		ORefSet targetRefs = getProject().getTargetPool().getRefSet();
		for (ORef targetRef : targetRefs)
		{
			Target target = Target.find(getProject(), targetRef);
			
			if (target.getViabilityMode().equals(tNCSTYLECODE))
				++count;
		}
		
		return Integer.toString(count);
	}

	private String getObjectPoolCount(int objectType)
	{
		int resourceCount = getProject().getPool(objectType).size();
		return Integer.toString(resourceCount);
	}

	private String getProjectScopeWordCount()
	{
		int scopeCount = getProject().getMetadata().getProjectScope().length();
		return Integer.toString(scopeCount);
	}

	public static boolean is(BaseObject object)
	{
		return is(object.getRef());
	}

	public static boolean is(ORef ref)
	{
		return is(ref.getObjectType());
	}
	
	public static boolean is(int objectType)
	{
		return objectType == getObjectType();
	}
	
	public static Dashboard find(ObjectManager objectManager, ORef ref)
	{
		return (Dashboard) objectManager.findObject(ref);
	}
	
	public static Dashboard find(Project project, ORef ref)
	{
		return find(project.getObjectManager(), ref);
	}
	
	@Override
	void clear()
	{
		super.clear();
		
		teamMemberCount = new PseudoStringData(PSEUDO_TEAM_MEMBER_COUNT);
		projectScopeWordCount = new PseudoStringData(PSEUDO_PROJECT_SCOPE_WORD_COUNT);
		targetCount = new PseudoStringData(PSEUDO_TARGET_COUNT);
		humanWelfareTargetCount = new PseudoStringData(PSEUDO_HUMAN_WELFARE_TARGET_COUNT);
		targetWithKeaCount = new PseudoStringData(PSEUDO_TARGET_WITH_KEA_COUNT);
		targetWithSimpleViabilityCount = new PseudoStringData(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT);
		threatCount = new PseudoStringData(PSEUDO_THREAT_COUNT);
		threatWithTaxonomyCount = new PseudoStringData(PSEUDO_THREAT_WITH_TAXONOMY_COUNT);
		threatTargetLinkCount = new PseudoStringData(PSEUDO_THREAT_TARGET_LINK_COUNT);
		threatTargetLinkWithRatingCount = new PseudoStringData(PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT);
		
		addPresentationDataField(PSEUDO_TEAM_MEMBER_COUNT, teamMemberCount);
		addPresentationDataField(PSEUDO_PROJECT_SCOPE_WORD_COUNT, projectScopeWordCount);
		addPresentationDataField(PSEUDO_TARGET_COUNT, targetCount);
		addPresentationDataField(PSEUDO_HUMAN_WELFARE_TARGET_COUNT, humanWelfareTargetCount);
		addPresentationDataField(PSEUDO_TARGET_WITH_KEA_COUNT, targetWithKeaCount);
		addPresentationDataField(PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT, targetWithSimpleViabilityCount);
		addPresentationDataField(PSEUDO_THREAT_COUNT, threatCount);
		addPresentationDataField(PSEUDO_THREAT_WITH_TAXONOMY_COUNT, threatWithTaxonomyCount);
		addPresentationDataField(PSEUDO_THREAT_TARGET_LINK_COUNT, threatTargetLinkCount);
		addPresentationDataField(PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT, threatTargetLinkWithRatingCount);
	}
	
	public static final String OBJECT_NAME = "Dashboard";
	
	public static final String PSEUDO_TEAM_MEMBER_COUNT = "TeamMemberCount";
	public static final String PSEUDO_PROJECT_SCOPE_WORD_COUNT = "ProjectScopeWordCount";
	public static final String PSEUDO_TARGET_COUNT = "TargetCount";
	public static final String PSEUDO_HUMAN_WELFARE_TARGET_COUNT = "HumanWelfareTargetCount";
	public static final String PSEUDO_TARGET_WITH_KEA_COUNT = "TargetWithKeaCount";
	public static final String PSEUDO_TARGET_WITH_SIMPLE_VIABILITY_COUNT = "TargetWithSimpleViabilityCount";
	public static final String PSEUDO_THREAT_COUNT = "ThreatCount";
	public static final String PSEUDO_THREAT_WITH_TAXONOMY_COUNT = "ThreatWithTaxonomyCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_COUNT = "ThreatTargetLinkCount";
	public static final String PSEUDO_THREAT_TARGET_LINK_WITH_RATING_COUNT = "ThreatTargetLinkWithRatingCount";
	
	private PseudoStringData teamMemberCount;
	private PseudoStringData projectScopeWordCount;
	private PseudoStringData targetCount;
	private PseudoStringData humanWelfareTargetCount;
	private PseudoStringData targetWithKeaCount;
	private PseudoStringData targetWithSimpleViabilityCount;
	private PseudoStringData threatCount;
	private PseudoStringData threatWithTaxonomyCount;
	private PseudoStringData threatTargetLinkCount;
	private PseudoStringData threatTargetLinkWithRatingCount;
}
