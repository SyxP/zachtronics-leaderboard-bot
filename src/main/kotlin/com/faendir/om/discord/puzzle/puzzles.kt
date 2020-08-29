package com.faendir.om.discord.puzzle

import com.faendir.om.discord.puzzle.Group.*
import com.faendir.om.discord.puzzle.Type.*

enum class Group(val displayName: String) {
    CHAPTER_1("Chapter 1"),
    CHAPTER_2("Chapter 2"),
    CHAPTER_3("Chapter 3"),
    CHAPTER_4("Chapter 4"),
    CHAPTER_5("Chapter 5"),
    CHAPTER_PRODUCTION("Production Alchemy"),
    JOURNAL_I("Journal I"),
    JOURNAL_II("Journal II"),
    JOURNAL_III("Journal III"),
    JOURNAL_IV("Journal IV"),
    JOURNAL_V("Journal V"),
    JOURNAL_VI("Journal VI"),
    JOURNAL_VII("Journal VII"),
    JOURNAL_VIII("Journal VIII"),
    JOURNAL_IX("Journal IX"),
    TOURNAMENT_2019("Tournament");
}

enum class Type {
    NORMAL,
    INFINITE,
    PRODUCTION,
}


@Suppress("unused", "SpellCheckingInspection")
enum class Puzzle(val group: Group, val type: Type, val displayName:String) {
    STABILIZED_WATER(CHAPTER_1, NORMAL, "Stabilized Water"),
    REFINED_GOLD(CHAPTER_1, NORMAL, "Refined Gold"),
    FACE_POWDER(CHAPTER_1, NORMAL, "Face Powder"),
    WATERPROOF_SEALANT(CHAPTER_1, NORMAL, "Waterproof Sealant"),
    HANGOVER_CURE(CHAPTER_1, NORMAL, "Hangover Cure"),
    AIRSHIP_FUEL(CHAPTER_1, NORMAL, "Airship Fuel"),
    PRECISION_MACHINE_OIL(CHAPTER_1, NORMAL, "Precision Machine Oil"),
    HEALTH_TONIC(CHAPTER_1, NORMAL, "Health Tonic"),
    STAMINA_POTION(CHAPTER_1, NORMAL, "Stamina Potion"),

    HAIR_PRODUCT(CHAPTER_2, NORMAL, "Hair Product"),
    ROCKET_PROPELLANT(CHAPTER_2, NORMAL, "Rocket Propellant"),
    MIST_OF_INCAPACITATION(CHAPTER_2, NORMAL, "Mist of Incapacitation"),
    EXPLOSIVE_PHIAL(CHAPTER_2, NORMAL, "Explosive Phial"),
    ARMOR_FILAMENT(CHAPTER_2, INFINITE, "Armor Filament"),
    COURAGE_POTION(CHAPTER_2, NORMAL, "Courage Potion"),
    SURRENDER_FLARE(CHAPTER_2, NORMAL, "Surrender Flare"),

    ALCOHOL_SEPARATION(CHAPTER_3, NORMAL, "Alcohol Separation"),
    WATER_PURIFIER(CHAPTER_3, NORMAL, "Water Purifier"),
    SEAL_SOLVENT(CHAPTER_3, NORMAL, "Seal Solvent"),
    CLIMBING_ROPE_FIBER(CHAPTER_3, INFINITE, "Climbing Rope Fiber"),
    WARMING_TONIC(CHAPTER_3, NORMAL, "Warming Tonic"),
    LIFE_SENSING_POTION(CHAPTER_3, NORMAL, "Life-Sensing Potion"),
    VERY_DARK_THREAD(CHAPTER_3, INFINITE, "Very Dark Thread"),

    LITHARGE_SEPARATION(CHAPTER_4, NORMAL, "Litharge Separation"),
    STAIN_REMOVER(CHAPTER_4, NORMAL, "Stain Remover"),
    SWORD_ALLOY(CHAPTER_4, INFINITE, "Sword Alloy"),
    INVISIBLE_INK(CHAPTER_4, NORMAL, "Invisible Ink"),
    PURIFIED_GOLD(CHAPTER_4, NORMAL, "Purified Gold"),
    ALCHEMICAL_JEWEL(CHAPTER_4, NORMAL, "Alchemical Jewel"),
    GOLDEN_THREAD(CHAPTER_4, INFINITE, "Golden Thread"),

