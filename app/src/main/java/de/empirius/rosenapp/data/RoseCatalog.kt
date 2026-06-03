package de.empirius.rosenapp.data

/** A known rose: its common/variety name, botanical (Latin) name, class, and era. */
data class RoseEntry(
    val name: String,
    val latinName: String,
    val type: RoseType,
    val era: RoseEra,
)

/**
 * A curated, bundled catalog of well-known roses used to power name
 * autocomplete and to pre-fill the Latin name. It is intentionally not
 * exhaustive — the name field still accepts any custom entry.
 *
 * Cultivars follow the botanical convention `Rosa 'Cultivar'`; species and
 * old-garden groups use their accepted binomials.
 */
object RoseCatalog {

    val roses: List<RoseEntry> = listOf(
        // --- Species & old garden roses ---
        RoseEntry("Dog Rose", "Rosa canina", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Rugosa Rose", "Rosa rugosa", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("French Rose", "Rosa gallica", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Apothecary's Rose", "Rosa gallica var. officinalis", RoseType.GALLICA, RoseEra.OLD_GARDEN),
        RoseEntry("Damask Rose", "Rosa × damascena", RoseType.DAMASK, RoseEra.OLD_GARDEN),
        RoseEntry("Cabbage Rose", "Rosa × centifolia", RoseType.CENTIFOLIA, RoseEra.OLD_GARDEN),
        RoseEntry("White Rose of York", "Rosa × alba", RoseType.ALBA, RoseEra.OLD_GARDEN),
        RoseEntry("Musk Rose", "Rosa moschata", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Lady Banks' Rose", "Rosa banksiae", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Redleaf Rose", "Rosa glauca", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Multiflora Rose", "Rosa multiflora", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Austrian Briar", "Rosa foetida", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Sweetbriar", "Rosa rubiginosa", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Burnet Rose", "Rosa spinosissima", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Chestnut Rose", "Rosa roxburghii", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Memorial Rose", "Rosa wichuraiana", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Cherokee Rose", "Rosa laevigata", RoseType.SPECIES, RoseEra.SPECIES),

        // --- Famous modern & David Austin cultivars ---
        RoseEntry("New Dawn", "Rosa 'New Dawn'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Peace", "Rosa 'Peace'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Iceberg", "Rosa 'Iceberg'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Schneewittchen", "Rosa 'Schneewittchen'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Climbing Iceberg", "Rosa 'Climbing Iceberg'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Queen Elizabeth", "Rosa 'Queen Elizabeth'", RoseType.GRANDIFLORA, RoseEra.MODERN),
        RoseEntry("Mister Lincoln", "Rosa 'Mister Lincoln'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Double Delight", "Rosa 'Double Delight'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Knock Out", "Rosa 'Knock Out'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Bonica", "Rosa 'Bonica'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Flower Carpet", "Rosa 'Flower Carpet'", RoseType.GROUNDCOVER, RoseEra.MODERN),
        RoseEntry("Graham Thomas", "Rosa 'Graham Thomas'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Gertrude Jekyll", "Rosa 'Gertrude Jekyll'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Lady of Shalott", "Rosa 'Lady of Shalott'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Munstead Wood", "Rosa 'Munstead Wood'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Eglantyne", "Rosa 'Eglantyne'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("The Generous Gardener", "Rosa 'The Generous Gardener'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Constance Spry", "Rosa 'Constance Spry'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("William Shakespeare 2000", "Rosa 'William Shakespeare 2000'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Fragrant Cloud", "Rosa 'Fragrant Cloud'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Just Joey", "Rosa 'Just Joey'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Blue Moon", "Rosa 'Blue Moon'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Compassion", "Rosa 'Compassion'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Sexy Rexy", "Rosa 'Sexy Rexy'", RoseType.FLORIBUNDA, RoseEra.MODERN),

        // --- Climbers, ramblers & old roses often grown in gardens ---
        RoseEntry("Zéphirine Drouhin", "Rosa 'Zéphirine Drouhin'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Cécile Brünner", "Rosa 'Cécile Brünner'", RoseType.POLYANTHA, RoseEra.MODERN),
        RoseEntry("Albertine", "Rosa 'Albertine'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Veilchenblau", "Rosa 'Veilchenblau'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Paul's Himalayan Musk", "Rosa 'Paul's Himalayan Musk'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Madame Alfred Carrière", "Rosa 'Madame Alfred Carrière'", RoseType.NOISETTE, RoseEra.OLD_GARDEN),
        RoseEntry("Gloire de Dijon", "Rosa 'Gloire de Dijon'", RoseType.TEA, RoseEra.OLD_GARDEN),
        RoseEntry("Souvenir de la Malmaison", "Rosa 'Souvenir de la Malmaison'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Madame Isaac Pereire", "Rosa 'Madame Isaac Pereire'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Charles de Mills", "Rosa 'Charles de Mills'", RoseType.GALLICA, RoseEra.OLD_GARDEN),
        RoseEntry("Rose de Rescht", "Rosa 'Rose de Rescht'", RoseType.PORTLAND, RoseEra.OLD_GARDEN),
        RoseEntry("Ghislaine de Féligonde", "Rosa 'Ghislaine de Féligonde'", RoseType.RAMBLER, RoseEra.MODERN),

        // --- Popular Kordes / Tantau garden roses ---
        RoseEntry("Westerland", "Rosa 'Westerland'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Sympathie", "Rosa 'Sympathie'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Rosarium Uetersen", "Rosa 'Rosarium Uetersen'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Leonardo da Vinci", "Rosa 'Leonardo da Vinci'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Nostalgie", "Rosa 'Nostalgie'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Aspirin Rose", "Rosa 'Aspirin Rose'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Lichtkönigin Lucia", "Rosa 'Lichtkönigin Lucia'", RoseType.SHRUB, RoseEra.MODERN),
    )

    /** Suggestions for [query], name-prefix matches first; empty query yields nothing. */
    fun search(query: String, limit: Int = 8): List<RoseEntry> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return emptyList()
        return roses
            .filter { it.name.lowercase().contains(q) || it.latinName.lowercase().contains(q) }
            .sortedBy { if (it.name.lowercase().startsWith(q)) 0 else 1 }
            .take(limit)
    }

    /** The catalog entry for an exact (case-insensitive) common-name match, if any. */
    fun entryFor(name: String): RoseEntry? {
        val n = name.trim()
        if (n.isEmpty()) return null
        return roses.firstOrNull { it.name.equals(n, ignoreCase = true) }
    }
}
