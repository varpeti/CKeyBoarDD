package ml.varpeti.ckeyboardd

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import kotlinx.android.synthetic.main.ckbdd_settings_main.*
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

    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"

    private fun start()
    {
        val dir = File(ex)

        // If smt is missing it will (re)create
        if (!dir.exists())
        {
            dir.mkdir()
        }
        if (!File("$ex/b.ton").exists())
        {
            copyResources(R.raw.b,"b.ton")
        }
        if (!File("$ex/r.ton").exists())
        {
            copyResources(R.raw.r,"r.ton")
        }
        if (!File("$ex/k.ton").exists())
        {
            copyResources(R.raw.k,"k.ton")
        }

        reset_default_settings.setOnLongClickListener { resetDefaultSettings() }
    }

    fun onClick(v : View)
    {
        when (v.id) //TODO
        {
            R.id.settings_keyboards -> startActivity(Intent(this,CKBDDsetkeyboards::class.java))
            R.id.settings_rows -> startActivity(Intent(this,CKBDDsetrows::class.java))
            R.id.settings_keys -> startActivity(Intent(this,CKBDDsetbuttons::class.java))
            R.id.reset_default_settings ->
            {
                Toast.makeText(this, "The keyboard is reloaded. Press long if you want to reset.", Toast.LENGTH_LONG).show()
                reload()
            }
            R.id.enable_input_method -> startActivity(Intent(Settings.ACTION_INPUT_METHOD_SETTINGS))
            R.id.set_input_method ->
            {
                val ims = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                ims.showInputMethodPicker()
            }
        }
    }

    private fun resetDefaultSettings() : Boolean
    {
        Toast.makeText(this, "The keyboard is reset.", Toast.LENGTH_LONG).show()
        copyResources(R.raw.b,"b.ton")
        copyResources(R.raw.r,"r.ton")
        copyResources(R.raw.k,"k.ton")
        reload()
        return true
    }

    private fun reload()
    {
        // We hide the softkeyboard cos it has to reload itshelf.
        val imm = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(settings_main.windowToken, 0)

        // This will tell the IMS it should reload. The IMS checks every onStartInputView.
        File("$ex/ch").createNewFile()
    }

    private fun copyResources(resId : Int, filename : String)
    {
        val ins = resources.openRawResource(resId)

        val f = File(filename)

        if (!f.exists())
        {
            try
            {
                val out = FileOutputStream(File(ex, filename))
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
                Log.e("|||", e.message)
            } catch (e: IOException) {
                Log.e("|||", e.message)
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