    MIST_OF_HALLUCINATION(CHAPTER_5, NORMAL, "Mist of Hallucination"),
    TIMING_CRYSTAL(CHAPTER_5, NORMAL, "Timing Crystal"),
    VOLTAIC_COIL(CHAPTER_5, INFINITE, "Voltaic Coil"),
    UNSTABLE_COMPOUND(CHAPTER_5, NORMAL, "Unstable Compound"),
    CURIOUS_LIPSTICK(CHAPTER_5, NORMAL, "Curious Lipstick"),
    UNIVERSAL_SOLVENT(CHAPTER_5, NORMAL, "Universal Solvent"),
    
    SILVER_PAINT(CHAPTER_PRODUCTION, PRODUCTION, "Silver Paint"),
    VISCOUS_SLUDGE(CHAPTER_PRODUCTION, PRODUCTION, "Viscous Sludge"),
    FRAGRANT_POWDERS(CHAPTER_PRODUCTION, PRODUCTION, "Fragrant Powders"),
    RAT_POISON(CHAPTER_PRODUCTION, PRODUCTION, "Rat Poison"),
    SPECIAL_AMARO(CHAPTER_PRODUCTION, PRODUCTION, "Special Amaro"),
    VAPOR_OF_LEVITY(CHAPTER_PRODUCTION, PRODUCTION, "Vapor of Levity"),
    ABRASIVE_PARTICLES(CHAPTER_PRODUCTION, PRODUCTION, "Abrasive Particles"),
    EYEDROPS_OF_REVELATION(CHAPTER_PRODUCTION, PRODUCTION, "Eyedrops of Revelation"),
    PARADE_ROCKET_FUEL(CHAPTER_PRODUCTION, PRODUCTION, "Parade-Rocket Fuel"),
    AETHER_DETECTOR(CHAPTER_PRODUCTION, PRODUCTION, "Aether Detector"),
    RECONSTRUCTED_SOLVENT(CHAPTER_PRODUCTION, PRODUCTION, "Reconstructed Solvent"),
    
    VAN_BERLO_S_WHEEL(JOURNAL_I, NORMAL, "Van Berlo's Wheel"),
    VAN_BERLO_S_CHAIN(JOURNAL_I, INFINITE, "Van Berlo's Chain"),
    REACTIVE_CINNABAR(JOURNAL_I, NORMAL, "Reactive Cinnabar"),
    SILVER_CAUSTIC(JOURNAL_I, NORMAL, "Silver Caustic"),
    LAMBENT_II_IX(JOURNAL_I, NORMAL, "Lambent II/IX"),
    
    EXPLORER_S_SALVE(JOURNAL_II, NORMAL, "Explorer's Salve"),
    PRESERVATIVE_SALT(JOURNAL_II, NORMAL, "Preservative Salt"),
    SAILCLOTH_THREAD(JOURNAL_II, INFINITE, "Sailcloth Thread"),
    BUOYANT_CABLE(JOURNAL_II, INFINITE, "Buoyant Cable"),
    SPYGLASS_CRYSTAL(JOURNAL_II, NORMAL, "Spyglass Crystal"),
    
    RAVARI_S_WHEEL(JOURNAL_III, NORMAL, "Ravari's Wheel"),
    LUBRICATING_FILAMENT(JOURNAL_III, INFINITE, "Lubricating Filament"),
    RESONANT_CRYSTAL(JOURNAL_III, NORMAL, "Resonant Crystal"),
    REFINED_BRONZE(JOURNAL_III, INFINITE, "Refined Bronze"),
    ABLATIVE_CRYSTAL(JOURNAL_III, NORMAL, "Ablative Crystal"),
    
    PROOF_OF_COMPLETENESS(JOURNAL_IV, NORMAL, "Proof of Completeness"),
    WHEEL_REPRESENTATION(JOURNAL_IV, NORMAL, "Wheel Representation"),
    SYNTHESIS_VIA_ALCOHOL(JOURNAL_IV, NORMAL, "Synthesis via Alcohol"),
    UNIVERSAL_COMPOUND(JOURNAL_IV, NORMAL, "Universal Compound"),

