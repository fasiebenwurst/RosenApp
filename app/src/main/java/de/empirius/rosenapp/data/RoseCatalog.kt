package de.empirius.rosenapp.data

/** A known rose: its common/variety name and its botanical (Latin) name. */
data class RoseEntry(val name: String, val latinName: String)

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
        RoseEntry("Dog Rose", "Rosa canina"),
        RoseEntry("Rugosa Rose", "Rosa rugosa"),
        RoseEntry("French Rose", "Rosa gallica"),
        RoseEntry("Apothecary's Rose", "Rosa gallica var. officinalis"),
        RoseEntry("Damask Rose", "Rosa × damascena"),
        RoseEntry("Cabbage Rose", "Rosa × centifolia"),
        RoseEntry("White Rose of York", "Rosa × alba"),
        RoseEntry("Musk Rose", "Rosa moschata"),
        RoseEntry("Lady Banks' Rose", "Rosa banksiae"),
        RoseEntry("Redleaf Rose", "Rosa glauca"),
        RoseEntry("Multiflora Rose", "Rosa multiflora"),
        RoseEntry("Austrian Briar", "Rosa foetida"),
        RoseEntry("Sweetbriar", "Rosa rubiginosa"),
        RoseEntry("Burnet Rose", "Rosa spinosissima"),
        RoseEntry("Chestnut Rose", "Rosa roxburghii"),
        RoseEntry("Memorial Rose", "Rosa wichuraiana"),
        RoseEntry("Cherokee Rose", "Rosa laevigata"),

        // --- Famous modern & David Austin cultivars ---
        RoseEntry("New Dawn", "Rosa 'New Dawn'"),
        RoseEntry("Peace", "Rosa 'Peace'"),
        RoseEntry("Iceberg", "Rosa 'Iceberg'"),
        RoseEntry("Schneewittchen", "Rosa 'Schneewittchen'"),
        RoseEntry("Climbing Iceberg", "Rosa 'Climbing Iceberg'"),
        RoseEntry("Queen Elizabeth", "Rosa 'Queen Elizabeth'"),
        RoseEntry("Mister Lincoln", "Rosa 'Mister Lincoln'"),
        RoseEntry("Double Delight", "Rosa 'Double Delight'"),
        RoseEntry("Knock Out", "Rosa 'Knock Out'"),
        RoseEntry("Bonica", "Rosa 'Bonica'"),
        RoseEntry("Flower Carpet", "Rosa 'Flower Carpet'"),
        RoseEntry("Graham Thomas", "Rosa 'Graham Thomas'"),
        RoseEntry("Gertrude Jekyll", "Rosa 'Gertrude Jekyll'"),
        RoseEntry("Lady of Shalott", "Rosa 'Lady of Shalott'"),
        RoseEntry("Munstead Wood", "Rosa 'Munstead Wood'"),
        RoseEntry("Eglantyne", "Rosa 'Eglantyne'"),
        RoseEntry("The Generous Gardener", "Rosa 'The Generous Gardener'"),
        RoseEntry("Constance Spry", "Rosa 'Constance Spry'"),
        RoseEntry("William Shakespeare 2000", "Rosa 'William Shakespeare 2000'"),
        RoseEntry("Fragrant Cloud", "Rosa 'Fragrant Cloud'"),
        RoseEntry("Just Joey", "Rosa 'Just Joey'"),
        RoseEntry("Blue Moon", "Rosa 'Blue Moon'"),
        RoseEntry("Compassion", "Rosa 'Compassion'"),
        RoseEntry("Sexy Rexy", "Rosa 'Sexy Rexy'"),

        // --- Climbers, ramblers & old roses often grown in gardens ---
        RoseEntry("Zéphirine Drouhin", "Rosa 'Zéphirine Drouhin'"),
        RoseEntry("Cécile Brünner", "Rosa 'Cécile Brünner'"),
        RoseEntry("Albertine", "Rosa 'Albertine'"),
        RoseEntry("Veilchenblau", "Rosa 'Veilchenblau'"),
        RoseEntry("Paul's Himalayan Musk", "Rosa 'Paul's Himalayan Musk'"),
        RoseEntry("Madame Alfred Carrière", "Rosa 'Madame Alfred Carrière'"),
        RoseEntry("Gloire de Dijon", "Rosa 'Gloire de Dijon'"),
        RoseEntry("Souvenir de la Malmaison", "Rosa 'Souvenir de la Malmaison'"),
        RoseEntry("Madame Isaac Pereire", "Rosa 'Madame Isaac Pereire'"),
        RoseEntry("Charles de Mills", "Rosa 'Charles de Mills'"),
        RoseEntry("Rose de Rescht", "Rosa 'Rose de Rescht'"),
        RoseEntry("Ghislaine de Féligonde", "Rosa 'Ghislaine de Féligonde'"),

        // --- Popular Kordes / Tantau garden roses ---
        RoseEntry("Westerland", "Rosa 'Westerland'"),
        RoseEntry("Sympathie", "Rosa 'Sympathie'"),
        RoseEntry("Rosarium Uetersen", "Rosa 'Rosarium Uetersen'"),
        RoseEntry("Leonardo da Vinci", "Rosa 'Leonardo da Vinci'"),
        RoseEntry("Nostalgie", "Rosa 'Nostalgie'"),
        RoseEntry("Aspirin Rose", "Rosa 'Aspirin Rose'"),
        RoseEntry("Lichtkönigin Lucia", "Rosa 'Lichtkönigin Lucia'"),
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

    /** The Latin name for an exact (case-insensitive) common-name match, if any. */
    fun latinFor(name: String): String? {
        val n = name.trim()
        if (n.isEmpty()) return null
        return roses.firstOrNull { it.name.equals(n, ignoreCase = true) }?.latinName
    }
}
