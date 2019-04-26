package ml.varpeti.ckeyboardd

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
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

        checkPermission()
    }

    private fun start()
    {
        //TODO a fejlesztés végén csak akkor ha még nem létezik
        //Kiírom a default keyboardot az SD kártyára
        File(ex.absolutePath + "/CKeyBoarDD").mkdir()

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

    private fun checkPermission()
    {
        //Jogok meglétének ellenőrzése, ha nincs kérünk
        when
        {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),0)
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            else -> Thread(Runnable(function = { start() }), "startAsync").start()
        }

    }

    // Ha kértünk jogot:
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        when (requestCode)
        {
            0 ->
            {
                //Ha üres a grantResults akkor nem kaptuk meg
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED))
                {
                    // YEAH van jogunk, recreate() hogy menjen tovább
                    recreate()
                }
                else
                {
                    // Nem kell jog, minek az? Az app se kell:
                    finish()
                }
                return
            }
            // Egyéb requestek
            else ->
            {
                // Ignoráljuk őket
            }
        }
    }
}