    GENERAL_ANAESTHETIC(JOURNAL_V, NORMAL, "General Anaesthetic"),
    WAKEFULNESS_POTION(JOURNAL_V, NORMAL, "Wakefulness Potion"),
    SUTURE_THREAD(JOURNAL_V, INFINITE, "Suture Thread"),
    BLOOD_STANCHING_POWDER(JOURNAL_V, NORMAL, "Blood-Stanching Powder"),
    TONIC_OF_HYDRATION(JOURNAL_V, NORMAL, "Tonic of Hydration"),
    
    HEXSTABILIZED_SALT(JOURNAL_VI, PRODUCTION, "Hexstabilized Salt"),
    LUSTRE(JOURNAL_VI, PRODUCTION, "Lustre"),
    LAMPLIGHT_GAS(JOURNAL_VI, PRODUCTION, "Lamplight Gas"),
    CONDUCTIVE_ENAMEL(JOURNAL_VI, PRODUCTION, "Conductive Enamel"),
    WELDING_THERMITE(JOURNAL_VI, PRODUCTION, "Welding Thermite"),

    VAN_BERLO_S_PIVOTS(JOURNAL_VII, NORMAL, "Van Berlo's Pivots"),
    REACTIVE_GOLD(JOURNAL_VII, NORMAL, "Reactive Gold"),
    ASSASSIN_S_FILAMENT(JOURNAL_VII, INFINITE, "Assassin's Filament"),
    VAPOROUS_SOLVENT(JOURNAL_VII, NORMAL, "Vaporous Solvent"),
    ALCHEMICAL_SLAG(JOURNAL_VII, NORMAL, "Alchemical Slag"),

    EXPLOSIVE_VICTRITE(JOURNAL_VIII, NORMAL, "Explosive Victrite"),
    CELESTIAL_THREAD(JOURNAL_VIII, INFINITE, "Celestial Thread"),
    VISILLARY_ANAESTHETIC(JOURNAL_VIII, NORMAL, "Visillary Anaesthetic"),
    ANIMISMUS_BUFFER(JOURNAL_VIII, NORMAL, "Animismus Buffer"),
    ELECTRUM_SEPARATION(JOURNAL_VIII, INFINITE, "Electrum Separation"),

    HYPER_VOLATILE_GAS(JOURNAL_IX, NORMAL, "Hyper-volatile Gas"),
    VANISHING_MATERIAL(JOURNAL_IX, PRODUCTION, "Vanishing Material"),
    SYNTHETIC_MALACHITE(JOURNAL_IX, PRODUCTION, "Synthetic Malachite"),
    EMBALMING_FLUID(JOURNAL_IX, NORMAL, "Embalming Fluid"),
    QUINTESSENTIAL_MEDIUM(JOURNAL_IX, NORMAL, "Quintessential Medium"),

    UNWINDING(TOURNAMENT_2019, NORMAL, "Unwinding"),
    WIRE_FORMING_AND_UNFORMING(TOURNAMENT_2019, INFINITE, "Wire Forming and Unforming"),
    CREATIVE_ACCOUNTING(TOURNAMENT_2019, NORMAL, "Creative Accounting"),
    VIRULENT_VECTOR(TOURNAMENT_2019, NORMAL, "Virulent Vector"),
    DWARVEN_FIRE_WINE(TOURNAMENT_2019, PRODUCTION, "Dwarven Fire Wine"),
    MIRACULOUS_AUTOSALT(TOURNAMENT_2019, NORMAL, "Miraculous Autosalt"),
    DO_YOU_REMEMBER(TOURNAMENT_2019, NORMAL, "Do You Remember"),
    EVIL_ORE(TOURNAMENT_2019, NORMAL, "Evil Ore"),
    PANACEA(TOURNAMENT_2019, NORMAL, "Panacea")
    ;


    companion object {
        fun findByName(name: String): List<Puzzle> = values().filter { it.displayName.contains(name, ignoreCase = true) }
    }
}