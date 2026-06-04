package de.empirius.rosenapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Plant::class, PlantPhoto::class],
    version = 5,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class RosenDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    abstract fun plantPhotoDao(): PlantPhotoDao

    companion object {
        @Volatile
        private var instance: RosenDatabase? = null

        /** Adds the per-plant label accent color. Default matches [DEFAULT_ACCENT_COLOR] (rose). */
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE plants ADD COLUMN accentColor INTEGER NOT NULL DEFAULT -5033638",
                )
            }
        }

        /** Adds the optional botanical (Latin) name column. */
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE plants ADD COLUMN latinName TEXT")
            }
        }

        /** Adds the optional classification columns (horticultural type and era). */
        private val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE plants ADD COLUMN type TEXT")
                db.execSQL("ALTER TABLE plants ADD COLUMN era TEXT")
            }
        }

        /** Adds the plant_photos table (the per-plant photo journal). */
        private val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "CREATE TABLE IF NOT EXISTS `plant_photos` (" +
                        "`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                        "`plantId` INTEGER NOT NULL, " +
                        "`path` TEXT NOT NULL, " +
                        "`takenAtMillis` INTEGER NOT NULL, " +
                        "`note` TEXT, " +
                        "`createdAtMillis` INTEGER NOT NULL)",
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_plant_photos_plantId` " +
                        "ON `plant_photos` (`plantId`)",
                )
            }
        }

        fun get(context: Context): RosenDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RosenDatabase::class.java,
                    "rosen.db",
                ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5).build()
                    .also { instance = it }
            }
    }
}
