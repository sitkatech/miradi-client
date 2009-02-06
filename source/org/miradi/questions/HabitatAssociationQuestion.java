/* 
Copyright 2005-2009, Foundations of Success, Bethesda, Maryland 
(on behalf of the Conservation Measures Partnership, EAM.text(CMP") and 
Beneficent Technology, Inc. ("Benetech")), Palo Alto, California. 

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

import org.miradi.main.EAM;

public class HabitatAssociationQuestion extends StaticChoiceQuestion
{
	public HabitatAssociationQuestion()
	{
		super(getHabitatAssociationChoices());
	}

	static ChoiceItem[] getHabitatAssociationChoices()
	{
		return new ChoiceItem[] {
				new ChoiceItem(FOREST_CODE, EAM.text("Forest")),
				new ChoiceItem("1.1", EAM.text("Forest - Boreal")), 
				new ChoiceItem("1.2", EAM.text("Forest - Subarctic")), 
				new ChoiceItem("1.3", EAM.text("Forest - Subantarctic")), 
				new ChoiceItem("1.4", EAM.text("Forest - Temperate")), 
				new ChoiceItem("1.5", EAM.text("Forest - Subtropical/Tropical Dry")), 
				new ChoiceItem("1.6", EAM.text("Forest - Subtropical/Tropical Moist Lowland")), 
				new ChoiceItem("1.7", EAM.text("Forest - Subtropical/Tropical Mangrove Vegetation Above High Tide Level")), 
				new ChoiceItem("1.8", EAM.text("Forest - Subtropical/Tropical Swamp")), 
				new ChoiceItem("1.9", EAM.text("Forest - Subtropical/Tropical Moist Montane")), 
				new ChoiceItem(SAVANNA_CODE, EAM.text("Savanna")),
				new ChoiceItem("2.1", EAM.text("Savanna - Dry Savanna")), 
				new ChoiceItem("2.2", EAM.text("Savanna - Moist Savana")), 
				new ChoiceItem("3", EAM.text("Shrubland")),
				new ChoiceItem("3.1", EAM.text("Shrubland - Subarctic")), 
				new ChoiceItem("3.2", EAM.text("Shrubland - Subantarctic")), 
				new ChoiceItem("3.3", EAM.text("Shrubland - Boreal")), 
				new ChoiceItem("3.4", EAM.text("Shrubland - Temperate")), 
				new ChoiceItem("3.5", EAM.text("Shrubland - Subtropical/Tropical Dry")), 
				new ChoiceItem("3.6", EAM.text("Shrubland - Subtropical/Tropical Moist")), 
				new ChoiceItem("3.7", EAM.text("Shrubland - Subtropical/Tropical High Altitude")), 
				new ChoiceItem("3.8", EAM.text("Shrubland - Mediterranean-type Shrubby Vegetation")), 
				new ChoiceItem("4", EAM.text("Grassland")),
				new ChoiceItem("4.1", EAM.text("Grassland - Tundra")), 
				new ChoiceItem("4.2", EAM.text("Grassland - Subarctic")), 
				new ChoiceItem("4.3", EAM.text("Grassland - Subantarctic")), 
				new ChoiceItem("4.4", EAM.text("Grassland - Temperate")), 
				new ChoiceItem("4.5", EAM.text("Grassland - Subtropical/Tropical Dry Lowland")),
				new ChoiceItem("4.6", EAM.text("Grassland - Subtropical/Tropical Seasonally Wet/Flooded Lowland")),
				new ChoiceItem("4.7", EAM.text("Grassland - Subtropical/Tropical High Altitude")),
				new ChoiceItem("5", EAM.text("Wetlands (inland)")),
				new ChoiceItem("5.1", EAM.text("Wetlands - Permanent Rivers/Streams/Creeks [includes waterfalls]")),
				new ChoiceItem("5.2", EAM.text("Wetlands - Seasonal/Intermittent/Irregular Rivers/Streams/Creeks")),
				new ChoiceItem("5.3", EAM.text("Wetlands - Shrub Dominated Wetlands")),
				new ChoiceItem("5.4", EAM.text("Wetlands - Bogs, Marshes, Swamps, Fens, Peatlands")),
				new ChoiceItem("5.5", EAM.text("Wetlands - Permanent Freshwater Lakes [over 8 ha]")),
				new ChoiceItem("5.6", EAM.text("Wetlands - Seasonal/Intermittent Freshwater Lakes [over 8 ha]")),
				new ChoiceItem("5.7", EAM.text("Wetlands - Permanent Freshwater Marshes/Pools [under 8 ha]")),
				new ChoiceItem("5.8", EAM.text("Wetlands - Seasonal/Intermittent Freshwater Marshes/Pools [under 8 ha]")),
				new ChoiceItem("5.9", EAM.text("Wetlands - Freshwater Springs and Oases")),
				new ChoiceItem("5.10", EAM.text("Wetlands - Tundra Wetlands [includes pools and temporary waters from snowmelt]")),
				new ChoiceItem("5.11", EAM.text("Wetlands - Alpine Wetlands [includes temporary waters from snowmelt]")),
				new ChoiceItem("5.12", EAM.text("Wetlands - Geothermal Wetlands")),
				new ChoiceItem("5.13", EAM.text("Wetlands - Permanent Inland Deltas")),
				new ChoiceItem("5.14", EAM.text("Wetlands - Permanent Saline, Brackish or Alkaline Lakes")),
				new ChoiceItem("5.15", EAM.text("Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Lakes and Flats")),
				new ChoiceItem("5.16", EAM.text("Wetlands - Permanent Saline, Brackish or Alkaline Marshes/Pools")),
				new ChoiceItem("5.17", EAM.text("Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Marshes/Pools")),
				new ChoiceItem("5.18", EAM.text("Wetlands - Karst and Other Subterranean Hydrological Systems [inland]")),
				new ChoiceItem("6", EAM.text("Rocky Areas [e.g. inland cliffs, mountain peaks]")),
				new ChoiceItem("7", EAM.text("Caves and Subterranean Habitats (non-aquatic)")),
				new ChoiceItem("7.1", EAM.text("- Caves")),
				new ChoiceItem("7.2", EAM.text("- Other Subterranean Habitats")),
				new ChoiceItem("8", EAM.text("Desert")), 
				new ChoiceItem("8.1", EAM.text("Desert - Hot")),  
				new ChoiceItem("8.2", EAM.text("Desert - Temperate")),
				new ChoiceItem("8.3", EAM.text("Desert - Cold")),
				new ChoiceItem("9", EAM.text("Marine Neritic (Submergent Nearshore Continental Shelf or Oceanic Island)")),
				new ChoiceItem("9.1", EAM.text("Marine Neritic - Pelagic")),
				new ChoiceItem("9.2", EAM.text("Marine Neritic - Subtidal Rock and Rocky Reefs")),
				new ChoiceItem("9.3", EAM.text("Marine Neritic - Subtidal Loose Rock/Pebble/Gravel")),
				new ChoiceItem("9.5", EAM.text("Marine Neritic - Subtidal Sandy")),
				new ChoiceItem("9.6", EAM.text("Marine Neritic - Subtidal Sandy-Mud")),
				new ChoiceItem("9.7", EAM.text("Marine Neritic - Macroalgal/Kelp")),
				new ChoiceItem("9.8", EAM.text("Marine Neritic - Coral Reef")),
				new ChoiceItem("9.8.1", EAM.text("Marine Neritic - Coral Reef - Outer Reef Channel")),
				new ChoiceItem("9.8.2", EAM.text("Marine Neritic - Coral Reef - Back Slope")),
				new ChoiceItem("9.8.3", EAM.text("Marine Neritic - Coral Reef - Foreslope (Outer Reef Slope)")),
				new ChoiceItem("9.8.4", EAM.text("Marine Neritic - Coral Reef - Lagoon")),
				new ChoiceItem("9.8.5", EAM.text("Marine Neritic - Coral Reef - Inter-Reef Soft Substrate")),
				new ChoiceItem("9.8.6", EAM.text("Marine Neritic - Coral Reef - Inter-Reef Rubble Substrate")),
				new ChoiceItem("9.9", EAM.text("Marine Neritic - Seagrass (Submerged)")),
				new ChoiceItem("9.10", EAM.text("Marine Neritic - Estuaries")),
				new ChoiceItem("10", EAM.text("Marine Oceanic")), 
				new ChoiceItem("10.1", EAM.text("Marine Oceanic - Epipelagic (0-200 m)")),
				new ChoiceItem("10.2", EAM.text("Marine Oceanic - Mesopelagic (200-1,000 m)")),
				new ChoiceItem("10.3", EAM.text("Marine Oceanic - Bathypelagic (1,000-4,000 m")),
				new ChoiceItem("10.4", EAM.text("Marine Oceanic - Abyssopelagic (4,000-6,000 m)")),
				new ChoiceItem("11", EAM.text("Marine Deep Benthic")), 
				new ChoiceItem("11.1", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone (200-4,000 m)")), 
				new ChoiceItem("11.1.1", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone - Hard Substrate")), 
				new ChoiceItem("11.1.2", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone - Soft Substrate")), 
				new ChoiceItem("11.2", EAM.text("Marine Deep Benthic - Abyssal Plain (4,000-6,000 m)")), 
				new ChoiceItem("11.3", EAM.text("Marine Deep Benthic - Abyssal Mountain/Hills (4,000-6,000 m)")), 
				new ChoiceItem("11.4", EAM.text("Marine Deep Benthic - Hadal/Deep Sea Trench (>6,000 m)")), 
				new ChoiceItem("11.5", EAM.text("Marine Deep Benthic - Seamount")), 
				new ChoiceItem("11.6", EAM.text("Marine Deep Benthic - Deep Sea Vents (Rifts/Seeps)")), 
				new ChoiceItem("12", EAM.text("Marine Intertidal")),
				new ChoiceItem("12.1", EAM.text("Marine Intertidal - Rocky Shoreline")), 
				new ChoiceItem("12.2", EAM.text("Marine Intertidal - Sandy Shoreline and/or Beaches, Sand Bars, Spits, Etc.")), 
				new ChoiceItem("12.3", EAM.text("Marine Intertidal - Shingle and/or Pebble Shoreline and/or Beaches")), 
				new ChoiceItem("12.4", EAM.text("Marine Intertidal - Mud Flats and Salt Flats")), 
				new ChoiceItem("12.5", EAM.text("Marine Intertidal - Salt Marshes (Emergent Grasses)")), 
				new ChoiceItem("12.6", EAM.text("Marine Intertidal - Tidepools")), 
				new ChoiceItem("12.7", EAM.text("Marine Intertidal - Mangrove Submerged Roots")), 
				new ChoiceItem("13", EAM.text("Marine Coastal/Supratidal")),
				new ChoiceItem("13.1", EAM.text("Marine Coastal/Supratidal - Sea Cliffs and Rocky Offshore Islands")), 
				new ChoiceItem("13.2", EAM.text("Marine Coastal/Supratidal - Coastal Caves/Karst")), 
				new ChoiceItem("13.3", EAM.text("Marine Coastal/Supratidal - Coastal Sand Dunes")), 
				new ChoiceItem("13.4", EAM.text("Marine Coastal/Supratidal - Coastal Brackish/Saline Lagoons/Marine Lakes")),
				new ChoiceItem("13.5", EAM.text("Marine Coastal/Supratidal - Coastal Freshwater Lakes")), 
				new ChoiceItem("14", EAM.text("Artificial - Terrestrial")),
				new ChoiceItem("14.1", EAM.text("Artificial - Arable Land")),
				new ChoiceItem("14.2", EAM.text("Artificial - Pastureland")), 
				new ChoiceItem("14.3", EAM.text("Artificial - Plantations")), 
				new ChoiceItem("14.4", EAM.text("Artificial - Rural Gardens")), 
				new ChoiceItem("14.5", EAM.text("Artificial - Urban Areas")), 
				new ChoiceItem("14.6", EAM.text("Artificial - Subtropical/Tropical Heavily Degraded Former Forest")), 
				new ChoiceItem("15", EAM.text("Artificial - Aquatic")),
				new ChoiceItem("15.1", EAM.text("Artificial - Water Storage Areas (over 8 ha)")), 
				new ChoiceItem("15.2", EAM.text("Artificial - Ponds (below 8 ha)")), 
				new ChoiceItem("15.3", EAM.text("Artificial - Aquaculture Ponds")), 
				new ChoiceItem("15.4", EAM.text("Artificial - Salt Exploitation Sites")), 
				new ChoiceItem("15.5", EAM.text("Artificial - Excavations (open)")), 
				new ChoiceItem("15.6", EAM.text("Artificial - Wastewater Treatment Areas")), 
				new ChoiceItem("15.7", EAM.text("Artificial - Irrigated Land [includes irrigation channels]")), 
				new ChoiceItem("15.8", EAM.text("Artificial - Seasonally Flooded Agricultural Land")), 
				new ChoiceItem("15.9", EAM.text("Artificial - Canals and Drainage Channels, Ditches")), 
				new ChoiceItem("15.10", EAM.text("Artificial - Karst and Other Subterranean Hydrological Systems [human-made]")), 
				new ChoiceItem("15.11", EAM.text("Artificial - Marine Anthropogenic Structures")), 
				new ChoiceItem("15.12", EAM.text("Artificial - Mariculture Cages")), 
				new ChoiceItem("15.13", EAM.text("Artificial - Mari/Brackish-culture Ponds")), 
				new ChoiceItem("16", EAM.text("Introduced Vegetation")),
				new ChoiceItem("TNC1", EAM.text("Rivers, Creeks and Streams")),
				new ChoiceItem("TNC2", EAM.text("Riparian Areas")),
				new ChoiceItem("TNC3", EAM.text("Freshwater Lakes")),
				new ChoiceItem("17", EAM.text("Other")),
				new ChoiceItem("18", EAM.text("Unknown")),
		};
	}
	
	public static final String FOREST_CODE = "1";
	public static final String SAVANNA_CODE = "2";
}
