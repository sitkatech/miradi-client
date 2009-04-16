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
package org.miradi.xml.conpro;

public interface ConProMiradiXmlVersion2
{
	public static final String CONSERVATION_PROJECT = "conservation_project";
	public static final String XMLNS = "xmlns";
	public static final String NAME_SPACE_VERSION = "2";
	public static final String PARTIAL_NAME_SPACE = "http://services.tnc.org/schema/conservation-project/";
	//FIXME this is a version under progress, inluding -working in nameSpace
	public static final String NAME_SPACE = PARTIAL_NAME_SPACE + NAME_SPACE_VERSION + "-working";
	public static final String PROJECT_SUMMARY = "project_summary";
	public static final String PROJECT_ID = "project_id";
	public static final String CONTEXT_ATTRIBUTE = "context";
	public static final String CONPRO_CONTEXT = "ConPro";
	public static final String SHARE_OUTSIDE_ORGANIZATION = "share_outside_organization";
	public static final String PROJECT_SUMMARY_NAME = "name";
	public static final String START_DATE = "start_date";
	public static final String AREA_SIZE = "area_size";
	public static final String AREA_SIZE_UNIT = "unit";
	public static final String GEOSPATIAL_LOCATION = "geospatial_location";
	public static final String GEOSPATIAL_LOCATION_TYPE = "type";
	public static final String LATITUDE = "latitude";
	public static final String LONGITUDE = "longitude";
	public static final String DESCRIPTION_COMMENT = "description_comment";
	public static final String GOAL_COMMENT = "goal_comment";
	public static final String PLANNING_TEAM_COMMENT = "planning_team_comment";
	public static final String LESSONS_LEARNED = "lessons_learned";
	public static final String STRESSLESS_THREAT_RANK = "stressless_threat_rank";
	public static final String PROJECT_THREAT_RANK = "project_threat_rank";
	public static final String PROJECT_VIABILITY_RANK = "project_viability_rank";
	public static final String TEAM = "team";
	public static final String ROLE = "role";
	public static final String TEAM_LEADER_VALUE = "Team Leader";
	public static final String TEAM_MEMBER_VALUE = "Team Member";
	public static final String PERSON = "person";
	public static final String GIVEN_NAME = "givenname";
	public static final String SUR_NAME = "surname";
	public static final String EMAIL = "email";
	public static final String PHONE = "phone";
	public static final String ORGANIZATION = "organization";
	public static final String ECOREGIONS = "ecoregions";
	public static final String ECOREGION_CODE = "ecoregion_code";
	public static final String COUNTRIES = "countries";
	public static final String COUNTRY_CODE = "country_code";
	public static final String OUS = "ous";
	public static final String OU_CODE = "ou_code";
	public static final String EXPORTER_NAME = "exporter_name";
	public static final String MIRADI = "Miradi";
	public static final String EXPORTER_VERSION = "exporter_version";
	public static final String EXPORT_DATE = "export_date";
	public static final String TARGETS = "targets";
	public static final String TARGET = "target";
	public static final String ID = "id";
	public static final String SEQUENCE = "sequence";
	public static final String TARGET_NAME = "name";
	public static final String TARGET_DESCRIPTION = "description";
	public static final String TARGET_DESCRIPTION_COMMENT = "description_comment";
	public static final String TARGET_VIABILITY_COMMENT = "target_viability_comment";
	public static final String TARGET_VIABILITY_RANK = "target_viability_rank";
	public static final String TARGET_VIABILITY_MODE = "target_viability_mode";
	public static final String HABITAT_TAXONOMY_CODES = "habitat_taxonomy_codes";
	public static final String HABITAT_TAXONOMY_CODE = "habitat_taxonomy_code";
	public static final String STRESSES = "stresses";
	public static final String STRESS = "stress";
	public static final String NAME = "name";
	public static final String STRESS_SEVERITY = "stress_severity"; 
	public static final String STRESS_SCOPE = "stress_scope";
	public static final String STRESS_OVERRIDE_RANK = "stress_ovrd_rank";
	public static final String STRESS_TO_TARGET_RANK = "stress_to_target_rank";
	public static final String THREAT_STRESS_RATINGS = "threat_stress_ratings";
	public static final String THREAT_STRESS_RATING = "threat_stress_rating";
	public static final String CONTRIBUTING_RANK = "contrib_rank";
	public static final String IRREVERSIBILITY_RANK = "irreversible_rank";
	public static final String STRESS_THREAT_TO_TARGET_RANK = "stress_threat_to_target_rank";
	public static final String NESTED_TARGETS = "nested_targets";
	public static final String NESTED_TARGET = "nested_target";
	public static final String COMMENT = "comment";
	public static final String THREAT_TARGET_ASSOCIATIONS = "threat_target_associations";
	public static final String THREAT_TARGET_ASSOCIATION  = "threat_target_association";
	public static final String THREAT_ID = "threat_id";
	public static final String THREAT_TO_TARGET_RANK = "threat_to_target_rank";
	public static final String THREAT_SEVERITY = "threat_severity";
	public static final String THREAT_SCOPE = "threat_scope";
	public static final String THREAT_IRREVERSIBILITY = "threat_irreversibility";
	public static final String THREAT_TARGET_COMMENT = "threat_target_comment";
	public static final String STRATEGY_THREAT_TARGET_ASSOCIATIONS = "strategy_threat_target_associations";
	public static final String STRATEGY_THREAT_TARGET_ASSOCIATION = "strategy_threat_target_association";
	public static final String STRATEGY_ID = "strategy_id";
	public static final String KEY_ATTRIBUTES = "key_attributes";
	public static final String KEY_ATTRIBUTE = "key_attribute";
	public static final String CATEGORY = "category";
	public static final String VIABILITY_ASSESSMENT = "viability_assessment";
	public static final String VIABILITY_ASSESSMENTS = "viability_assessments";
	public static final String TARGET_ID = "target_id";
	public static final String INDICATOR_ID = "indicator_id";
	public static final String KEA_ID = "kea_id";
	public static final String INDICATOR_DESCRIPTION_POOR = "indicator_description_poor";
	public static final String INDICATOR_DESCRIPTION_FAIR = "indicator_description_fair";
	public static final String INDICATOR_DESCRIPTION_GOOD = "indicator_description_good";
	public static final String INDICATOR_DESCRIPTION_VERY_GOOD = "indicator_description_very_good";
	public static final String DESIRED_VIABILITY_RATING = "desired_viability_rating";
	public static final String SOURCE_INDICATOR_RATINGS = "source_indicator_ratings";
	public static final String DESIRED_RATING_DATE = "desired_rating_date";
	public static final String KEA_AND_INDICATOR_COMMENT = "kea_and_indicator_comment";
	public static final String INDICATOR_RATING_COMMENT = "indicator_rating_comment";
	public static final String DESIRED_RATING_COMMENT = "desired_rating_comment";
	public static final String THREATS = "threats";
	public static final String THREAT = "threat";
	public static final String THREAT_TAXONOMY_CODE = "threat_taxonomy_code";
	public static final String THREAT_TO_PROJECT_RANK = "threat_to_project_rank";
	public static final String OBJECTIVES = "objectives";
	public static final String OBJECTIVE = "objective";
	public static final String INDICATORS = "indicators";
	public static final String INDICATOR = "indicator";
	public static final String STRATEGIES = "strategies";
	public static final String STRATEGY = "strategy";
	public static final String LEGACY_TNC_STRATEGY_RATING = "legacy_strategy_ranking";
	public static final String OBJECTIVE_ID = "objective_id";
	public static final String TAXONOMY_CODE = "taxonomy_code";
	public static final String SELECTED = "selected";
	public static final String ACTIVITIES = "activities";
	public static final String ACTIVITY = "activity";
	public static final String ACTIVITY_START_DATE = "start_date";
	public static final String ACTIVITY_END_DATE = "end_date";
	public static final String PRIORITY = "priority";
	public static final String STATUS = "status";
	public static final String ANNUAL_COST = "annual_cost";
	public static final String SEE_DETAILS_FIELD_METHOD_NAME = "See Details field";
	public static final String ORGANIZATIONAL_PRIORITIES = "organizational_priorities";
	public static final String ORGANIZATIONAL_PRIORITY = "priority";
	public static final String PROJECT_TYPES = "project_types";
	public static final String PROJECT_TYPE = "project_type";
	public static final String PROGRESS_PERCENT_REPORTS = "percent_complete_measures";
	public static final String PROGRESS_PERCENT_REPORT = "percent_complete_measure";
	public static final String PROGRESS_PERCENT_COMPLETE = "measure";
	public static final String PROGRESS_PERCENT_DATE = "date";
	public static final String PROGRESS_PERCENT_COMMENT = "comment";
	public static final String PROGRESS_REPORTS = "statuses";
	public static final String PROGRESS_REPORT = "status";
	public static final String PROGRESS_REPORT_DATE = "date";
	public static final String PROGRESS_REPORT_STATUS = "measure";
	public static final String PROGRESS_REPORT_COMMENT = "comment";
	public static final String METHOD_ID = "method_id";
	public static final String METHODS = "methods";
	public static final String METHOD = "method";
	public static final String METHOD_NAME = "name";
	public static final String METHOD_DETAIL = "detail";
	public static final String METHOD_ANNUAL_COST = "annual_cost";
	public static final String METHOD_COMMENT = "comment";
	public static final String MEASUREMENTS = "measures";
	public static final String MEASUREMENT = "measure";
	public static final String MEASUREMENT_SUMMARY = "measurement";
	public static final String MEASUREMENT_DATE = "date";
	public static final String MEASUREMENT_STATUS_CONFIDENCE = "source";
	public static final String MEASUREMENT_TREND = "trend";
	public static final String MEASUREMENT_RATING = "rating";
}
