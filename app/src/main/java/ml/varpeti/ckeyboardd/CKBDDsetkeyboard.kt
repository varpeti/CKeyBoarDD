package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_edit_list.*
import java.io.File

class CKBDDsetkeyboard : AppCompatActivity()
{
    private val ton2View = CKBDDton2view()
    private val ss = CKBDDsetsettings()
    private var sView : LinearLayout? = null
    private lateinit var old : String

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_list)

        bnew.setOnClickListener { addNew() }
        bsave.setOnClickListener { save() }
        bsettings.setOnClickListener { settings() }
        llist.text = getString(R.string.rows)

        val id = intent.getStringExtra("id")
        old = id

        if (!ton2View.ks.containsKey(id)) return
        val k = ton2View.ks.get(id)

        //ID
        tid.setText(id)

        for (rid in k.get("rows").keyArrayList)
        {
            val row = EditText(this)
            row.setText(rid)
            row.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            buttons.addView(row)
        }

    }

    private fun addNew()
    {
        val button = EditText(this)
        button.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        buttons.addView(button)
    }

    private fun save()
    {
        val id = tid.text.toString().trim()
        if (id.isBlank())  // Delete the old if ID is blank
        {
            ton2View.ks.remove(old)
        }
        else // If ID is not empty
        {
            ton2View.ks.remove(id) // Delete if already exist
            ton2View.ks.put(id) // If we modify the ID the old one will remain
            val k = ton2View.ks.get(id)
            k.put("rows")
            val rows = k.get("rows")

            var n = 0
            for (i in 0 until buttons.childCount)
            {
                val button = buttons.getChildAt(i) as EditText
                val bid = button.text.toString().trim()
                if (bid == "") continue // The empty is deleted
                rows.put("$n",bid)
                n++
            }

            // Save SETTINGS
            k.put("settings")
            val settings = k.get("settings")
            if (sView==null) sView = ss.show(settings,this)
            ss.save(settings,sView)

        }

        //Write out
        File(ton2View.kton).bufferedWriter().use{ out ->
            out.write(ton2View.ks.toString())
        }

        //This will tell the IMS it should reload. The IMS checks every onStartInputView.
        File("${ton2View.ex}/ch").createNewFile()

        finish()
    }

    private fun settings()
    {
        val id = tid.text.toString().trim()
        ton2View.ks.put(id)
        val k = ton2View.ks.get(id)
        k.put("settings")
        val settings = k.get("settings")
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