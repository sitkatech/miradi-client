/* 
* Copyright 2005-2008, Foundations of Success, Bethesda, Maryland 
* (on behalf of the Conservation Measures Partnership, "CMP") and 
* Beneficent Technology, Inc. ("Benetech"), Palo Alto, California. 
*/ 
package org.miradi.views.umbrella;

import java.util.Hashtable;

import org.miradi.wizard.threatmatrix.ThreatRatingWizardCheckBundleStep;
import org.miradi.wizard.threatmatrix.ThreatRatingWizardCheckTotalsStep;

public class DefinitionCommonTerms
{
    static
    {
    	Hashtable defs = new Hashtable();
		Definition def = new Definition("Goal", 
			"Goal - A formal statement detailing a desired impact of a project. " +
			"In conservation projects, it is the desired future status of a target." );
		defs.put("Definition:Goal", def);
		
		
		def = new Definition("Chain",
				"Chain - A sequence of linked factors in a diagram. " +
				"A \"Factor Chain\" in a conceptual model shows the state " +
				"of the world before you take action. A \"Results Chain\" " +
				"shows the expected outcomes from the implementation of a strategy. " +
				"Chains thus represent the assumptions you are making " +
				"about your project site." );						
		defs.put("Definition:Chain", def);
		
		
		def = new Definition("Conservation Targets", 
			"Targets - A limited suite of species, communities, and ecological systems " +
			"that are chosen to represent and encompass the full array of " +
			"biodiversity found in a project area.  They are the basis for " +
			"setting goals, carrying out conservation actions, and measuring " +
			"conservation effectiveness.  In theory - and hopefully in practice " +
			"- conservation of the focal targets will ensure the conservation of " +
			"all native biodiversity within functional landscapes. ");
		defs.put("Definition:ConservationTargets", def);
		
		
		def = new Definition("Stress", 
				"Stress - Impaired aspects of conservation targets " +
				"that result directly or indirectly from human activities " +
				"(e.g., low population size, reduced extent of forest system; " +
				"reduced river flows; increased sedimentation; lowered groundwater table level). " +
				"Generally equivalent to degraded key ecological attributes (e.g., habitat loss).");
		defs.put("Definition:Stress", def);
		
		
		def = new Definition("Ecosystems", 
			"Ecosystems - The ecological systems that characterize the " +
			"terrestrial, aquatic, and marine biodiversity of the project site. ");
		defs.put("Definition:Ecosystems", def);
		
		
		def = new Definition("Focal Species", 
			"Focal Species - These include species endemic to the ecoregion, " +
			"area-sensitive (umbrella) species, commercially exploited species, " +
			"flagship species, keystone species, or imperiled species. Species " +
			"selected as focal targets are typically those that are not represented " +
			"by the key ecosystems because they require multiple habitats or have " +
			"special conservation requirements. ");
		defs.put("Definition:FocalSpecies", def);
		
		
		def = new Definition("Ecological Processes",
			"Ecological Processes - Ecological processes that create and maintain " +
			"biodiversity. These could include pollination, seed dispersal, " +
			"dispersal of large mammals between protected areas, movements of " +
			"migratory fish, nursery and recruitment areas for coastal fisheries; " +
			"or altitudinal migrations by birds.");
		defs.put("Definition:EcologicalProcesses", def);
		

		def = new Definition("Direct Threat", 
			"Direct threat - Proximate agents or factors that directly " +
			"degrade conservation targets.");
		defs.put("Definition:DirectThreat", def);
			

		def = new Definition("Classifications of Direct Threats", 
			"Direct threats can be classified according to the new " +
			"'IUCN-CMP Unified Classifications of Direct Threats', " +
			"available on the web at www.conservationmeasures.org.");
		defs.put("Definition:IUCNThreats", def);
		
		
		def = new Definition("Contributing Factor", 
			"Contributing factors (Indirect threats and Opportunities) " +
			"- Human-induced actions and event that underlie or lead " +
			"to the direct threats");
		defs.put("Definition:IndirectThreat", def);
		

		def = new Definition("Conceptual Model", 
			"Conceptual model - A diagram of a set of relationships " +
			"between certain factors that are believed to impact or " +
			"lead to a conservation target");
		defs.put("Definition:ConceptualModel", def);
		
		
		def = new Definition("Scope", 
			"Scope - The broad geographic or thematic focus of a project");
		defs.put("Definition:Scope", def);
		
		
		def = new Definition("Scope (Threat Ratings)", 
			"Scope (Threat Ratings) - A threat rating criterion that is most commonly defined " +
			"spatially as the geographic scope of impact on the conservation target at the site " +
			"that can reasonably be expected within ten years under current circumstances " +
			"(i.e., given the continuation of the existing situation). ");
		defs.put("Definition:ThreatRatingScope", def);
		
		def = new Definition("Vision", 
			"Vision - A general summary of the desired state or " +
			"ultimate condition of the project area that a project is working to achieve");
		defs.put("Definition:Vision", def);
		

		def = new Definition("Indicator", 
			"Indicator - A measurable entity related to a specific information need " +
			"(for example, the status of a target, change in a threat, " +
			"or progress towards an objective).  " +
			"A good indicator meets the criteria of being: measurable, " +
			"precise, consistent, and sensitive.");
		defs.put("Definition:Indicator", def);
		
		def = new Definition("KEA", 
			"KEA (Key Ecological Attribute) - An aspect of a target's biology or ecology " +
			"that if present, defines a healthy target and if missing or altered, " +
			"would lead to the outright loss or extreme degradation of that target over time.");
		defs.put("Definition:KEA", def);

		def = new Definition("Method",
			"Method - A specific technique used to collect data to measure an indicator.  " +
			"A good method is accurate, reliable, cost-effective, feasible, and appropriate." );
		defs.put("Definition:Method", def);
		

		def = new Definition("Monitoring Plan", 
			"Monitoring Plan - A plan for collecting information that you and others " +
			"need to know about your project. A good play includes " +
			"the indicators that you will track over time as well as " +
			"the methods that you will use.");
		defs.put("Definition:MonitoringPlan", def);
		
		
		def = new Definition("Strategy Effectiveness", 
			"Strategy Effectiveness - Information used to answer the question: Are the conservation actions " +
			"we are taking achieving their desired results?");
		defs.put("Definition:StrategyEffectiveness", def);
		

		def = new Definition("Status Assessments", 
			"Status Assessments - Information used to answer the questions how are key targets, threats, " +
			"and other factors changing? Answers to these questions, even when no " +
			"actions are occurring, are important to determine if future actions are needed.");
		defs.put("Definition:StatusAssessments", def);
		
		
		def = new Definition("Objective", 
			"Objective - A formal statement detailing a desired " +
			"outcome of a project, such as reducing a critical threat." );
		defs.put("Definition:Objective", def);
		
		
		def = new Definition("StrategicPlan", 
			"Strategic plan - An outline of how the project team proposes to" +
			" change the world that contains a project's goals, objectives," +
			" and strategies." );
		defs.put("Definition:StrategicPlan", def);

		
		def = new Definition("Strategy", 
			"Strategy - A broad course of action designed to restore natural systems, " +
			"reduce threats, and/or develop capacity.  A strategy is typically " +
			"used as an umbrella term to describe a set of specific " +
			"conservation actions." );
		defs.put("Definition:Strategy", def);
		
		
		def = new Definition("Initial Project Team",
			"Initial Project Team - The people who conceive of and initiate the project.");
		defs.put("Definition:InitialProjectTeam", def);
		
		
		def = new Definition("Work Plan", 
				"Work Plan - A description of the specific activities/methods and tasks that " +
				"you need to undertake your strategic and monitoring plans.  A good work plan " +
				"shows the timing and sequence of each task as well as the specific resources " +
				"required to carry it out.");
		defs.put("Definition:WorkPlan", def);
		

		def = new Definition("Activity",
				"Activity - An action carried out to accomplish one or more of a project's strategies.");
		defs.put("Definition:Activity", def);
		
		
		def = new Definition("Task",
			"Task - A specific action required in service of an activity or method.  Tasks themselves can " +
			"be broken into sub-tasks if necessary.");
		defs.put("Definition:Task", def);
		
		
		def = new Definition("Quarter",
				"Quarter - A planning period encompassing 3 months. If you are using a Western calendar, " +
				"typically 1st quarter is Jan - Mar, 2nd quarter is Apr - Jun, 3rd quarter is Jul - Sep, " +
				"and 4th qtr is Oct - Dec. ");
		defs.put("Definition:Quarter", def);
		
		
		def = new Definition("Resource",
				"Resource - A person or item that is needed to complete an activity/method or a task. " +
				"There are three main kinds of resources - human, material, and financial.");
		defs.put("Definition:Resource", def);
		
		
		def = new Definition("Overall Threat Rating", 
				"Overall Threat Rating - The rating of the effect of a direct threat on a specific target. " +
				"The overall threat rating is calculated using a rule-based system to combine the scope, " +
				"severity, and irreversibility criteria. ");
		defs.put("Definition:OverallThreatRating", def);
		
		def = new Definition("Irreversibility", "<html>Irreversibility - The degree to which the effects of a threat " +
				"can be undone and the biodiversity targets affected by the threat restored, if the threat is stopped.<ul>" + 
				"<li>4 = Very High: The effects of the threat cannot be undone, and it is very unlikely the target can be restored " +
				"(e.g., wetlands converted to a shopping centre). \n" +
				"<li>3 = High: The effects of the threat can technically be undone and the target restored, " +
				"but it is not practically affordable (e.g., wetland converted to agriculture).\n" + 
				"<li>2 = Medium: The effects of the threat can be undone and the target restored with a reasonable commitment of resources " +
				"(e.g., ditching and draining of wetland). \n" +
				"<li>1 = Low: The effects of the threat are easily reversible and the target can be easily restored at a relatively low cost " +
				"(e.g., off-road vehicles trespassing in wetland).\n"); 
		defs.put("Definition:Irreversibility", def);

		def = new Definition("Severity", 
				"Severity - A threat rating criterion that is defined as " +
				"the level of damage to the conservation target that can " +
				"reasonably be expected within ten years under current " +
				"circumstances (i.e., given the continuation of the existing situation). ");
		defs.put("Definition:Severity", def);
		
		def = new Definition("Contribution", 
				"Contribution - A threat rating criterion defined as the expected contribution of the source, " +
				"acting alone, to the full expression of a stress (as determined in the stress assessment) " +
				"under current circumstances (i.e., given the continuation of the existing management/conservation situation).");
		defs.put("Definition:Contribution", def);
		
		def = new Definition("Project", 
				"Project (general) - Any set of actions undertaken by a group of people and/or " +
				"organizations to achieve defined goals and objectives. " +
				"Projects can range in scale from efforts by local people to protect a " +
				"small sacred grove to efforts by a donor to protect an entire ocean. " + 
				"\n\n" +
				"Project (in Miradi) - A project is the basic unit for a software file in Miradi. " +
				"As a general rule, you should have one file for each real-world project you are managing, " +
				"although for certain large and complex projects, " +
				"you may wish to have one file for each target or for other subdivisions. " +
				"Miradi's default setting is to create a folder " +
				"with the name of the project in the Miradi directory " +
				"(in Windows, the default path is C:/Documents and Setting/User Name/Miradi).");
		defs.put("Definition:Project", def);
		
		
		def = new Definition("CMP", 
				"The Conservation Measures Partnership (CMP) - A consortium of some of " +
				"the world's leading conservation organizations, CMP members work together " +
				"to develop common standards and tools such as Miradi. More information " +
				"is available at www.ConservationMeasures.org ");
		defs.put("Definition:CMP", def);
		

		def = new Definition("OpenStandards", 
				"Open Standards for the Practice of Conservation - A series of best practices " +
				"for designing, managing, monitoring, and learning from conservation projects. " +
				"The basis for Miradi. You can download a copy of the Open Standards at www.ConservationMeasures.org");
		defs.put("Definition:OpenStandards", def);

		

		def = new Definition("ImportZip",
				"Zipped Project - A project folder that has been compressed using the zip format. " +
				"To share files with other Miradi users, you can use the file/export/project " +
				"zip file menu command to create a zipped folder that can then be imported into " +
				"Miradi. Note that Miradi will automatically zip and unzip the folders for you if " +
				"you use the export and import commands. Miradi will also create zip files for project " +
				"backups that are created during format migrations. ");
		defs.put("Definition:ImportZip", def);

		
		def = new Definition("Budget", "Budget - A forward looking projection of the " +
				"expenses and anticipated funding sources for the activities/methods and " +
				"tasks you need to implement your strategic and monitoring plans.");
		defs.put("Definition:Budget", def);

		
		def = new Definition("ActualExpenditures", "Actual Expenditures - A backwards looking " +
				"report on the money you spent for the activities/methods and tasks you " +
				"undertook to implement your strategic and monitoring plans.");
		defs.put("Definition:ActualExpenditures", def);
		
		
		def = new Definition("AccountingCodes", "Accounting Codes - A set of categories " +
				"developed by an organization to categorize and track expenses.  " +
				"They typically include hierarchical strings of numbers and/or " +
				"letters (the code) along with a text description.");
		defs.put("Definition:AccountingCodes", def);
		
		def = new Definition("TabDelimitedFile", "Tab Delimited File - A software file " +
				"with regular entries spaced by tab commands.  Most word processors or " +
				"spreadsheet programs will allow you to create a table and then export " +
				"as a tab delimited file.");
		defs.put("Definition:TabDelimitedFile", def);
		
		
		def = new Definition("Funding Sources", "Funding Sources - The sources of money available to your project.");
		defs.put("Definition:FundingSources", def);
		
		def = new Definition("Threat Calculations", ThreatRatingWizardCheckBundleStep.class, "ThreatRatingExplanationOfCalculation.html");
		defs.put("Definition:ShowRulesCalculations", def);
		
		def = new Definition("Bundle Rules", ThreatRatingWizardCheckTotalsStep.class, "ThreatRatingBundleRules.html");
		defs.put("Definition:ShowRulesBundles", def);
		
		
		definitions = defs;
    }
	
    static public Definition getDefintion(String key)
    {
    	Definition def = (Definition)definitions.get(key);
    	if (def==null)
    		return new Definition(key, "Undefined");
    	return def;
    }
    
	static Hashtable definitions;
}


