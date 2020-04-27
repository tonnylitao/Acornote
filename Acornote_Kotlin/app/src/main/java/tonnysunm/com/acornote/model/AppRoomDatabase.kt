package tonnysunm.com.acornote.model

import android.content.Context
import androidx.room.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataTypeConverter {
    companion object {
        val gson = Gson()
    }

    @TypeConverter
    fun fromString(value: String?): List<String>? {
        val listType: Type = object : TypeToken<List<String?>?>() {}.type
        return if (value != null) gson.fromJson(value, listType) else null
    }

    @TypeConverter
    fun fromList(list: List<String?>?): String? {
        return if (list != null) gson.toJson(list) else null
    }
}

@Database(
    entities = [Label::class, Note::class, NoteLabel::class, ColorTag::class, Image::class],
    version = 1
)
@TypeConverters(DataTypeConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun labelDao(): LabelDao
    abstract fun noteDao(): NoteDao
    abstract fun noteLabelDao(): NoteLabelDao
    abstract fun colorTagDao(): ColorTagDao
    abstract fun imageDao(): ImageDao

    //
    companion object {
        @Volatile
        private var INSTANCE: AppRoomDatabase? = null

//        private val MIGRATION_1_2 = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL(
//                    "ALTER TABLE note_table ADD COLUMN editing INTEGER DEFAULT 0"
//                )
//            }
//        }

        fun getDatabase(context: Context): AppRoomDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }

            synchronized(this) {
                val instance = Room
                    .databaseBuilder(
                        context.applicationContext,
                        AppRoomDatabase::class.java,
                        "acornote_db"
                    )
//                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}