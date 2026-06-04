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

        // --- More wild species ---
        RoseEntry("Virginia Rose", "Rosa virginiana", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Swamp Rose", "Rosa palustris", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Field Rose", "Rosa arvensis", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Alpine Rose", "Rosa pendulina", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Winged Thorn Rose", "Rosa sericea f. pteracantha", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Incense Rose", "Rosa primula", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Nootka Rose", "Rosa nutkana", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Apple Rose", "Rosa villosa", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Mandarin Rose", "Rosa moyesii", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Macartney Rose", "Rosa bracteata", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Carolina Rose", "Rosa carolina", RoseType.SPECIES, RoseEra.SPECIES),
        RoseEntry("Father Hugo's Rose", "Rosa hugonis", RoseType.SPECIES, RoseEra.SPECIES),

        // --- Gallica (old garden) ---
        RoseEntry("Rosa Mundi", "Rosa gallica 'Versicolor'", RoseType.GALLICA, RoseEra.OLD_GARDEN),
        RoseEntry("Cardinal de Richelieu", "Rosa 'Cardinal de Richelieu'", RoseType.GALLICA, RoseEra.OLD_GARDEN),
        RoseEntry("Tuscany Superb", "Rosa 'Tuscany Superb'", RoseType.GALLICA, RoseEra.OLD_GARDEN),
        RoseEntry("Belle de Crécy", "Rosa 'Belle de Crécy'", RoseType.GALLICA, RoseEra.OLD_GARDEN),
        RoseEntry("Complicata", "Rosa 'Complicata'", RoseType.GALLICA, RoseEra.OLD_GARDEN),

        // --- Damask (old garden) ---
        RoseEntry("Madame Hardy", "Rosa 'Madame Hardy'", RoseType.DAMASK, RoseEra.OLD_GARDEN),
        RoseEntry("Ispahan", "Rosa 'Ispahan'", RoseType.DAMASK, RoseEra.OLD_GARDEN),
        RoseEntry("Celsiana", "Rosa 'Celsiana'", RoseType.DAMASK, RoseEra.OLD_GARDEN),
        RoseEntry("Kazanlik", "Rosa × damascena 'Trigintipetala'", RoseType.DAMASK, RoseEra.OLD_GARDEN),

        // --- Alba (old garden) ---
        RoseEntry("Königin von Dänemark", "Rosa 'Königin von Dänemark'", RoseType.ALBA, RoseEra.OLD_GARDEN),
        RoseEntry("Great Maiden's Blush", "Rosa 'Great Maiden's Blush'", RoseType.ALBA, RoseEra.OLD_GARDEN),
        RoseEntry("Félicité Parmentier", "Rosa 'Félicité Parmentier'", RoseType.ALBA, RoseEra.OLD_GARDEN),
        RoseEntry("Celestial", "Rosa 'Celestial'", RoseType.ALBA, RoseEra.OLD_GARDEN),

        // --- Centifolia & Moss (old garden) ---
        RoseEntry("Fantin-Latour", "Rosa 'Fantin-Latour'", RoseType.CENTIFOLIA, RoseEra.OLD_GARDEN),
        RoseEntry("Tour de Malakoff", "Rosa 'Tour de Malakoff'", RoseType.CENTIFOLIA, RoseEra.OLD_GARDEN),
        RoseEntry("Petite de Hollande", "Rosa 'Petite de Hollande'", RoseType.CENTIFOLIA, RoseEra.OLD_GARDEN),
        RoseEntry("Common Moss", "Rosa × centifolia 'Muscosa'", RoseType.MOSS, RoseEra.OLD_GARDEN),
        RoseEntry("William Lobb", "Rosa 'William Lobb'", RoseType.MOSS, RoseEra.OLD_GARDEN),
        RoseEntry("Henri Martin", "Rosa 'Henri Martin'", RoseType.MOSS, RoseEra.OLD_GARDEN),
        RoseEntry("Nuits de Young", "Rosa 'Nuits de Young'", RoseType.MOSS, RoseEra.OLD_GARDEN),

        // --- Portland & China (old garden) ---
        RoseEntry("Comte de Chambord", "Rosa 'Comte de Chambord'", RoseType.PORTLAND, RoseEra.OLD_GARDEN),
        RoseEntry("Jacques Cartier", "Rosa 'Jacques Cartier'", RoseType.PORTLAND, RoseEra.OLD_GARDEN),
        RoseEntry("Old Blush", "Rosa 'Old Blush'", RoseType.CHINA, RoseEra.OLD_GARDEN),
        RoseEntry("Mutabilis", "Rosa 'Mutabilis'", RoseType.CHINA, RoseEra.OLD_GARDEN),
        RoseEntry("Cramoisi Supérieur", "Rosa 'Cramoisi Supérieur'", RoseType.CHINA, RoseEra.OLD_GARDEN),
        RoseEntry("Hermosa", "Rosa 'Hermosa'", RoseType.CHINA, RoseEra.OLD_GARDEN),

        // --- Tea & Noisette (old garden) ---
        RoseEntry("Lady Hillingdon", "Rosa 'Lady Hillingdon'", RoseType.TEA, RoseEra.OLD_GARDEN),
        RoseEntry("Sombreuil", "Rosa 'Sombreuil'", RoseType.TEA, RoseEra.OLD_GARDEN),
        RoseEntry("Maman Cochet", "Rosa 'Maman Cochet'", RoseType.TEA, RoseEra.OLD_GARDEN),
        RoseEntry("Mrs B. R. Cant", "Rosa 'Mrs B. R. Cant'", RoseType.TEA, RoseEra.OLD_GARDEN),
        RoseEntry("Blush Noisette", "Rosa 'Blush Noisette'", RoseType.NOISETTE, RoseEra.OLD_GARDEN),
        RoseEntry("Rêve d'Or", "Rosa 'Rêve d'Or'", RoseType.NOISETTE, RoseEra.OLD_GARDEN),
        RoseEntry("Lamarque", "Rosa 'Lamarque'", RoseType.NOISETTE, RoseEra.OLD_GARDEN),
        RoseEntry("Crépuscule", "Rosa 'Crépuscule'", RoseType.NOISETTE, RoseEra.OLD_GARDEN),

        // --- Bourbon (old garden) ---
        RoseEntry("Louise Odier", "Rosa 'Louise Odier'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Madame Pierre Oger", "Rosa 'Madame Pierre Oger'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Boule de Neige", "Rosa 'Boule de Neige'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Honorine de Brabant", "Rosa 'Honorine de Brabant'", RoseType.BOURBON, RoseEra.OLD_GARDEN),
        RoseEntry("Variegata di Bologna", "Rosa 'Variegata di Bologna'", RoseType.BOURBON, RoseEra.OLD_GARDEN),

        // --- Hybrid Perpetual (old garden) ---
        RoseEntry("Reine des Violettes", "Rosa 'Reine des Violettes'", RoseType.HYBRID_PERPETUAL, RoseEra.OLD_GARDEN),
        RoseEntry("Baronne Prévost", "Rosa 'Baronne Prévost'", RoseType.HYBRID_PERPETUAL, RoseEra.OLD_GARDEN),
        RoseEntry("Frau Karl Druschki", "Rosa 'Frau Karl Druschki'", RoseType.HYBRID_PERPETUAL, RoseEra.OLD_GARDEN),
        RoseEntry("Paul Neyron", "Rosa 'Paul Neyron'", RoseType.HYBRID_PERPETUAL, RoseEra.OLD_GARDEN),
        RoseEntry("Général Jacqueminot", "Rosa 'Général Jacqueminot'", RoseType.HYBRID_PERPETUAL, RoseEra.OLD_GARDEN),
        RoseEntry("Mrs John Laing", "Rosa 'Mrs John Laing'", RoseType.HYBRID_PERPETUAL, RoseEra.OLD_GARDEN),

        // --- Hybrid Tea (modern) ---
        RoseEntry("Madame Caroline Testout", "Rosa 'Madame Caroline Testout'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Crimson Glory", "Rosa 'Crimson Glory'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Chrysler Imperial", "Rosa 'Chrysler Imperial'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Super Star", "Rosa 'Super Star'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Papa Meilland", "Rosa 'Papa Meilland'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Ingrid Bergman", "Rosa 'Ingrid Bergman'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Pascali", "Rosa 'Pascali'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Elina", "Rosa 'Elina'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Alec's Red", "Rosa 'Alec's Red'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Royal William", "Rosa 'Royal William'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Savoy Hotel", "Rosa 'Savoy Hotel'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Dainty Bess", "Rosa 'Dainty Bess'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Whisky Mac", "Rosa 'Whisky Mac'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Grandpa Dickson", "Rosa 'Grandpa Dickson'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Barkarole", "Rosa 'Barkarole'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Black Baccara", "Rosa 'Black Baccara'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Garden Party", "Rosa 'Garden Party'", RoseType.HYBRID_TEA, RoseEra.MODERN),
        RoseEntry("Sutter's Gold", "Rosa 'Sutter's Gold'", RoseType.HYBRID_TEA, RoseEra.MODERN),

        // --- Floribunda & Grandiflora (modern) ---
        RoseEntry("Margaret Merril", "Rosa 'Margaret Merril'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Sunsprite", "Rosa 'Sunsprite'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Amber Queen", "Rosa 'Amber Queen'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Arthur Bell", "Rosa 'Arthur Bell'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Trumpeter", "Rosa 'Trumpeter'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Lilli Marleen", "Rosa 'Lilli Marleen'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Europeana", "Rosa 'Europeana'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Hot Chocolate", "Rosa 'Hot Chocolate'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Masquerade", "Rosa 'Masquerade'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Gruss an Aachen", "Rosa 'Gruss an Aachen'", RoseType.FLORIBUNDA, RoseEra.MODERN),
        RoseEntry("Gold Medal", "Rosa 'Gold Medal'", RoseType.GRANDIFLORA, RoseEra.MODERN),
        RoseEntry("Tournament of Roses", "Rosa 'Tournament of Roses'", RoseType.GRANDIFLORA, RoseEra.MODERN),

        // --- Polyantha (modern) ---
        RoseEntry("The Fairy", "Rosa 'The Fairy'", RoseType.POLYANTHA, RoseEra.MODERN),
        RoseEntry("Perle d'Or", "Rosa 'Perle d'Or'", RoseType.POLYANTHA, RoseEra.MODERN),
        RoseEntry("Marie Pavié", "Rosa 'Marie Pavié'", RoseType.POLYANTHA, RoseEra.MODERN),
        RoseEntry("White Pet", "Rosa 'White Pet'", RoseType.POLYANTHA, RoseEra.MODERN),

        // --- Climbers (modern) ---
        RoseEntry("Pierre de Ronsard", "Rosa 'Pierre de Ronsard'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Don Juan", "Rosa 'Don Juan'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Dublin Bay", "Rosa 'Dublin Bay'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Altissimo", "Rosa 'Altissimo'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Golden Showers", "Rosa 'Golden Showers'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Handel", "Rosa 'Handel'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Schoolgirl", "Rosa 'Schoolgirl'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Aloha", "Rosa 'Aloha'", RoseType.CLIMBER, RoseEra.MODERN),
        RoseEntry("Maigold", "Rosa 'Maigold'", RoseType.CLIMBER, RoseEra.MODERN),

        // --- Ramblers (modern) ---
        RoseEntry("Dorothy Perkins", "Rosa 'Dorothy Perkins'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("American Pillar", "Rosa 'American Pillar'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Rambling Rector", "Rosa 'Rambling Rector'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Bobbie James", "Rosa 'Bobbie James'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Seagull", "Rosa 'Seagull'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Wedding Day", "Rosa 'Wedding Day'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Albéric Barbier", "Rosa 'Albéric Barbier'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("François Juranville", "Rosa 'François Juranville'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Félicité et Perpétue", "Rosa 'Félicité et Perpétue'", RoseType.RAMBLER, RoseEra.MODERN),
        RoseEntry("Sander's White Rambler", "Rosa 'Sander's White Rambler'", RoseType.RAMBLER, RoseEra.MODERN),

        // --- English / David Austin (modern) ---
        RoseEntry("Abraham Darby", "Rosa 'Abraham Darby'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Golden Celebration", "Rosa 'Golden Celebration'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Mary Rose", "Rosa 'Mary Rose'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Winchester Cathedral", "Rosa 'Winchester Cathedral'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Heritage", "Rosa 'Heritage'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Brother Cadfael", "Rosa 'Brother Cadfael'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("The Pilgrim", "Rosa 'The Pilgrim'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Jude the Obscure", "Rosa 'Jude the Obscure'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Scepter'd Isle", "Rosa 'Scepter'd Isle'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("A Shropshire Lad", "Rosa 'A Shropshire Lad'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Crown Princess Margareta", "Rosa 'Crown Princess Margareta'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Lady Emma Hamilton", "Rosa 'Lady Emma Hamilton'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Teasing Georgia", "Rosa 'Teasing Georgia'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Wollerton Old Hall", "Rosa 'Wollerton Old Hall'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Claire Austin", "Rosa 'Claire Austin'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Olivia Rose Austin", "Rosa 'Olivia Rose Austin'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Roald Dahl", "Rosa 'Roald Dahl'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Boscobel", "Rosa 'Boscobel'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Desdemona", "Rosa 'Desdemona'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Harlow Carr", "Rosa 'Harlow Carr'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("Princess Anne", "Rosa 'Princess Anne'", RoseType.ENGLISH, RoseEra.MODERN),
        RoseEntry("The Poet's Wife", "Rosa 'The Poet's Wife'", RoseType.ENGLISH, RoseEra.MODERN),

        // --- Shrub, Hybrid Musk & Rugosa hybrids (modern) ---
        RoseEntry("Ballerina", "Rosa 'Ballerina'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Penelope", "Rosa 'Penelope'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Felicia", "Rosa 'Felicia'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Buff Beauty", "Rosa 'Buff Beauty'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Cornelia", "Rosa 'Cornelia'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Roseraie de l'Haÿ", "Rosa 'Roseraie de l'Haÿ'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Blanc Double de Coubert", "Rosa 'Blanc Double de Coubert'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Hansa", "Rosa 'Hansa'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Fru Dagmar Hastrup", "Rosa 'Fru Dagmar Hastrup'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Nevada", "Rosa 'Nevada'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Frühlingsgold", "Rosa 'Frühlingsgold'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Frühlingsmorgen", "Rosa 'Frühlingsmorgen'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Golden Wings", "Rosa 'Golden Wings'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Sally Holmes", "Rosa 'Sally Holmes'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Scharlachglut", "Rosa 'Scharlachglut'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Mozart", "Rosa 'Mozart'", RoseType.SHRUB, RoseEra.MODERN),
        RoseEntry("Cerise Bouquet", "Rosa 'Cerise Bouquet'", RoseType.SHRUB, RoseEra.MODERN),

        // --- Groundcover (modern) ---
        RoseEntry("Suffolk", "Rosa 'Suffolk'", RoseType.GROUNDCOVER, RoseEra.MODERN),
        RoseEntry("Kent", "Rosa 'Kent'", RoseType.GROUNDCOVER, RoseEra.MODERN),
        RoseEntry("Surrey", "Rosa 'Surrey'", RoseType.GROUNDCOVER, RoseEra.MODERN),
        RoseEntry("Magic Carpet", "Rosa 'Magic Carpet'", RoseType.GROUNDCOVER, RoseEra.MODERN),
        RoseEntry("Scarlet Meidiland", "Rosa 'Scarlet Meidiland'", RoseType.GROUNDCOVER, RoseEra.MODERN),

        // --- Miniature (modern) ---
        RoseEntry("Starina", "Rosa 'Starina'", RoseType.MINIATURE, RoseEra.MODERN),
        RoseEntry("Baby Masquerade", "Rosa 'Baby Masquerade'", RoseType.MINIATURE, RoseEra.MODERN),
        RoseEntry("Rise 'n' Shine", "Rosa 'Rise n Shine'", RoseType.MINIATURE, RoseEra.MODERN),
        RoseEntry("Green Ice", "Rosa 'Green Ice'", RoseType.MINIATURE, RoseEra.MODERN),
        RoseEntry("Cinderella", "Rosa 'Cinderella'", RoseType.MINIATURE, RoseEra.MODERN),
        RoseEntry("Sweet Chariot", "Rosa 'Sweet Chariot'", RoseType.MINIATURE, RoseEra.MODERN),
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
