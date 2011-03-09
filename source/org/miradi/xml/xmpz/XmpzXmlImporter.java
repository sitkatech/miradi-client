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

import java.util.Vector;

import javax.xml.namespace.NamespaceContext;

import org.miradi.main.EAM;
import org.miradi.objecthelpers.ThreatStressRatingEnsurer;
import org.miradi.project.Project;
import org.miradi.utils.ProgressInterface;
import org.miradi.xml.AbstractXmlImporter;
import org.miradi.xml.AbstractXmpzObjectImporter;
import org.miradi.xml.wcs.XmpzXmlConstants;
import org.w3c.dom.Node;

public class XmpzXmlImporter extends AbstractXmlImporter implements XmpzXmlConstants
{
	public XmpzXmlImporter(Project projectToFill, ProgressInterface progressIndicatorToUse) throws Exception
	{
		super(projectToFill);
		
		progressIndicator = progressIndicatorToUse;
	}
	
	@Override
	protected void importXml() throws Exception
	{
		Vector<AbstractXmpzObjectImporter> importers = new Vector<AbstractXmpzObjectImporter>();
		
		importers.add(new ProjectSummaryImporter(this));
		importers.add(new ProjectResourceImporter(this));
		importers.add(new OrganizationImporter(this));
		importers.add(new ProjectSummaryScopeImporter(this));
		importers.add(new ProjectSummaryLocationImporter(this));
		importers.add(new ProjectSummaryPlanningImporter(this));
		importers.add(new TncProjectDataImporter(this));
		importers.add(new WwfProjectDataImporter(this));
		importers.add(new WcsProjectDataImporter(this));
		importers.add(new RareProjetDataImporter(this));
		importers.add(new FosProjectDataImporter(this));
		
		importers.add(new ConceptualModelPoolImporter(this));
		importers.add(new ResultsChainDiagramPoolImporter(this));
		
		importers.add(new CausePoolImporter(this));
		importers.add(new BiodiversityTargetPoolImporter(this));
		importers.add(new HumanWelfareTargetPoolImporter(this));
		importers.add(new StrategyPoolImporter(this));
		importers.add(new ThreatReductionResultsPoolImporter(this));
		importers.add(new IntermediateResultPoolImporter(this));
		importers.add(new StressPoolImporter(this));
		importers.add(new ScopeBoxPoolImporter(this));
		importers.add(new TextBoxPoolImporter(this));
		importers.add(new GroupBoxPoolImporter(this));
		importers.add(new TaskPoolImporter(this));		
		
		importers.add(new DiagramFactorPoolImporter(this));
		importers.add(new DiagramLinkPoolImporter(this));
		
		importers.add(new KeyEcologicalAttributePoolImporter(this));
		importers.add(new IndicatorPoolImporter(this));
		importers.add(new GoalPoolImporter(this));
		importers.add(new ObjectivePoolImporter(this));
		importers.add(new MeasurementPoolImporter(this));
		importers.add(new SubTargetPoolImporter(this));
		importers.add(new ProgressReportPoolImporter(this));
		importers.add(new ProgressPercentPoolImporter(this));
		importers.add(new AccountingCodePoolImporter(this));
		importers.add(new FundingSourcePoolImporter(this));
		importers.add(new BudgetCategoryOnePoolImporter(this));
		importers.add(new BudgetCategoryTwoPoolImporter(this));
		importers.add(new IucnRedListspeciesPoolImporter(this));
		importers.add(new OtherNotableSpeciesPoolImporter(this));
		importers.add(new AudiencePoolImporter(this));
		importers.add(new ObjectTreeTableConfigurationPoolImporter(this));
		importers.add(new ExpenseAssignmentPoolImporter(this));
		importers.add(new ResourceAssignmentPoolImporter(this));
		importers.add(new DashboardPoolImporter(this));
		importers.add(new TaggedObjectSetPoolImporter(this));
		
		final int DELETE_ORPHANS_PROGRESS_TASK = 1;
		final int IMPORT_THREAT_STRESS_RATING_PROGERESS_TASK = 2;
		final int TOTAL_CUSTOM_TASK_COUNT = DELETE_ORPHANS_PROGRESS_TASK + IMPORT_THREAT_STRESS_RATING_PROGERESS_TASK;
		progressIndicator.setStatusMessage(EAM.text("Importing..."), importers.size() + TOTAL_CUSTOM_TASK_COUNT);
		for (AbstractXmpzObjectImporter importer : importers)
		{
			importer.importElement();
			incrementProgress();
		}
		
		importThreatStressRatings();
		incrementProgress();
		
		importDeletedOrphanText();
		incrementProgress();
	}

	private void incrementProgress()
	{
		progressIndicator.incrementProgress();
	}

	private void importDeletedOrphanText() throws Exception
	{
		Node node = getNode(getRootNode(), XmpzXmlConstants.DELETED_ORPHANS_ELEMENT_NAME);
		getProject().appendToQuarantineFile(getSafeNodeContent(node));
	}

	private void importThreatStressRatings() throws Exception
	{
		beginUsingCommandsToSetData();
		try
		{
			ThreatStressRatingEnsurer ensurer = new ThreatStressRatingEnsurer(getProject());
			ensurer.createOrDeleteThreatStressRatingsAsNeeded();
		}
		finally
		{
			endUsingCommandsToSetData();
		}
		
		new ThreatTargetThreatRatingElementImporter(this).importElement();
	}

	private void endUsingCommandsToSetData()
	{
		getProject().endCommandSideEffectMode();
	}

	private void beginUsingCommandsToSetData()
	{
		getProject().beginCommandSideEffectMode();
	}

	@Override
	protected String getNameSpaceVersion()
	{
		return NAME_SPACE_VERSION;
	}

	@Override
	protected String getPartialNameSpace()
	{
		return PARTIAL_NAME_SPACE;
	}

	@Override
	protected String getRootNodeName()
	{
		return CONSERVATION_PROJECT;
	}
	
	@Override
	protected String getPrefix()
	{
		return PREFIX;
	}
	
	@Override
	protected NamespaceContext getNamespaceContext()
	{
		return new XmpzNameSpaceContext();
	}
	
	protected ProgressInterface progressIndicator;
}
