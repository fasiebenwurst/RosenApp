package de.empirius.rosenapp.data

import androidx.room.TypeConverter

/** Room converters for the classification enums; stored as their enum [Enum.name]. */
class Converters {

    @TypeConverter
    fun roseTypeToString(value: RoseType?): String? = value?.name

    @TypeConverter
    fun stringToRoseType(value: String?): RoseType? =
        value?.let { runCatching { RoseType.valueOf(it) }.getOrNull() }

    @TypeConverter
    fun roseEraToString(value: RoseEra?): String? = value?.name

    @TypeConverter
    fun stringToRoseEra(value: String?): RoseEra? =
        value?.let { runCatching { RoseEra.valueOf(it) }.getOrNull() }
}
