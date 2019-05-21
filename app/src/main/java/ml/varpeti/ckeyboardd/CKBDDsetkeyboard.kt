package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import kotlinx.android.synthetic.main.ckbdd_edit_list.*
import ml.varpeti.ton.Ton
import java.io.File

class CKBDDsetkeyboard : AppCompatActivity()
{
    private lateinit var ks : Ton
    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    private val fileName = "$ex/k.ton"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_list)

        bnew.setOnClickListener { addNew() }
        bsave.setOnClickListener { save() }
        llist.text = getString(R.string.rows)

        ks = Ton.parsefromFile(fileName) //Keyboards

        val id = intent.getStringExtra("id")

        if (!ks.containsKey(id)) return
        val k = ks.get(id)

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
        val id = tid.text.toString()
        ks.remove(id) // Delete if already exist
        ks.put(id) //If we modify the ID the old one will remain
        val k = ks.get(id)
        k.put("rows")
        val rows = k.get("rows")

        var n = 0
        for (i in 0 until buttons.childCount)
        {
            val button = buttons.getChildAt(i) as EditText
            val bid = button.text.toString()
            if (bid == "") continue // The empty is deleted
            rows.put("$n",bid)
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
            out.write(ks.toString())
        }

        //This will tell the IMS it should reload. The IMS checks every onStartInputView.
        File("$ex/ch").createNewFile()

        finish()
    }



}