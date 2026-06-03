package de.empirius.rosenapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Plant::class],
    version = 1,
    exportSchema = false,
)
abstract class RosenDatabase : RoomDatabase() {

    abstract fun plantDao(): PlantDao

    companion object {
        @Volatile
        private var instance: RosenDatabase? = null

        fun get(context: Context): RosenDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    RosenDatabase::class.java,
                    "rosen.db",
                ).build().also { instance = it }
            }
    }
}
