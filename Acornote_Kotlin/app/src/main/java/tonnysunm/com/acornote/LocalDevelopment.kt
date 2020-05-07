package tonnysunm.com.acornote

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tonnysunm.com.acornote.model.Label
import tonnysunm.com.acornote.model.Note
import tonnysunm.com.acornote.model.NoteLabelCrossRef
import tonnysunm.com.acornote.model.Repository
import java.io.*
import java.lang.reflect.Type

//for ADB WIFI
//~/Library/Android/sdk/platform-tools/adb devices -l
//~/Library/Android/sdk/platform-tools/adb -s 99171FFAZ00B73 tcpip 5555
//~/Library/Android/sdk/platform-tools/adb connect 192.168.20.5:5555

object BackupFile {
    private fun exportDataToJSON(context: Context) {
        val repository = Repository(context)
        GlobalScope.launch {
            val allNotes = repository.noteDao.getAll()
            val allLabels = repository.labelDao.getAll()
            val allNoteLabels = repository.noteLabelDao.getAll()


            val string = Gson().toJson(
                mapOf(
                    "notes" to Gson().toJson(allNotes),
                    "labels" to Gson().toJson(allLabels),
                    "noteLabels" to Gson().toJson(allNoteLabels)
                )
            )

            BackupFile.writeStringAsFile(
                context = context,
                fileContents = string,
                fileName = "backup"
            )
        }
    }

    private fun importDataFromJSON(context: Context) {
        val string = BackupFile.readFileAsString(context = context, fileName = "backup")

        val repository = Repository(context)
        GlobalScope.launch {
            val listType: Type = object : TypeToken<Map<String, String>?>() {}.type
            val obj = Gson().fromJson(string, listType) as? Map<String, String>

            (Gson().fromJson(
                obj?.get("notes"),
                object : TypeToken<List<Note>?>() {}.type
            ) as? List<Note>)?.let {
                repository.noteDao.insert(it)
            }

            (Gson().fromJson(
                obj?.get("labels"),
                object : TypeToken<List<Label>?>() {}.type
            ) as? List<Label>)?.let {
                repository.labelDao.insert(it)
            }

            (Gson().fromJson(
                obj?.get("noteLabels"),
                object : TypeToken<List<NoteLabelCrossRef>?>() {}.type
            ) as? List<NoteLabelCrossRef>)?.let {
                repository.noteLabelDao.insert(it)
            }
        }
    }

    fun d(TAG: String?, message: String) {
        val maxLogSize = 2000
        for (i in 0..message.length / maxLogSize) {
            val start = i * maxLogSize
            var end = (i + 1) * maxLogSize
            end = if (end > message.length) message.length else end
            Log.d(TAG, message.substring(start, end))
        }
    }

    fun writeStringAsFile(
        context: Context,
        fileContents: String,
        fileName: String?
    ) {
        try {
            val out = FileWriter(File(context.getFilesDir(), fileName))
            out.write(fileContents)
            out.close()
        } catch (e: IOException) {

        }
    }

    fun readFileAsString(context: Context, fileName: String): String? {
        val stringBuilder = StringBuilder()
        var line: String? = null
        var `in`: BufferedReader? = null
        try {
            `in` = BufferedReader(FileReader(File(context.getFilesDir(), fileName)))
            while (`in`.readLine().also({ line = it }) != null) stringBuilder.append(line)
        } catch (e: FileNotFoundException) {

        } catch (e: IOException) {

        }
        return stringBuilder.toString()
    }

//    private fun showOverlayIfNecessary() {
//        val prefs = PreferenceManager.getDefaultSharedPreferences(this)
//        val overlayEnabled = prefs.getBoolean("settings_overlay", false)
//
//        if (overlayEnabled && Settings.canDrawOverlays(this)) {
//            startService(Intent(this, BubbleService::class.java))
//        }
//    }

    fun initialImage() {
        //        viewModelScope.launch {
//            Log.d("TAG", "find all images")
//
//            repository.imageDao.insert(
//                Image(
//                    url = "https://www.newzealand.com/assets/Tourism-NZ/Fiordland/img-1536137761-110-7749-p-7ECF7092-95BD-FE18-6D4107E2E42D067E-2544003__aWxvdmVrZWxseQo_FocalPointCropWzQyNyw2NDAsNTAsNTAsODUsImpwZyIsNjUsMi41XQ.jpg",
//                    noteId = 1
//                )
//            )
//        }
    }
}