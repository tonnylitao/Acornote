package tonnysunm.com.acornote.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Folder::class, Item::class], version = 2)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun folderDao(): FolderDao
    abstract fun itemDao(): ItemDao

    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

        fun getDatabase(context: Context): AppRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppRoomDatabase::class.java,
                    "acornote_database"
                ).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}