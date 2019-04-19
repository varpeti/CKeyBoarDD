package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException


class CKBDDsettings : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_settings_main)

        //Kiírom a default keyboardot az SD kártyára
        copyResources(R.raw.b,"b.ton")
        copyResources(R.raw.r,"r.ton")
        copyResources(R.raw.k,"k.ton")
    }

    val ex = Environment.getExternalStorageDirectory()

    fun copyResources(resId : Int, filename : String)
    {
        Log.i("|||", "$resId")
        val ins = resources.openRawResource(resId)

        val f = File(filename)

        if (!f.exists()) {
            try {
                val out = FileOutputStream(File("${ex.absolutePath}/CKeyBoarDD", filename))
                val buffer = ByteArray(1024)
                var len: Int
                while (true)
                {
                    len = ins.read(buffer, 0, buffer.size)
                    if (len == -1) break
                    out.write(buffer, 0, len)
                }
                ins.close()
                out.close()
            } catch (e: FileNotFoundException) {
                Log.i("|||", e.message)
            } catch (e: IOException) {
                Log.i("|||", e.message)
            }

        }
    }
}

