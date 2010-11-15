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
				new ChoiceItemWithLongDescriptionProvider("1.1", EAM.text("Forest - Boreal")), 
				new ChoiceItemWithLongDescriptionProvider("1.2", EAM.text("Forest - Subarctic")), 
				new ChoiceItemWithLongDescriptionProvider("1.3", EAM.text("Forest - Subantarctic")), 
				new ChoiceItemWithLongDescriptionProvider("1.4", EAM.text("Forest - Temperate")), 
				new ChoiceItemWithLongDescriptionProvider("1.5", EAM.text("Forest - Subtropical/Tropical Dry")), 
				new ChoiceItemWithLongDescriptionProvider("1.6", EAM.text("Forest - Subtropical/Tropical Moist Lowland")), 
				new ChoiceItemWithLongDescriptionProvider("1.7", EAM.text("Forest - Subtropical/Tropical Mangrove Vegetation Above High Tide Level")), 
				new ChoiceItemWithLongDescriptionProvider("1.8", EAM.text("Forest - Subtropical/Tropical Swamp")), 
				new ChoiceItemWithLongDescriptionProvider("1.9", EAM.text("Forest - Subtropical/Tropical Moist Montane")), 
				new NonSelectableChoiceItem(SAVANNA_CODE, EAM.text("Savanna")),
				new ChoiceItemWithLongDescriptionProvider("2.1", EAM.text("Savanna - Dry Savanna")), 
				new ChoiceItemWithLongDescriptionProvider("2.2", EAM.text("Savanna - Moist Savana")), 
				new NonSelectableChoiceItem("3", EAM.text("Shrubland")),
				new ChoiceItemWithLongDescriptionProvider("3.1", EAM.text("Shrubland - Subarctic")), 
				new ChoiceItemWithLongDescriptionProvider("3.2", EAM.text("Shrubland - Subantarctic")), 
				new ChoiceItemWithLongDescriptionProvider("3.3", EAM.text("Shrubland - Boreal")), 
				new ChoiceItemWithLongDescriptionProvider("3.4", EAM.text("Shrubland - Temperate")), 
				new ChoiceItemWithLongDescriptionProvider("3.5", EAM.text("Shrubland - Subtropical/Tropical Dry")), 
				new ChoiceItemWithLongDescriptionProvider("3.6", EAM.text("Shrubland - Subtropical/Tropical Moist")), 
				new ChoiceItemWithLongDescriptionProvider("3.7", EAM.text("Shrubland - Subtropical/Tropical High Altitude")), 
				new ChoiceItemWithLongDescriptionProvider("3.8", EAM.text("Shrubland - Mediterranean-type Shrubby Vegetation")), 
				new NonSelectableChoiceItem("4", EAM.text("Grassland")),
				new ChoiceItemWithLongDescriptionProvider("4.1", EAM.text("Grassland - Tundra")), 
				new ChoiceItemWithLongDescriptionProvider("4.2", EAM.text("Grassland - Subarctic")), 
				new ChoiceItemWithLongDescriptionProvider("4.3", EAM.text("Grassland - Subantarctic")), 
				new ChoiceItemWithLongDescriptionProvider("4.4", EAM.text("Grassland - Temperate")), 
				new ChoiceItemWithLongDescriptionProvider("4.5", EAM.text("Grassland - Subtropical/Tropical Dry Lowland")),
				new ChoiceItemWithLongDescriptionProvider("4.6", EAM.text("Grassland - Subtropical/Tropical Seasonally Wet/Flooded Lowland")),
				new ChoiceItemWithLongDescriptionProvider("4.7", EAM.text("Grassland - Subtropical/Tropical High Altitude")),
				new NonSelectableChoiceItem("5", EAM.text("Wetlands (inland)")),
				new ChoiceItemWithLongDescriptionProvider("5.1", EAM.text("Wetlands - Permanent Rivers/Streams/Creeks [includes waterfalls]")),
				new ChoiceItemWithLongDescriptionProvider("5.2", EAM.text("Wetlands - Seasonal/Intermittent/Irregular Rivers/Streams/Creeks")),
				new ChoiceItemWithLongDescriptionProvider("5.3", EAM.text("Wetlands - Shrub Dominated Wetlands")),
				new ChoiceItemWithLongDescriptionProvider("5.4", EAM.text("Wetlands - Bogs, Marshes, Swamps, Fens, Peatlands")),
				new ChoiceItemWithLongDescriptionProvider("5.5", EAM.text("Wetlands - Permanent Freshwater Lakes [over 8 ha]")),
				new ChoiceItemWithLongDescriptionProvider("5.6", EAM.text("Wetlands - Seasonal/Intermittent Freshwater Lakes [over 8 ha]")),
				new ChoiceItemWithLongDescriptionProvider("5.7", EAM.text("Wetlands - Permanent Freshwater Marshes/Pools [under 8 ha]")),
				new ChoiceItemWithLongDescriptionProvider("5.8", EAM.text("Wetlands - Seasonal/Intermittent Freshwater Marshes/Pools [under 8 ha]")),
				new ChoiceItemWithLongDescriptionProvider("5.9", EAM.text("Wetlands - Freshwater Springs and Oases")),
				new ChoiceItemWithLongDescriptionProvider("5.10", EAM.text("Wetlands - Tundra Wetlands [includes pools and temporary waters from snowmelt]")),
				new ChoiceItemWithLongDescriptionProvider("5.11", EAM.text("Wetlands - Alpine Wetlands [includes temporary waters from snowmelt]")),
				new ChoiceItemWithLongDescriptionProvider("5.12", EAM.text("Wetlands - Geothermal Wetlands")),
				new ChoiceItemWithLongDescriptionProvider("5.13", EAM.text("Wetlands - Permanent Inland Deltas")),
				new ChoiceItemWithLongDescriptionProvider("5.14", EAM.text("Wetlands - Permanent Saline, Brackish or Alkaline Lakes")),
				new ChoiceItemWithLongDescriptionProvider("5.15", EAM.text("Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Lakes and Flats")),
				new ChoiceItemWithLongDescriptionProvider("5.16", EAM.text("Wetlands - Permanent Saline, Brackish or Alkaline Marshes/Pools")),
				new ChoiceItemWithLongDescriptionProvider("5.17", EAM.text("Wetlands - Seasonal/Intermittent Saline, Brackish or Alkaline Marshes/Pools")),
				new ChoiceItemWithLongDescriptionProvider("5.18", EAM.text("Wetlands - Karst and Other Subterranean Hydrological Systems [inland]")),
				new NonSelectableChoiceItem("6", EAM.text("Rocky Areas [e.g. inland cliffs, mountain peaks]")),
				new NonSelectableChoiceItem("7", EAM.text("Caves and Subterranean Habitats (non-aquatic)")),
				new ChoiceItemWithLongDescriptionProvider("7.1", EAM.text("- Caves")),
				new ChoiceItemWithLongDescriptionProvider("7.2", EAM.text("- Other Subterranean Habitats")),
				new NonSelectableChoiceItem("8", EAM.text("Desert")), 
				new ChoiceItemWithLongDescriptionProvider("8.1", EAM.text("Desert - Hot")),  
				new ChoiceItemWithLongDescriptionProvider("8.2", EAM.text("Desert - Temperate")),
				new ChoiceItemWithLongDescriptionProvider("8.3", EAM.text("Desert - Cold")),
				new NonSelectableChoiceItem("9", EAM.text("Marine Neritic (Submergent Nearshore Continental Shelf or Oceanic Island)")),
				new ChoiceItemWithLongDescriptionProvider("9.1", EAM.text("Marine Neritic - Pelagic")),
				new ChoiceItemWithLongDescriptionProvider("9.2", EAM.text("Marine Neritic - Subtidal Rock and Rocky Reefs")),
				new ChoiceItemWithLongDescriptionProvider("9.3", EAM.text("Marine Neritic - Subtidal Loose Rock/Pebble/Gravel")),
				new ChoiceItemWithLongDescriptionProvider("9.5", EAM.text("Marine Neritic - Subtidal Sandy")),
				new ChoiceItemWithLongDescriptionProvider("9.6", EAM.text("Marine Neritic - Subtidal Sandy-Mud")),
				new ChoiceItemWithLongDescriptionProvider("9.7", EAM.text("Marine Neritic - Macroalgal/Kelp")),
				new ChoiceItemWithLongDescriptionProvider("9.8", EAM.text("Marine Neritic - Coral Reef")),
				new ChoiceItemWithLongDescriptionProvider("9.8.1", EAM.text("Marine Neritic - Coral Reef - Outer Reef Channel")),
				new ChoiceItemWithLongDescriptionProvider("9.8.2", EAM.text("Marine Neritic - Coral Reef - Back Slope")),
				new ChoiceItemWithLongDescriptionProvider("9.8.3", EAM.text("Marine Neritic - Coral Reef - Foreslope (Outer Reef Slope)")),
				new ChoiceItemWithLongDescriptionProvider("9.8.4", EAM.text("Marine Neritic - Coral Reef - Lagoon")),
				new ChoiceItemWithLongDescriptionProvider("9.8.5", EAM.text("Marine Neritic - Coral Reef - Inter-Reef Soft Substrate")),
				new ChoiceItemWithLongDescriptionProvider("9.8.6", EAM.text("Marine Neritic - Coral Reef - Inter-Reef Rubble Substrate")),
				new ChoiceItemWithLongDescriptionProvider("9.9", EAM.text("Marine Neritic - Seagrass (Submerged)")),
				new ChoiceItemWithLongDescriptionProvider("9.10", EAM.text("Marine Neritic - Estuaries")),
				new NonSelectableChoiceItem("10", EAM.text("Marine Oceanic")), 
				new ChoiceItemWithLongDescriptionProvider("10.1", EAM.text("Marine Oceanic - Epipelagic (0-200 m)")),
				new ChoiceItemWithLongDescriptionProvider("10.2", EAM.text("Marine Oceanic - Mesopelagic (200-1,000 m)")),
				new ChoiceItemWithLongDescriptionProvider("10.3", EAM.text("Marine Oceanic - Bathypelagic (1,000-4,000 m")),
				new ChoiceItemWithLongDescriptionProvider("10.4", EAM.text("Marine Oceanic - Abyssopelagic (4,000-6,000 m)")),
				new NonSelectableChoiceItem("11", EAM.text("Marine Deep Benthic")), 
				new ChoiceItemWithLongDescriptionProvider("11.1", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone (200-4,000 m)")), 
				new ChoiceItemWithLongDescriptionProvider("11.1.1", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone - Hard Substrate")), 
				new ChoiceItemWithLongDescriptionProvider("11.1.2", EAM.text("Marine Deep Benthic - Continental Slope/Bathyl Zone - Soft Substrate")), 
				new ChoiceItemWithLongDescriptionProvider("11.2", EAM.text("Marine Deep Benthic - Abyssal Plain (4,000-6,000 m)")), 
				new ChoiceItemWithLongDescriptionProvider("11.3", EAM.text("Marine Deep Benthic - Abyssal Mountain/Hills (4,000-6,000 m)")), 
				new ChoiceItemWithLongDescriptionProvider("11.4", EAM.text("Marine Deep Benthic - Hadal/Deep Sea Trench (>6,000 m)")), 
				new ChoiceItemWithLongDescriptionProvider("11.5", EAM.text("Marine Deep Benthic - Seamount")), 
				new ChoiceItemWithLongDescriptionProvider("11.6", EAM.text("Marine Deep Benthic - Deep Sea Vents (Rifts/Seeps)")), 
				new NonSelectableChoiceItem("12", EAM.text("Marine Intertidal")),
				new ChoiceItemWithLongDescriptionProvider("12.1", EAM.text("Marine Intertidal - Rocky Shoreline")), 
				new ChoiceItemWithLongDescriptionProvider("12.2", EAM.text("Marine Intertidal - Sandy Shoreline and/or Beaches, Sand Bars, Spits, Etc.")), 
				new ChoiceItemWithLongDescriptionProvider("12.3", EAM.text("Marine Intertidal - Shingle and/or Pebble Shoreline and/or Beaches")), 
				new ChoiceItemWithLongDescriptionProvider("12.4", EAM.text("Marine Intertidal - Mud Flats and Salt Flats")), 
				new ChoiceItemWithLongDescriptionProvider("12.5", EAM.text("Marine Intertidal - Salt Marshes (Emergent Grasses)")), 
				new ChoiceItemWithLongDescriptionProvider("12.6", EAM.text("Marine Intertidal - Tidepools")), 
				new ChoiceItemWithLongDescriptionProvider("12.7", EAM.text("Marine Intertidal - Mangrove Submerged Roots")), 
				new NonSelectableChoiceItem("13", EAM.text("Marine Coastal/Supratidal")),
				new ChoiceItemWithLongDescriptionProvider("13.1", EAM.text("Marine Coastal/Supratidal - Sea Cliffs and Rocky Offshore Islands")), 
				new ChoiceItemWithLongDescriptionProvider("13.2", EAM.text("Marine Coastal/Supratidal - Coastal Caves/Karst")), 
				new ChoiceItemWithLongDescriptionProvider("13.3", EAM.text("Marine Coastal/Supratidal - Coastal Sand Dunes")), 
				new ChoiceItemWithLongDescriptionProvider("13.4", EAM.text("Marine Coastal/Supratidal - Coastal Brackish/Saline Lagoons/Marine Lakes")),
				new ChoiceItemWithLongDescriptionProvider("13.5", EAM.text("Marine Coastal/Supratidal - Coastal Freshwater Lakes")), 
				new NonSelectableChoiceItem("14", EAM.text("Artificial - Terrestrial")),
				new ChoiceItemWithLongDescriptionProvider("14.1", EAM.text("Artificial - Arable Land")),
				new ChoiceItemWithLongDescriptionProvider("14.2", EAM.text("Artificial - Pastureland")), 
				new ChoiceItemWithLongDescriptionProvider("14.3", EAM.text("Artificial - Plantations")), 
				new ChoiceItemWithLongDescriptionProvider("14.4", EAM.text("Artificial - Rural Gardens")), 
				new ChoiceItemWithLongDescriptionProvider("14.5", EAM.text("Artificial - Urban Areas")), 
				new ChoiceItemWithLongDescriptionProvider("14.6", EAM.text("Artificial - Subtropical/Tropical Heavily Degraded Former Forest")), 
				new NonSelectableChoiceItem("15", EAM.text("Artificial - Aquatic")),
				new ChoiceItemWithLongDescriptionProvider("15.1", EAM.text("Artificial - Water Storage Areas (over 8 ha)")), 
				new ChoiceItemWithLongDescriptionProvider("15.2", EAM.text("Artificial - Ponds (below 8 ha)")), 
				new ChoiceItemWithLongDescriptionProvider("15.3", EAM.text("Artificial - Aquaculture Ponds")), 
				new ChoiceItemWithLongDescriptionProvider("15.4", EAM.text("Artificial - Salt Exploitation Sites")), 
				new ChoiceItemWithLongDescriptionProvider("15.5", EAM.text("Artificial - Excavations (open)")), 
				new ChoiceItemWithLongDescriptionProvider("15.6", EAM.text("Artificial - Wastewater Treatment Areas")), 
				new ChoiceItemWithLongDescriptionProvider("15.7", EAM.text("Artificial - Irrigated Land [includes irrigation channels]")), 
				new ChoiceItemWithLongDescriptionProvider("15.8", EAM.text("Artificial - Seasonally Flooded Agricultural Land")), 
				new ChoiceItemWithLongDescriptionProvider("15.9", EAM.text("Artificial - Canals and Drainage Channels, Ditches")), 
				new ChoiceItemWithLongDescriptionProvider("15.10", EAM.text("Artificial - Karst and Other Subterranean Hydrological Systems [human-made]")), 
				new ChoiceItemWithLongDescriptionProvider("15.11", EAM.text("Artificial - Marine Anthropogenic Structures")), 
				new ChoiceItemWithLongDescriptionProvider("15.12", EAM.text("Artificial - Mariculture Cages")), 
				new ChoiceItemWithLongDescriptionProvider("15.13", EAM.text("Artificial - Mari/Brackish-culture Ponds")), 
				new NonSelectableChoiceItem("16", EAM.text("Introduced Vegetation")),
				new ChoiceItemWithLongDescriptionProvider("TNC1", EAM.text("Rivers, Creeks and Streams")),
				new ChoiceItemWithLongDescriptionProvider("TNC2", EAM.text("Riparian Areas")),
				new ChoiceItemWithLongDescriptionProvider("TNC3", EAM.text("Freshwater Lakes")),
				new ChoiceItemWithLongDescriptionProvider("17", EAM.text("Other")),
				new ChoiceItemWithLongDescriptionProvider("18", EAM.text("Unknown")),
		};
	}
	
	public static final String FOREST_CODE = "1";
	public static final String SAVANNA_CODE = "2";
}
