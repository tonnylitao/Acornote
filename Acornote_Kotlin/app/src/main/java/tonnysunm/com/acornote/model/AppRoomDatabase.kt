package tonnysunm.com.acornote.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Label::class, Note::class], version = 1)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun labelDao(): LabelDao
    abstract fun noteDao(): NoteDao

    //
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
                    "acornote_db"
                ).build()

                INSTANCE = instance

                return instance
            }
        }
    }
}