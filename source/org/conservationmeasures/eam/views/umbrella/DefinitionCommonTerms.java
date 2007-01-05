/*
 * Copyright 2006, The Benetech Initiative
 * 
 * This file is confidential and proprietary
 */
package org.conservationmeasures.eam.views.umbrella;

import java.util.Hashtable;

public class DefinitionCommonTerms
{
    static
    {
    	Hashtable defs = new Hashtable();
		Definition def = new Definition("Goal", 
			"Goal -- A formal statement detailing a desired impact of a project. " +
			"In conservation projects, it is the desired future status of a target." );
		defs.put("Definition:Goal", def);
		

		def = new Definition("Conservation Targets", 
			"Targets - A limited suite of species, communities, and ecological systems " +
			"that are chosen to represent and encompass the full array of " +
			"biodiversity found in a project area.  They are the basis for " +
			"setting goals, carrying out conservation actions, and measuring " +
			"conservation effectiveness.  In theory - and hopefully in practice " +
			"- conservation of the focal targets will ensure the conservation of " +
			"all native biodiversity within functional landscapes. ");
		defs.put("Definition:ConservationTargets", def);
		

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
			"Direct threat -- Proximate agents or factors that directly " +
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
				"Activity - An action carried out to accomplish one or more of a project’s strategies.");
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
				"Resource - A person or item that is needed to complete an activity/method or a task.  " +
				"There are three main kinds of resources.");
		defs.put("Definition:Resource", def);
		
		
		def = new Definition("Overall Threat Rating", "The overall threat rating is...");
		defs.put("Definition:OverallThreatRating", def);
		

		def = new Definition("Irreversibility", "Irreversibility is...");
		defs.put("Definition:Irreversibility", def);
		

		def = new Definition("Severity", "Severity is...");
		defs.put("Definition:Severity", def);
		

		def = new Definition("Project", "A project is...");
		defs.put("Definition:Project", def);
		
		
		def = new Definition("CMP", "The Conservation Measures Partnership (CMP) is...");
		defs.put("Definition:CMP", def);
		

		def = new Definition("OpenStandards", "The Open Standards are...");
		defs.put("Definition:OpenStandards", def);

		
		def = new Definition("NewProject", "A New Project is...");
		defs.put("Definition:NewProject", def);
		

		def = new Definition("CreateProject", "A New Project is...");
		defs.put("Definition:CreateProject", def);
	

		def = new Definition("ImportZip","A Zipped Project is...");
		defs.put("Definition:ImportZip", def);

		
		def = new Definition("ImportCAP","A CAP Workbook is...");
		defs.put("Definition:ImportCAP", def);

		definitions = defs;
    }
	
    static public Definition getDefintion(String key)
    {
    	Definition def = (Definition)definitions.get(key);
    	if (def==null)
    		return new Definition("Undefined", "Undefined");
    	return def;
    }
    
	static Hashtable definitions;
}


