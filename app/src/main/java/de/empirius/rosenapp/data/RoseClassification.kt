package de.empirius.rosenapp.data

/**
 * Horticultural class of a rose. [displayName] is shown in the UI; the enum
 * [name] is what gets persisted (see [Converters]).
 */
enum class RoseType(val displayName: String) {
    SPECIES("Species (wild)"),
    GALLICA("Gallica"),
    DAMASK("Damask"),
    ALBA("Alba"),
    CENTIFOLIA("Centifolia"),
    MOSS("Moss"),
    CHINA("China"),
    TEA("Tea"),
    BOURBON("Bourbon"),
    NOISETTE("Noisette"),
    PORTLAND("Portland"),
    HYBRID_PERPETUAL("Hybrid Perpetual"),
    HYBRID_TEA("Hybrid Tea"),
    FLORIBUNDA("Floribunda"),
    GRANDIFLORA("Grandiflora"),
    POLYANTHA("Polyantha"),
    CLIMBER("Climber"),
    RAMBLER("Rambler"),
    SHRUB("Shrub"),
    ENGLISH("English (Austin)"),
    MINIATURE("Miniature"),
    GROUNDCOVER("Groundcover"),
    OTHER("Other"),
}

/**
 * Historical era of a rose, following the American Rose Society convention that
 * splits old garden vs modern roses at 1867 (the first Hybrid Tea), with wild
 * species standing apart.
 */
enum class RoseEra(val displayName: String) {
    SPECIES("Wild / species"),
    OLD_GARDEN("Old garden (historical)"),
    MODERN("Modern"),
}
