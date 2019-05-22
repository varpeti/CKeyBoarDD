package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_edit_button.*
import java.io.File

class CKBDDsetbutton : AppCompatActivity()
{

    private val ton2View = CKBDDton2view()
    private val ss = CKBDDsetsettings()
    private var sView : LinearLayout? = null
    private lateinit var old : String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_button)

        bsave.setOnClickListener { save() }
        bsettings.setOnClickListener { settings() }

        val id = intent.getStringExtra("id")
        old = id

        if (!ton2View.bs.containsKey(id)) return
        val b = ton2View.bs.get(id)

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

        //TODO CMD be more user friendly here /right after the CMD system rework/
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

    }

    private fun save()
    {

        val id = tid.text.toString().trim()
        if (id.isBlank())  // Delete the old if ID is blank
        {
            ton2View.bs.remove(old)
        }
        else // If ID is not empty
        {
            ton2View.bs.remove(id) // Delete if already exist
            ton2View.bs.put(id) // If we modify the ID the old one will remain
            val b = ton2View.bs.get(id)


            //SHOW
            b.put("show")
            val show = b.get("show")
            if (tshowprimary.text.toString().isNotBlank())
            {
                show.put("primary", tshowprimary.text.toString().trim())
            }
            if (tshowsecondary.text.toString().isNotBlank())
            {
                show.put("secondary", tshowsecondary.text.toString().trim())
            }

            //CMD
            b.put("cmd")
            val cmd = b.get("cmd")
            if (tcmdnormal.text.toString().isNotBlank())
            {
                cmd.putParsedfromString("normal",tcmdnormal.text.toString())
            }
            if (tcmdlong.text.toString().isNotBlank())
            {
                cmd.putParsedfromString("long",tcmdlong.text.toString())
            }

            //SETTINGS
            b.put("settings")
            val settings = b.get("settings")
            if (sView==null) sView = ss.show(settings,this)
            ss.save(settings,sView)

        }

        //Write out
        File(ton2View.bton).bufferedWriter().use{ out ->
            out.write(ton2View.bs.toString())
        }

        //This will tell the IMS it should reload. The IMS checks every onStartInputView.
        File("${ton2View.ex}/ch").createNewFile()

        finish()
    }

    private fun settings()
    {
        val id = tid.text.toString().trim()
        ton2View.bs.put(id)
        val b = ton2View.bs.get(id)
        b.put("settings")
        val settings = b.get("settings")
        sView = ss.show(settings,this)
        if (sView!=null)
        {
            ssView.removeView(bsettings)
            ssView.addView(sView)
        }
    }

    /* TODO onSaveInstanceState
    public override fun onSaveInstanceState(savedInstanceState: Bundle)
    {
        super.onSaveInstanceState(savedInstanceState)

    }*/

}