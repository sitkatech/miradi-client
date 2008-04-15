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
package org.miradi.questions;

public class HabitatAssociationQuestion extends StaticChoiceQuestion
{
	public HabitatAssociationQuestion()
	{
		super(getHabitatAssociationChoices());
	}

	static ChoiceItem[] getHabitatAssociationChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(FOREST_CODE, "Forest"),
				new ChoiceItem("1.1", "Forest - Boreal"), 
				new ChoiceItem("1.2", "Forest - Subarctic"), 
				new ChoiceItem("1.3", "Forest - Subantarctic"), 
				new ChoiceItem("1.4", "Forest - Temperate"), 
				new ChoiceItem("1.5", "Forest - Subtropical/Tropical Dry"), 
				new ChoiceItem("1.6", "Forest - Subtropical/Tropical Moist Lowland"), 
				new ChoiceItem("1.7", "Forest - Subtropical/Tropical Mangrove Vegetation Above High Tide Level"), 
				new ChoiceItem("1.8", "Forest - Subtropical/Tropical Swamp"), 
				new ChoiceItem("1.9", "Forest - Subtropical/Tropical Moist Montane"), 
				new ChoiceItem(SAVANNA_CODE, "Savanna"),
				new ChoiceItem("2.1", "Savanna - Dry Savanna"), 
				new ChoiceItem("2.2", "Savanna - Moist Savana"), 
				new ChoiceItem("3", "Shrubland"),
				new ChoiceItem("3.1", "Shrubland - Subarctic"), 
				new ChoiceItem("3.2", "Shrubland - Subantarctic"), 
				new ChoiceItem("3.3", "Shrubland - Boreal"), 
				new ChoiceItem("3.4", "Shrubland - Temperate"), 
				new ChoiceItem("3.5", "Shrubland - Subtropical/Tropical Dry"), 
				new ChoiceItem("3.6", "Shrubland - Subtropical/Tropical Moist"), 
				new ChoiceItem("3.7", "Shrubland - Subtropical/Tropical High Altitude"), 
				new ChoiceItem("3.8", "Shrubland - Mediterranean-type Shrubby Vegetation"), 
				new ChoiceItem("4", "Grassland"),
				new ChoiceItem("4.1", "Grassland - Tundra"), 
				new ChoiceItem("4.2", "Grassland - Subarctic"), 
				new ChoiceItem("4.3", "Grassland - Subantarctic"), 
				new ChoiceItem("4.4", "Grassland - Temperate"), 
				new ChoiceItem("4.5", "Grassland - Subtropical/Tropical Dry Lowland"),
				new ChoiceItem("4.6", "Grassland - Subtropical/Tropical Seasonally Wet/Flooded Lowland"),
				new ChoiceItem("4.7", "Grassland - Subtropical/Tropical High Altitude"),
				new ChoiceItem("5", "Wetlands (inland)"),
				new ChoiceItem("5.1", "Wetlands - Permanent Rivers/Streams/Creeks [includes waterfalls]"),
				new ChoiceItem("5.2", "Wetlands - Seasonal/Intermittent/Irregular Rivers/Streams/Creeks"),
				new ChoiceItem("5.3", "Wetlands - Shrub Dominated Wetlands"),
				new ChoiceItem("5.4", "Wetlands - Bogs, Marshes, Swamps, Fens, Peatlands"),
				new ChoiceItem("5.5", "Wetlands - Permanent Freshwater Lakes [over 8 ha]"),
				new ChoiceItem("5.6", "Wetlands - Seasonal/Intermittent Freshwater Lakes [over 8 ha]"),
				new ChoiceItem("5.7", "Wetlands - Permanent Freshwater Marshes/Pools [under 8 ha]"),
				new ChoiceItem("5.8", "Wetlands - Seasonal/Intermittent Freshwater Marshes/Pools [under 8 ha]"),
				new ChoiceItem("5.9", "Wetlands - Freshwater Springs and Oases"),
				new ChoiceItem("5.10", "Wetlands - Tundra Wetlands [includes pools and temporary waters from snowmelt]"),
				new ChoiceItem("5.11", "Wetlands - Alpine Wetlands [includes temporary waters from snowmelt]"),
				new ChoiceItem("5.12", "Wetlands - Geothermal Wetlands"),
				new ChoiceItem("5.13", "Wetlands - Permanent Inland Deltas"),
				new ChoiceItem("5.14", "Wetlands - Permanent Saline, Brackish or Alkaline Lakes"),
				new ChoiceItem("5.15", "Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Lakes and Flats"),
				new ChoiceItem("5.16", "Wetlands - Permanent Saline, Brackish or Alkaline Marshes/Pools"),
				new ChoiceItem("5.17", "Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Marshes/Pools"),
				new ChoiceItem("5.18", "Wetlands - Karst and Other Subterranean Hydrological Systems [inland]"),
				new ChoiceItem("6", "Rocky Areas [e.g. inland cliffs, mountain peaks]"),
				new ChoiceItem("7", "Caves and Subterranean Habitats (non-aquatic)"),
				new ChoiceItem("7.1", "- Caves"),
				new ChoiceItem("7.2", "- Other Subterranean Habitats"),
				new ChoiceItem("8", "Desert"), 
				new ChoiceItem("8.1", "Desert - Hot"),  
				new ChoiceItem("8.2", "Desert - Temperate"),
				new ChoiceItem("8.3", "Desert - Cold"),
				new ChoiceItem("9", "Marine Neritic (Submergent Nearshore Continental Shelf or Oceanic Island)"),
				new ChoiceItem("9.1", "Marine Neritic - Pelagic"),
				new ChoiceItem("9.2", "Marine Neritic - Subtidal Rock and Rocky Reefs"),
				new ChoiceItem("9.3", "Marine Neritic - Subtidal Loose Rock/Pebble/Gravel"),
				new ChoiceItem("9.5", "Marine Neritic - Subtidal Sandy"),
				new ChoiceItem("9.6", "Marine Neritic - Subtidal Sandy-Mud"),
				new ChoiceItem("9.7", "Marine Neritic - Macroalgal/Kelp"),
				new ChoiceItem("9.8", "Marine Neritic - Coral Reef"),
				new ChoiceItem("9.8.1", "Marine Neritic - Coral Reef - Outer Reef Channel"),
				new ChoiceItem("9.8.2", "Marine Neritic - Coral Reef - Back Slope"),
				new ChoiceItem("9.8.3", "Marine Neritic - Coral Reef - Foreslope (Outer Reef Slope)"),
				new ChoiceItem("9.8.4", "Marine Neritic - Coral Reef - Lagoon"),
				new ChoiceItem("9.8.5", "Marine Neritic - Coral Reef - Inter-Reef Soft Substrate"),
				new ChoiceItem("9.8.6", "Marine Neritic - Coral Reef - Inter-Reef Rubble Substrate"),
				new ChoiceItem("9.9", "Marine Neritic - Seagrass (Submerged)"),
				new ChoiceItem("9.10", "Marine Neritic - Estuaries"),
				new ChoiceItem("10", "Marine Oceanic"), 
				new ChoiceItem("10.1", "Marine Oceanic - Epipelagic (0-200 m)"),
				new ChoiceItem("10.2", "Marine Oceanic - Mesopelagic (200-1,000 m)"),
				new ChoiceItem("10.3", "Marine Oceanic - Bathypelagic (1,000-4,000 m"),
				new ChoiceItem("10.4", "Marine Oceanic - Abyssopelagic (4,000-6,000 m)"),
				new ChoiceItem("11", "Marine Deep Benthic"), 
				new ChoiceItem("11.1", "Marine Deep Benthic - Continental Slope/Bathyl Zone (200-4,000 m)"), 
				new ChoiceItem("11.1.1", "Marine Deep Benthic - Continental Slope/Bathyl Zone - Hard Substrate"), 
				new ChoiceItem("11.1.2", "Marine Deep Benthic - Continental Slope/Bathyl Zone - Soft Substrate"), 
				new ChoiceItem("11.2", "Marine Deep Benthic - Abyssal Plain (4,000-6,000 m)"), 
				new ChoiceItem("11.3", "Marine Deep Benthic - Abyssal Mountain/Hills (4,000-6,000 m)"), 
				new ChoiceItem("11.4", "Marine Deep Benthic - Hadal/Deep Sea Trench (>6,000 m)"), 
				new ChoiceItem("11.5", "Marine Deep Benthic - Seamount"), 
				new ChoiceItem("11.6", "Marine Deep Benthic - Deep Sea Vents (Rifts/Seeps)"), 
				new ChoiceItem("12", "Marine Intertidal"),
				new ChoiceItem("12.1", "Marine Intertidal - Rocky Shoreline"), 
				new ChoiceItem("12.2", "Marine Intertidal - Sandy Shoreline and/or Beaches, Sand Bars, Spits, Etc."), 
				new ChoiceItem("12.3", "Marine Intertidal - Shingle and/or Pebble Shoreline and/or Beaches"), 
				new ChoiceItem("12.4", "Marine Intertidal - Mud Flats and Salt Flats"), 
				new ChoiceItem("12.5", "Marine Intertidal - Salt Marshes (Emergent Grasses)"), 
				new ChoiceItem("12.6", "Marine Intertidal - Tidepools"), 
				new ChoiceItem("12.7", "Marine Intertidal - Mangrove Submerged Roots"), 
				new ChoiceItem("13", "Marine Coastal/Supratidal"),
				new ChoiceItem("13.1", "Marine Coastal/Supratidal - Sea Cliffs and Rocky Offshore Islands"), 
				new ChoiceItem("13.2", "Marine Coastal/Supratidal - Coastal Caves/Karst"), 
				new ChoiceItem("13.3", "Marine Coastal/Supratidal - Coastal Sand Dunes"), 
				new ChoiceItem("13.4", "Marine Coastal/Supratidal - Coastal Brackish/Saline Lagoons/Marine Lakes"),
				new ChoiceItem("13.5", "Marine Coastal/Supratidal - Coastal Freshwater Lakes"), 
				new ChoiceItem("14", "Artificial - Terrestrial"),
				new ChoiceItem("14.1", "Artificial - Arable Land"),
				new ChoiceItem("14.2", "Artificial - Pastureland"), 
				new ChoiceItem("14.3", "Artificial - Plantations"), 
				new ChoiceItem("14.4", "Artificial - Rural Gardens"), 
				new ChoiceItem("14.5", "Artificial - Urban Areas"), 
				new ChoiceItem("14.6", "Artificial - Subtropical/Tropical Heavily Degraded Former Forest"), 
				new ChoiceItem("15", "Artificial - Aquatic"),
				new ChoiceItem("15.1", "Artificial - Water Storage Areas (over 8 ha)"), 
				new ChoiceItem("15.2", "Artificial - Ponds (below 8 ha)"), 
				new ChoiceItem("15.3", "Artificial - Aquaculture Ponds"), 
				new ChoiceItem("15.4", "Artificial - Salt Exploitation Sites"), 
				new ChoiceItem("15.5", "Artificial - Excavations (open)"), 
				new ChoiceItem("15.6", "Artificial - Wastewater Treatment Areas"), 
				new ChoiceItem("15.7", "Artificial - Irrigated Land [includes irrigation channels]"), 
				new ChoiceItem("15.8", "Artificial - Seasonally Flooded Agricultural Land"), 
				new ChoiceItem("15.9", "Artificial - Canals and Drainage Channels, Ditches"), 
				new ChoiceItem("15.10", "Artificial - Karst and Other Subterranean Hydrological Systems [human-made]"), 
				new ChoiceItem("15.11", "Artificial - Marine Anthropogenic Structures"), 
				new ChoiceItem("15.12", "Artificial - Mariculture Cages"), 
				new ChoiceItem("15.13", "Artificial - Mari/Brackish-culture Ponds"), 
				new ChoiceItem("16", "Introduced Vegetation"),
				new ChoiceItem("17", "Other"),
				new ChoiceItem("18", "Unknown"),
		};
	}
	
	public static final String FOREST_CODE = "1";
	public static final String SAVANNA_CODE = "2";
}
