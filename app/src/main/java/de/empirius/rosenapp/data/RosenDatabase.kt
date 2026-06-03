package de.empirius.rosenapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [Plant::class],
    version = 2,
    exportSchema = false,
)
abstract class RosenDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

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

        fun get(context: Context): RosenDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RosenDatabase::class.java,
                    "rosen.db",
                ).addMigrations(MIGRATION_1_2).build().also { instance = it }
            }
    }
}
