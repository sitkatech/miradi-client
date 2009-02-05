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
package org.miradi.views.umbrella;

import java.util.Hashtable;

import org.miradi.main.EAM;

public class DefinitionCommonTerms
{
    static
    {
    	Hashtable defs = new Hashtable();
		Definition def = Definition.createDefinitionFromTextString(EAM.text("Goal"), EAM.text("Goal - A formal statement detailing a desired impact of a project. " +
		"In conservation projects, it is the desired future status of a target. In Miradi, a goal is represented by a small blue oval."));
		defs.put("Definition:Goal", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Chain"), EAM.text("Chain - A sequence of linked factors in a diagram. " +
		"A \"Factor Chain\" in a conceptual model shows the state " +
		"of the world before you take action. A \"Results Chain\" " +
		"shows the expected outcomes from the implementation of a strategy. " +
		"Chains thus represent the assumptions you are making " +
		"about your project site."));						
		defs.put("Definition:Chain", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Conservation Targets"), EAM.text("Targets - A limited suite of species, communities, and ecological systems " +
		"that are chosen to represent and encompass the full array of " +
		"biodiversity found in a project area.  They are the basis for " +
		"setting goals, carrying out conservation actions, and measuring " +
		"conservation effectiveness.  In theory - and hopefully in practice " +
		"- conservation of the focal targets will ensure the conservation of " +
		"all native biodiversity within functional landscapes. In Miradi, a target is represented by a large green oval."));
		defs.put("Definition:ConservationTargets", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Stress"), EAM.text("Stress - An impaired aspect of a conservation target " +
		"that results directly or indirectly from human activities " +
		"(e.g., low population size, reduced extent of forest system; " +
		"reduced river flows; increased sedimentation; lowered groundwater table level). " +
		"Generally equivalent to a degraded key ecological attribute (e.g., habitat loss)."));
		defs.put("Definition:Stress", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Ecosystems"), EAM.text("Ecosystems - The ecological systems that characterize the " +
		"terrestrial, aquatic, and marine biodiversity of the project site. "));
		defs.put("Definition:Ecosystems", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Focal Species"), EAM.text("Focal Species - These include species endemic to the ecoregion, " +
		"area-sensitive (umbrella) species, commercially exploited species, " +
		"flagship species, keystone species, or imperiled species. Species " +
		"selected as focal targets are typically those that are not represented " +
		"by the key ecosystems because they require multiple habitats or have " +
		"special conservation requirements. "));
		defs.put("Definition:FocalSpecies", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Ecological Processes"), EAM.text("Ecological Processes - Ecological processes that create and maintain " +
		"biodiversity. These could include pollination, seed dispersal, " +
		"dispersal of large mammals between protected areas, movements of " +
		"migratory fish, nursery and recruitment areas for coastal fisheries; " +
		"or altitudinal migrations by birds."));
		defs.put("Definition:EcologicalProcesses", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Direct Threat"), EAM.text("Direct threat - A proximate agent or factor that directly " +
		"degrades one or more conservation targets. In Miradi, a dirct threat is represented by a pink rectangle."));
		defs.put("Definition:DirectThreat", def);
			

		def = Definition.createDefinitionFromTextString(EAM.text("Classifications of Direct Threats"), EAM.text("Direct threats can be classified according to the new " +
		"'IUCN-CMP Unified Classifications of Direct Threats', " +
		"available on the web at www.conservationmeasures.org."));
		defs.put("Definition:IUCNThreats", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Contributing Factor"), EAM.text("Contributing factor (Indirect threats and Opportunities) " +
		"- A human-induced action or event that underlies or leads " +
		"to one or more direct threats. In Miradi, a contributing factor is represented by an orange rectangle."));
		defs.put("Definition:IndirectThreat", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Conceptual Model"), EAM.text("Conceptual model - A diagram of a set of relationships " +
		"between certain factors that are believed to impact or " +
		"lead to a conservation target."));
		defs.put("Definition:ConceptualModel", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Scope"), EAM.text("Scope - The broad geographic or thematic focus of a project. In Miradi, a project's scope is represented by a green box around the targets."));
		defs.put("Definition:Scope", def);
		
		def = Definition.createDefinitionFromTextString(EAM.text("Assigned Resource"), EAM.text("Assigned Resource - A project resource that has been assigned to a specific activity, method, or task."));
		defs.put("Definition:AssignedResource", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Scope (Threat Ratings)"), EAM.text("Scope (Threat Ratings) - A threat rating criterion that is most commonly defined " +
		"spatially as the proportion of the target that can reasonably be expected to be " +
		"affected by the threat within ten years given the continuation of current " +
		"circumstances and trends. For ecosystems and ecological communities, measured " +
		"as the proportion of the target's occurence. For species, measured as the " +
		"proportion of the target's population."));
		defs.put("Definition:ThreatRatingScope", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Vision"), EAM.text("Vision - A general summary of the desired state or " +
		"ultimate condition of the project area that a project is working to achieve."));
		defs.put("Definition:Vision", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Indicator"), EAM.text("Indicator - A measurable entity related to a specific information need " +
		"(for example, the status of a target, change in a threat, " +
		"or progress towards an objective).  " +
		"A good indicator meets the criteria of being: measurable, " +
		"precise, consistent, and sensitive. In Miradi, an indicator is represented by a purple triangle."));
		defs.put("Definition:Indicator", def);
		
		def = Definition.createDefinitionFromTextString(EAM.text("KEA"), EAM.text("KEA (Key Ecological Attribute) - An aspect of a target's biology or ecology " +
		"that if present, defines a healthy target and if missing or altered, " +
		"would lead to the outright loss or extreme degradation of that target over time. In Miradi, a KEA is represented by a green key symbol."));
		defs.put("Definition:KEA", def);

		def = Definition.createDefinitionFromTextString(EAM.text("Method"), EAM.text("Method - A specific technique used to collect data to measure an indicator.  " +
		"A good method is accurate, reliable, cost-effective, feasible, and appropriate. In Miradi, a method is represented by a purple flattened oval."));
		defs.put("Definition:Method", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Monitoring Plan"), EAM.text("Monitoring Plan - A plan for collecting information that you and others " +
		"need to know about your project. A good plan includes " +
		"the indicators that you will track over time as well as " +
		"the methods that you will use."));
		defs.put("Definition:MonitoringPlan", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Strategy Effectiveness"), EAM.text("Strategy Effectiveness - Information used to answer the question: Are the conservation actions " +
		"we are taking achieving their desired results?"));
		defs.put("Definition:StrategyEffectiveness", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Status Assessments"), EAM.text("Status Assessments - Information used to answer the questions how are key targets, threats, " +
		"and other factors changing? Answers to these questions, even when no " +
		"actions are occurring, are important to determine if future actions are needed."));
		defs.put("Definition:StatusAssessments", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Objective"), EAM.text("Objective - A formal statement detailing a desired " +
		"outcome of a project, such as reducing a critical threat. In Miradi, an objective is represented by a small blue rectangle."));
		defs.put("Definition:Objective", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("StrategicPlan"), EAM.text("Strategic plan - An outline of how the project team proposes to" +
		" change the world that contains a project's goals, objectives," +
		" and strategies."));
		defs.put("Definition:StrategicPlan", def);

		
		def = Definition.createDefinitionFromTextString(EAM.text("Strategy"), EAM.text("Strategy - A broad course of action designed to restore natural systems, " +
		"reduce threats, and/or develop capacity.  A strategy is typically " +
		"used as an umbrella term to describe a set of specific " +
		"conservation actions. In Miradi, a strategy is represented by a yellow hexagon."));
		defs.put("Definition:Strategy", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Initial Project Team"), EAM.text("Initial Project Team - The people who conceive of and initiate the project."));
		defs.put("Definition:InitialProjectTeam", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Work Plan"), EAM.text("Work Plan - A description of the specific activities/methods and tasks that " +
		"you need to undertake your strategic and monitoring plans.  A good work plan " +
		"shows the timing and sequence of each task as well as the specific resources " +
		"required to carry it out."));
		defs.put("Definition:WorkPlan", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("Activity"), EAM.text("Activity - An action carried out to accomplish one or more of a project's strategies. In Miradi, an activity is represented by a yellow flattened oval."));
		defs.put("Definition:Activity", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Task"), EAM.text("Task - A specific action required in service of an activity or method.  Tasks themselves can " +
		"be broken into sub-tasks if necessary. In Miradi, a task is represented by a grey flattened oval."));
		defs.put("Definition:Task", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Quarter"), EAM.text("Quarter - A planning period encompassing 3 months. If you are using a Western calendar, " +
		"typically 1st quarter is Jan - Mar, 2nd quarter is Apr - Jun, 3rd quarter is Jul - Sep, " +
		"and 4th qtr is Oct - Dec. "));
		defs.put("Definition:Quarter", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Resource"), EAM.text("Resource - A person or item that is needed to complete an activity/method or a task. " +
		"There are three main kinds of resources - human, material, and financial."));
		defs.put("Definition:Resource", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Overall Threat Rating"), EAM.text("Target-Threat Rating - The rating of the effect of a direct threat on a specific target. " +
		"The target-threat rating is calculated using a rule-based system to combine the scope, " +
		"severity, and irreversibility criteria. "));
		defs.put("Definition:OverallThreatRating", def);
		
		def = Definition.createDefinitionFromTextString(EAM.text("Irreversibility"), EAM.text("<html>Irreversibility - The degree to which the effects of a " +
				"threat can be revcersed and the target affected by the threat restored. "));
		defs.put("Definition:Irreversibility", def);

		def = Definition.createDefinitionFromTextString(EAM.text("Severity"), EAM.text("Severity - Within the scope, the level of damage to the target from the threat " +
		"that can reasonably be expected given the continuation of " +
		"current circumstances and trends. For ecosystems and ecological communities, " +
		"typically measured as the degree of destruction or degradation of " +
		"the target within the scope. For species, usually measured " +
		"as the degree of reduction of the target population within the scope."));
		defs.put("Definition:Severity", def);
		
		def = Definition.createDefinitionFromTextString(EAM.text("Contribution"), EAM.text("Contribution - A threat rating criterion defined as the expected contribution of the source, " +
		"acting alone, to the full expression of a stress (as determined in the stress assessment) " +
		"under current circumstances (i.e., given the continuation of the existing management/conservation situation)."));
		defs.put("Definition:Contribution", def);
		
		def = Definition.createDefinitionFromTextString(EAM.text("Project"), EAM.text("<html>Project (general) - Any set of actions undertaken by a group of people and/or " +
		"organizations to achieve defined goals and objectives. " +
		"Projects can range in scale from efforts by local people to protect a " +
		"small sacred grove to efforts by a donor to protect an entire ocean. " + 
		"\n\n" +
		"<p>Project (in Miradi) - A project is the basic unit for a software file in Miradi. " +
		"As a general rule, you should have one file for each real-world project you are managing, " +
		"although for certain large and complex projects, " +
		"you may wish to have one file for each target or for other subdivisions. " +
		"\n\n" +
		"<p>To see where your projects are being stored, " +
		"use the <code class='toolbarbutton'>&lt;Edit&gt;/&lt;Preferences&gt;</code> menu, " +
		"and look at the bottom of the <code class='toolbarbutton'>&lt;Data Location&gt;</code> tab."));
		defs.put("Definition:Project", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("CMP"), EAM.text("The Conservation Measures Partnership (CMP) - A consortium of some of " +
		"the world's leading conservation organizations, CMP members work together " +
		"to develop common standards and tools such as Miradi. More information " +
		"is available at www.ConservationMeasures.org "));
		defs.put("Definition:CMP", def);
		

		def = Definition.createDefinitionFromTextString(EAM.text("OpenStandards"), EAM.text("Open Standards for the Practice of Conservation - A series of best practices " +
		"for designing, managing, monitoring, and learning from conservation projects. " +
		"The basis for Miradi. You can download a copy of the Open Standards at www.ConservationMeasures.org"));
		defs.put("Definition:OpenStandards", def);

		

		def = Definition.createDefinitionFromTextString(EAM.text("ImportZip"), EAM.text("Zipped Project - A project folder that has been compressed using the zip format. " +
		"To share files with other Miradi users, you can use the file/export/project " +
		"zip file menu command to create a zipped folder that can then be imported into " +
		"Miradi. Note that Miradi will automatically zip and unzip the folders for you if " +
		"you use the export and import commands. Miradi will also create zip files for project " +
		"backups that are created during format migrations. "));
		defs.put("Definition:ImportZip", def);

		
		def = Definition.createDefinitionFromTextString(EAM.text("Budget"), EAM.text("Budget - A forward looking projection of the " +
				"expenses and anticipated funding sources for the activities/methods and " +
				"tasks you need to implement your strategic and monitoring plans."));
		defs.put("Definition:Budget", def);

		
		def = Definition.createDefinitionFromTextString(EAM.text("ActualExpenditures"), EAM.text("Actual Expenditures - A backwards looking " +
				"report on the money you spent for the activities/methods and tasks you " +
				"undertook to implement your strategic and monitoring plans."));
		defs.put("Definition:ActualExpenditures", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("AccountingCodes"), EAM.text("Accounting Codes - A set of categories " +
				"developed by an organization to categorize and track expenses.  " +
				"They typically include hierarchical strings of numbers and/or " +
				"letters (the code) along with a text description."));
		defs.put("Definition:AccountingCodes", def);
		
		def = Definition.createDefinitionFromTextString(EAM.text("TabDelimitedFile"), EAM.text("Tab Delimited File - A software file " +
				"with regular entries spaced by tab commands.  Most word processors or " +
				"spreadsheet programs will allow you to create a table and then export " +
				"as a tab delimited file."));
		defs.put("Definition:TabDelimitedFile", def);
		
		
		def = Definition.createDefinitionFromTextString(EAM.text("Funding Sources"), EAM.text("Funding Sources - The sources of money available to your project."));
		defs.put("Definition:FundingSources", def);


		def = Definition.createDefinitionFromTextString(EAM.text("ThreatReductionResult"), EAM.text("Threat Reduction Result - A factor in a results chain that describes the desired change in a direct threat that results from implementing one or more conservation strategies. In Miradi, a threat reduction result is represented by a purple rectangle."));
		defs.put("Definition:ThreatReductionResult", def);

		def = Definition.createDefinitionFromTextString(EAM.text("IntermediateResult"), EAM.text("Intermediate Result - A factor in a results chain that describes a specific outcome that results from implementing one or more conservation strategies. In Miradi, an intermediate result is represented by a blue rectangle."));
		defs.put("Definition:IntermediateResult", def);

		def = Definition.createDefinitionFromTextString(EAM.text("NestedTarget"), EAM.text("Nested Target - Species, ecological communities, or ecological system targets whose conservation needs are subsumed in one or more focal conservation targets. Often includes ecoregional targets that a team wants to note and/or track."));
		defs.put("Definition:NestedTargets", def);

		def = Definition.createDefinitionFromTextString(EAM.text("ViabilityRatings"), EAM.text("Viability Ratings - A project's scale of what is very good, good, fair, or poor for a given indicator for a given target. Viability ratings are often quantitatively defined, but they can be qualitative as well.  In effect, by establishing this rating scale, the project team is specifying its assumption as to what constitutes a \"conserved\" target versus one that is in need of management intervention."));
		defs.put("Definition:ViabilityRatings", def);
	
		def = Definition.createDefinitionFromTextString(EAM.text("AcceptableRangeofVariation"), EAM.text("Acceptable Range of Variation - Key ecological attributes of focal targets naturally vary over time. The acceptable range defines the limits of this variation which constitute the minimum conditions for persistence of the target (note that persistence may still require human management interventions).  This concept of an acceptable range of variation establishes the minimum criteria for identifying a conservation target as \"conserved\" or not.  If the attribute lies outside this acceptable range, it is a \"degraded\" attribute."));
		defs.put("Definition:ViabilityRatings", def);

		def = Definition.createDefinitionFromTextString(EAM.text("Measurement"), EAM.text("Measurement - An assessment of an indicator at a given point in time. Measurements can be either quantitative or qualitative."));
		defs.put("Definition:Measurement", def);	

		def = Definition.createDefinitionFromTextString(EAM.text("PriorityRating"), EAM.text("Priority Rating - An assesment of the importance or urgency or collecting measurements for a given indicator."));
		defs.put("Definition:PriorityRating", def);	

		def = Definition.createDefinitionFromTextString(EAM.text("DesiredFutureStatus"), EAM.text("Desired Future Status - A measurement or rating value for an indicator for a key ecological attribute that the project intends to achieve at a specified time in the future. Generally equivalent to a project sub-goal."));
		defs.put("Definition:PriorityRating", def);	


	
		def = Definition.createDefinitionFromHtmlFilename(EAM.text("Threat Calculations"), "ThreatRatingExplanationOfCalculation.html");
		defs.put("Definition:ShowRulesCalculations", def);
		
		def = Definition.createDefinitionFromHtmlFilename(EAM.text("Bundle Rules"), "ThreatRatingBundleRules.html");
		defs.put("Definition:ShowRulesBundles", def);
		
		
		definitions = defs;
    }
	
    static public Definition getDefintion(String key)
    {
    	Definition def = (Definition)definitions.get(key);
    	if (def==null)
    		return Definition.createDefinitionFromTextString(key, EAM.text("Undefined"));
    	return def;
    }
    
	static Hashtable definitions;
}


