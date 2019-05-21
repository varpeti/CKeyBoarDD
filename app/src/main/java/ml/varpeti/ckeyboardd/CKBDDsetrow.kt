package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import kotlinx.android.synthetic.main.ckbdd_edit_list.*
import ml.varpeti.ton.Ton
import java.io.File

class CKBDDsetrow : AppCompatActivity()
{
    private lateinit var rs : Ton
    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    private val fileName = "$ex/r.ton"


    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_list)

        bnew.setOnClickListener { addNew() }
        bsave.setOnClickListener { save() }
        llist.text = getString(R.string.buttons)

        rs = Ton.parsefromFile(fileName) //Rows

        val id = intent.getStringExtra("id")

        if (!rs.containsKey(id)) return
        val r = rs.get(id)

        //ID
        tid.setText(id)

        for (bid in r.get("buttons").keyArrayList)
        {
            val button = EditText(this)
            button.setText(bid)
            button.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
            buttons.addView(button)
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
        val id = tid.text.toString()
        rs.remove(id) // Delete if already exist
        rs.put(id) //If we modify the ID the old one will remain
        val r = rs.get(id)
        r.put("buttons")
        val rbuttons = r.get("buttons")

        var n = 0
        for (i in 0 until buttons.childCount)
        {
            val button = buttons.getChildAt(i) as EditText
            val bid = button.text.toString()
            if (bid == "") continue // The empty is deleted
            rbuttons.put("$n",bid)
            n++
        }

        /*TODO settings
        //SETTINGS
        b.put("settings")
        val settings = b.get("settings")
        settings.put(tsettings.text.toString())
        */

        //Write out
        File(fileName).bufferedWriter().use{ out ->
            out.write(rs.toString())
        }

        //This will tell the IMS it should reload. The IMS checks every onStartInputView.
        File("$ex/ch").createNewFile()

        finish()

    }



}