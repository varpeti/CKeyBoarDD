package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import kotlinx.android.synthetic.main.ckbdd_edit_row_keyboard.*
import ml.varpeti.ton.Ton

class CKBDDsetkeyboard : AppCompatActivity()
{
    private lateinit var ks : Ton
    private val fileName = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD/k.ton"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_row_keyboard)

        ks = Ton.parsefromFile(fileName) //Keyboards

        val id = intent.getStringExtra("id")
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

        bnew.setOnClickListener { addNew() }
        bsave.setOnClickListener { save() }

    }

    private fun addNew()
    {
        val button = EditText(this)
        button.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_NORMAL
        buttons.addView(button)
    }

    private fun save()
    {
        //TODO
    }



}