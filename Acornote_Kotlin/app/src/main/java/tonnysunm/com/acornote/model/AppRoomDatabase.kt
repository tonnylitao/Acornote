package tonnysunm.com.acornote.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

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

private val TAG = AppRoomDatabase::class.simpleName

@Database(
    entities = [Label::class, Note::class, NoteLabelCrossRef::class, ColorTag::class, Image::class, NoteFts::class],
    version = 1
)
@TypeConverters(DataTypeConverter::class)
abstract class AppRoomDatabase : RoomDatabase() {

    abstract fun labelDao(): LabelDao
    abstract fun noteDao(): NoteDao
    abstract fun noteLabelDao(): NoteLabelCrossRefDao
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
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
//                            db.execSQL("")
                        }

                        override fun onOpen(db: SupportSQLiteDatabase) {
                            super.onOpen(db)

                            val cursor =
                                db.query("SELECT count(*) FROM note_table WHERE editing = 0")

                            cursor.use {

                                if (it.moveToFirst() && it.getInt(0) == 0) {
                                    Log.d(TAG, "insert default data")

                                    db.insert(
                                        "note_table",
                                        SQLiteDatabase.CONFLICT_NONE,
                                        ContentValues().apply {
                                            put("id", 1)
                                            put("title", "Hello Jetpack")
                                            put(
                                                "description",
                                                "Jetpack is a suite of libraries, tools, and guidance to help developers write high-quality apps more easily. These components help you follow best practices, free you from writing boilerplate code, and simplify complex tasks, so you can focus on the code you care about."
                                            )
                                            put("`order`", 1)
                                            put("created_at", Date().time)
                                            put("updated_at", Date().time)
                                            put("editing", 0)
                                        }
                                    )

                                    db.insert(
                                        "image_table",
                                        SQLiteDatabase.CONFLICT_NONE,
                                        ContentValues().apply {
                                            put("note_id", 1)
                                            put(
                                                "url",
                                                "https://www.stripes.com/polopoly_fs/1.565654.1548264011!/image/image.jpg_gen/derivatives/landscape_900/image.jpg"
                                            )
                                        }
                                    )

                                    db.insert(
                                        "image_table",
                                        SQLiteDatabase.CONFLICT_NONE,
                                        ContentValues().apply {
                                            put("note_id", 1)
                                            put(
                                                "url",
                                                "https://www.kindpng.com/picc/m/14-142436_android-jetpack-logo-hd-png-download.png"
                                            )
                                        }
                                    )

                                }
                            }
                        }
                    })
//                    .addMigrations(MIGRATION_1_2)
                    .build()

                INSTANCE = instance

                return instance
            }
        }
    }
}