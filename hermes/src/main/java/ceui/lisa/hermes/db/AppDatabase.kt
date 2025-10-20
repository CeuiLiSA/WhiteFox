package ceui.lisa.hermes.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [GeneralEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun generalDao(): GeneralDao
}