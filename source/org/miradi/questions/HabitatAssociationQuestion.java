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
				new NonSelectableChoiceItem(FOREST_CODE, EAM.text("Forest")),
				new ChoiceItemWithRowDescriptionProvider("1.1", EAM.text("Forest - Boreal")), 
				new ChoiceItemWithRowDescriptionProvider("1.2", EAM.text("Forest - Subarctic")), 
				new ChoiceItemWithRowDescriptionProvider("1.3", EAM.text("Forest - Subantarctic")), 
				new ChoiceItemWithRowDescriptionProvider("1.4", EAM.text("Forest - Temperate")), 
				new ChoiceItemWithRowDescriptionProvider("1.5", EAM.text("Forest - Subtropical/Tropical Dry")), 
				new ChoiceItemWithRowDescriptionProvider("1.6", EAM.text("Forest - Subtropical/Tropical Moist Lowland")), 
				new ChoiceItemWithRowDescriptionProvider("1.7", EAM.text("Forest - Subtropical/Tropical Mangrove Vegetation Above High Tide Level")), 
				new ChoiceItemWithRowDescriptionProvider("1.8", EAM.text("Forest - Subtropical/Tropical Swamp")), 
				new ChoiceItemWithRowDescriptionProvider("1.9", EAM.text("Forest - Subtropical/Tropical Moist Montane")), 
				new NonSelectableChoiceItem(SAVANNA_CODE, EAM.text("Savanna")),
				new ChoiceItemWithRowDescriptionProvider("2.1", EAM.text("Savanna - Dry Savanna")), 
				new ChoiceItemWithRowDescriptionProvider("2.2", EAM.text("Savanna - Moist Savana")), 
				new NonSelectableChoiceItem("3", EAM.text("Shrubland")),
				new ChoiceItemWithRowDescriptionProvider("3.1", EAM.text("Shrubland - Subarctic")), 
				new ChoiceItemWithRowDescriptionProvider("3.2", EAM.text("Shrubland - Subantarctic")), 
				new ChoiceItemWithRowDescriptionProvider("3.3", EAM.text("Shrubland - Boreal")), 
				new ChoiceItemWithRowDescriptionProvider("3.4", EAM.text("Shrubland - Temperate")), 
				new ChoiceItemWithRowDescriptionProvider("3.5", EAM.text("Shrubland - Subtropical/Tropical Dry")), 
				new ChoiceItemWithRowDescriptionProvider("3.6", EAM.text("Shrubland - Subtropical/Tropical Moist")), 
				new ChoiceItemWithRowDescriptionProvider("3.7", EAM.text("Shrubland - Subtropical/Tropical High Altitude")), 
				new ChoiceItemWithRowDescriptionProvider("3.8", EAM.text("Shrubland - Mediterranean-type Shrubby Vegetation")), 
				new NonSelectableChoiceItem("4", EAM.text("Grassland")),
				new ChoiceItemWithRowDescriptionProvider("4.1", EAM.text("Grassland - Tundra")), 
				new ChoiceItemWithRowDescriptionProvider("4.2", EAM.text("Grassland - Subarctic")), 
				new ChoiceItemWithRowDescriptionProvider("4.3", EAM.text("Grassland - Subantarctic")), 
				new ChoiceItemWithRowDescriptionProvider("4.4", EAM.text("Grassland - Temperate")), 
				new ChoiceItemWithRowDescriptionProvider("4.5", EAM.text("Grassland - Subtropical/Tropical Dry Lowland")),
				new ChoiceItemWithRowDescriptionProvider("4.6", EAM.text("Grassland - Subtropical/Tropical Seasonally Wet/Flooded Lowland")),
				new ChoiceItemWithRowDescriptionProvider("4.7", EAM.text("Grassland - Subtropical/Tropical High Altitude")),
				new NonSelectableChoiceItem("5", EAM.text("Wetlands (inland)")),
				new ChoiceItemWithRowDescriptionProvider("5.1", EAM.text("Wetlands - Permanent Rivers/Streams/Creeks [includes waterfalls]")),
				new ChoiceItemWithRowDescriptionProvider("5.2", EAM.text("Wetlands - Seasonal/Intermittent/Irregular Rivers/Streams/Creeks")),
				new ChoiceItemWithRowDescriptionProvider("5.3", EAM.text("Wetlands - Shrub Dominated Wetlands")),
				new ChoiceItemWithRowDescriptionProvider("5.4", EAM.text("Wetlands - Bogs, Marshes, Swamps, Fens, Peatlands")),
				new ChoiceItemWithRowDescriptionProvider("5.5", EAM.text("Wetlands - Permanent Freshwater Lakes [over 8 ha]")),
				new ChoiceItemWithRowDescriptionProvider("5.6", EAM.text("Wetlands - Seasonal/Intermittent Freshwater Lakes [over 8 ha]")),
				new ChoiceItemWithRowDescriptionProvider("5.7", EAM.text("Wetlands - Permanent Freshwater Marshes/Pools [under 8 ha]")),
				new ChoiceItemWithRowDescriptionProvider("5.8", EAM.text("Wetlands - Seasonal/Intermittent Freshwater Marshes/Pools [under 8 ha]")),
				new ChoiceItemWithRowDescriptionProvider("5.9", EAM.text("Wetlands - Freshwater Springs and Oases")),
				new ChoiceItemWithRowDescriptionProvider("5.10", EAM.text("Wetlands - Tundra Wetlands [includes pools and temporary waters from snowmelt]")),
				new ChoiceItemWithRowDescriptionProvider("5.11", EAM.text("Wetlands - Alpine Wetlands [includes temporary waters from snowmelt]")),
				new ChoiceItemWithRowDescriptionProvider("5.12", EAM.text("Wetlands - Geothermal Wetlands")),
				new ChoiceItemWithRowDescriptionProvider("5.13", EAM.text("Wetlands - Permanent Inland Deltas")),
				new ChoiceItemWithRowDescriptionProvider("5.14", EAM.text("Wetlands - Permanent Saline, Brackish or Alkaline Lakes")),
				new ChoiceItemWithRowDescriptionProvider("5.15", EAM.text("Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Lakes and Flats")),
				new ChoiceItemWithRowDescriptionProvider("5.16", EAM.text("Wetlands - Permanent Saline, Brackish or Alkaline Marshes/Pools")),
				new ChoiceItemWithRowDescriptionProvider("5.17", EAM.text("Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Marshes/Pools")),
				new ChoiceItemWithRowDescriptionProvider("5.18", EAM.text("Wetlands - Karst and Other Subterranean Hydrological Systems [inland]")),
				new NonSelectableChoiceItem("6", EAM.text("Rocky Areas [e.g. inland cliffs, mountain peaks]")),
				new NonSelectableChoiceItem("7", EAM.text("Caves and Subterranean Habitats (non-aquatic)")),
				new ChoiceItemWithRowDescriptionProvider("7.1", EAM.text("- Caves")),
				new ChoiceItemWithRowDescriptionProvider("7.2", EAM.text("- Other Subterranean Habitats")),
				new NonSelectableChoiceItem("8", EAM.text("Desert")), 
				new ChoiceItemWithRowDescriptionProvider("8.1", EAM.text("Desert - Hot")),  
				new ChoiceItemWithRowDescriptionProvider("8.2", EAM.text("Desert - Temperate")),
				new ChoiceItemWithRowDescriptionProvider("8.3", EAM.text("Desert - Cold")),
				new NonSelectableChoiceItem("9", EAM.text("Marine Neritic (Submergent Nearshore Continental Shelf or Oceanic Island)")),
				new ChoiceItemWithRowDescriptionProvider("9.1", EAM.text("Marine Neritic - Pelagic")),
				new ChoiceItemWithRowDescriptionProvider("9.2", EAM.text("Marine Neritic - Subtidal Rock and Rocky Reefs")),
				new ChoiceItemWithRowDescriptionProvider("9.3", EAM.text("Marine Neritic - Subtidal Loose Rock/Pebble/Gravel")),
				new ChoiceItemWithRowDescriptionProvider("9.5", EAM.text("Marine Neritic - Subtidal Sandy")),
				new ChoiceItemWithRowDescriptionProvider("9.6", EAM.text("Marine Neritic - Subtidal Sandy-Mud")),
				new ChoiceItemWithRowDescriptionProvider("9.7", EAM.text("Marine Neritic - Macroalgal/Kelp")),
				new ChoiceItemWithRowDescriptionProvider("9.8", EAM.text("Marine Neritic - Coral Reef")),
				new ChoiceItemWithRowDescriptionProvider("9.8.1", EAM.text("Marine Neritic - Coral Reef - Outer Reef Channel")),
				new ChoiceItemWithRowDescriptionProvider("9.8.2", EAM.text("Marine Neritic - Coral Reef - Back Slope")),
				new ChoiceItemWithRowDescriptionProvider("9.8.3", EAM.text("Marine Neritic - Coral Reef - Foreslope (Outer Reef Slope)")),
				new ChoiceItemWithRowDescriptionProvider("9.8.4", EAM.text("Marine Neritic - Coral Reef - Lagoon")),
				new ChoiceItemWithRowDescriptionProvider("9.8.5", EAM.text("Marine Neritic - Coral Reef - Inter-Reef Soft Substrate")),
				new ChoiceItemWithRowDescriptionProvider("9.8.6", EAM.text("Marine Neritic - Coral Reef - Inter-Reef Rubble Substrate")),
				new ChoiceItemWithRowDescriptionProvider("9.9", EAM.text("Marine Neritic - Seagrass (Submerged)")),
				new ChoiceItemWithRowDescriptionProvider("9.10", EAM.text("Marine Neritic - Estuaries")),
				new NonSelectableChoiceItem("10", EAM.text("Marine Oceanic")), 
				new ChoiceItemWithRowDescriptionProvider("10.1", EAM.text("Marine Oceanic - Epipelagic (0-200 m)")),
				new ChoiceItemWithRowDescriptionProvider("10.2", EAM.text("Marine Oceanic - Mesopelagic (200-1,000 m)")),
				new ChoiceItemWithRowDescriptionProvider("10.3", EAM.text("Marine Oceanic - Bathypelagic (1,000-4,000 m")),
				new ChoiceItemWithRowDescriptionProvider("10.4", EAM.text("Marine Oceanic - Abyssopelagic (4,000-6,000 m)")),
				new NonSelectableChoiceItem("11", EAM.text("Marine Deep Benthic")), 
				new ChoiceItemWithRowDescriptionProvider("11.1", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone (200-4,000 m)")), 
				new ChoiceItemWithRowDescriptionProvider("11.1.1", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone - Hard Substrate")), 
				new ChoiceItemWithRowDescriptionProvider("11.1.2", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone - Soft Substrate")), 
				new ChoiceItemWithRowDescriptionProvider("11.2", EAM.text("Marine Deep Benthic - Abyssal Plain (4,000-6,000 m)")), 
				new ChoiceItemWithRowDescriptionProvider("11.3", EAM.text("Marine Deep Benthic - Abyssal Mountain/Hills (4,000-6,000 m)")), 
				new ChoiceItemWithRowDescriptionProvider("11.4", EAM.text("Marine Deep Benthic - Hadal/Deep Sea Trench (>6,000 m)")), 
				new ChoiceItemWithRowDescriptionProvider("11.5", EAM.text("Marine Deep Benthic - Seamount")), 
				new ChoiceItemWithRowDescriptionProvider("11.6", EAM.text("Marine Deep Benthic - Deep Sea Vents (Rifts/Seeps)")), 
				new NonSelectableChoiceItem("12", EAM.text("Marine Intertidal")),
				new ChoiceItemWithRowDescriptionProvider("12.1", EAM.text("Marine Intertidal - Rocky Shoreline")), 
				new ChoiceItemWithRowDescriptionProvider("12.2", EAM.text("Marine Intertidal - Sandy Shoreline and/or Beaches, Sand Bars, Spits, Etc.")), 
				new ChoiceItemWithRowDescriptionProvider("12.3", EAM.text("Marine Intertidal - Shingle and/or Pebble Shoreline and/or Beaches")), 
				new ChoiceItemWithRowDescriptionProvider("12.4", EAM.text("Marine Intertidal - Mud Flats and Salt Flats")), 
				new ChoiceItemWithRowDescriptionProvider("12.5", EAM.text("Marine Intertidal - Salt Marshes (Emergent Grasses)")), 
				new ChoiceItemWithRowDescriptionProvider("12.6", EAM.text("Marine Intertidal - Tidepools")), 
				new ChoiceItemWithRowDescriptionProvider("12.7", EAM.text("Marine Intertidal - Mangrove Submerged Roots")), 
				new NonSelectableChoiceItem("13", EAM.text("Marine Coastal/Supratidal")),
				new ChoiceItemWithRowDescriptionProvider("13.1", EAM.text("Marine Coastal/Supratidal - Sea Cliffs and Rocky Offshore Islands")), 
				new ChoiceItemWithRowDescriptionProvider("13.2", EAM.text("Marine Coastal/Supratidal - Coastal Caves/Karst")), 
				new ChoiceItemWithRowDescriptionProvider("13.3", EAM.text("Marine Coastal/Supratidal - Coastal Sand Dunes")), 
				new ChoiceItemWithRowDescriptionProvider("13.4", EAM.text("Marine Coastal/Supratidal - Coastal Brackish/Saline Lagoons/Marine Lakes")),
				new ChoiceItemWithRowDescriptionProvider("13.5", EAM.text("Marine Coastal/Supratidal - Coastal Freshwater Lakes")), 
				new NonSelectableChoiceItem("14", EAM.text("Artificial - Terrestrial")),
				new ChoiceItemWithRowDescriptionProvider("14.1", EAM.text("Artificial - Arable Land")),
				new ChoiceItemWithRowDescriptionProvider("14.2", EAM.text("Artificial - Pastureland")), 
				new ChoiceItemWithRowDescriptionProvider("14.3", EAM.text("Artificial - Plantations")), 
				new ChoiceItemWithRowDescriptionProvider("14.4", EAM.text("Artificial - Rural Gardens")), 
				new ChoiceItemWithRowDescriptionProvider("14.5", EAM.text("Artificial - Urban Areas")), 
				new ChoiceItemWithRowDescriptionProvider("14.6", EAM.text("Artificial - Subtropical/Tropical Heavily Degraded Former Forest")), 
				new NonSelectableChoiceItem("15", EAM.text("Artificial - Aquatic")),
				new ChoiceItemWithRowDescriptionProvider("15.1", EAM.text("Artificial - Water Storage Areas (over 8 ha)")), 
				new ChoiceItemWithRowDescriptionProvider("15.2", EAM.text("Artificial - Ponds (below 8 ha)")), 
				new ChoiceItemWithRowDescriptionProvider("15.3", EAM.text("Artificial - Aquaculture Ponds")), 
				new ChoiceItemWithRowDescriptionProvider("15.4", EAM.text("Artificial - Salt Exploitation Sites")), 
				new ChoiceItemWithRowDescriptionProvider("15.5", EAM.text("Artificial - Excavations (open)")), 
				new ChoiceItemWithRowDescriptionProvider("15.6", EAM.text("Artificial - Wastewater Treatment Areas")), 
				new ChoiceItemWithRowDescriptionProvider("15.7", EAM.text("Artificial - Irrigated Land [includes irrigation channels]")), 
				new ChoiceItemWithRowDescriptionProvider("15.8", EAM.text("Artificial - Seasonally Flooded Agricultural Land")), 
				new ChoiceItemWithRowDescriptionProvider("15.9", EAM.text("Artificial - Canals and Drainage Channels, Ditches")), 
				new ChoiceItemWithRowDescriptionProvider("15.10", EAM.text("Artificial - Karst and Other Subterranean Hydrological Systems [human-made]")), 
				new ChoiceItemWithRowDescriptionProvider("15.11", EAM.text("Artificial - Marine Anthropogenic Structures")), 
				new ChoiceItemWithRowDescriptionProvider("15.12", EAM.text("Artificial - Mariculture Cages")), 
				new ChoiceItemWithRowDescriptionProvider("15.13", EAM.text("Artificial - Mari/Brackish-culture Ponds")), 
				new NonSelectableChoiceItem("16", EAM.text("Introduced Vegetation")),
				new ChoiceItemWithRowDescriptionProvider("TNC1", EAM.text("Rivers, Creeks and Streams")),
				new ChoiceItemWithRowDescriptionProvider("TNC2", EAM.text("Riparian Areas")),
				new ChoiceItemWithRowDescriptionProvider("TNC3", EAM.text("Freshwater Lakes")),
				new ChoiceItemWithRowDescriptionProvider("17", EAM.text("Other")),
				new ChoiceItemWithRowDescriptionProvider("18", EAM.text("Unknown")),
		};
	}
	
	public static final String FOREST_CODE = "1";
	public static final String SAVANNA_CODE = "2";
}
