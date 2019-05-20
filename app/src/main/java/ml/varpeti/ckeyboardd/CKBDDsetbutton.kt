package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import kotlinx.android.synthetic.main.ckbdd_edit_button.*
import ml.varpeti.ton.Ton
import java.io.File

class CKBDDsetbutton : AppCompatActivity()
{

    private lateinit var bs : Ton
    private val fileName = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD/b.ton"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_button)

        bs = Ton.parsefromFile(fileName) //Buttons

        val id = intent.getStringExtra("id")
        val b = bs.get(id)

        //ID
        tid.setText(id)

        //SHOW
        if (b.containsKey("show"))
        {
            val show = b.get("show")
            if (show.containsKey("primary") && !show.get("primary").isEmpty)
            {
                tshowprimary.setText(b.get("show").get("primary").first())
            }
            if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
            {
                tshowsecondary.setText(b.get("show").get("secondary").first())
            }
        }

        //TODO
        tcmdnormal.isEnabled = false
        tcmdlong.isEnabled = false
        tsettings.isEnabled = false

        //CMD
        if (b.containsKey("cmd"))
        {
            val cmd = b.get("cmd")
            if (cmd.containsKey("normal") && !cmd.get("normal").isEmpty)
            {
                tcmdnormal.setText(cmd.get("normal").toString())
            }
            if (cmd.containsKey("long") && !cmd.get("long").isEmpty)
            {
                tcmdlong.setText(cmd.get("long").toString())
            }
        }

        //SETTINGS
        if (b.containsKey("settings"))
        {
            val settings = b.get("settings")
            tsettings.setText(settings.toString())
        }

        bsave.setOnClickListener { onClick() }

    }

    private fun onClick()
    {

        //ID
        val b = bs.get(tid.text.toString())

        //SHOW
        b.put("show")
        val show = b.get("show")
        if (show.containsKey("primary") && !show.get("primary").isEmpty)
        {
            show.put("primary",tshowprimary.text.toString())
        }
        if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
        {
            show.put("secondary",tshowprimary.text.toString())
        }

        //TODO
        /*CMD
        b.put("cmd")
        val cmd = b.get("cmd")
        if (cmd.containsKey("normal") && !cmd.get("normal").isEmpty)
        {
           cmd.put("normal",tcmdnormal.text.toString())
        }
        if (cmd.containsKey("long") && !cmd.get("long").isEmpty)
        {
            cmd.put("long",tcmdnormal.text.toString())
        }

        //SETTINGS
        b.put("settings")
        val settings = b.get("settings")
        settings.put(tsettings.text.toString())
        */

        Log.i("|||",b.toString())

        File(fileName).bufferedWriter().use{ out ->
            out.write(bs.toString())
        }
    }
}