package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.ckbdd_edit_button.*
import ml.varpeti.ton.Ton
import java.io.File

class CKBDDsetbutton : AppCompatActivity()
{

    private lateinit var bs : Ton
    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    private val fileName = "$ex/b.ton"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_button)

        bsave.setOnClickListener { onClick() }

        //TODO
        tcmdnormal.isEnabled = false
        tcmdlong.isEnabled = false
        tsettings.isEnabled = false

        bs = Ton.parsefromFile(fileName) //Buttons

        val id = intent.getStringExtra("id")

        if (!bs.containsKey(id)) return
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

    }

    private fun onClick()
    {

        val id = tid.text.toString()
        bs.remove(id) // Delete if already exist
        bs.put(id) //If we modify the ID the old one will remain
        val b = bs.get(id)

        //SHOW
        b.put("show")
        val show = b.get("show")
        if (tshowprimary.text.toString()!="")
        {
            show.put("primary",tshowprimary.text.toString())
        }
        if (tshowsecondary.text.toString()!="")
        {
            show.put("secondary",tshowsecondary.text.toString())
        }

        //TODO
        /*CMD
        b.put("cmd")
        val cmd = b.get("cmd")
        if (tcmdnormal.text.toString()!="")
        {
           cmd.put("normal",tcmdnormal.text.toString())
        }
        if (tcmdlong.text.toString()!="")
        {
            cmd.put("long",tcmdlong.text.toString())
        }

        //SETTINGS
        b.put("settings")
        val settings = b.get("settings")
        settings.put(tsettings.text.toString())
        */

        //Write out
        File(fileName).bufferedWriter().use{ out ->
            out.write(bs.toString())
        }

        //This will tell the IMS it should reload. The IMS checks every onStartInputView.
        File("$ex/ch").createNewFile()

        finish()
    }
}