package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.text.InputType
import android.widget.EditText
import kotlinx.android.synthetic.main.ckbdd_edit_row_keyboard.*
import ml.varpeti.ton.Ton

class CKBDDsetrow : AppCompatActivity()
{
    private lateinit var rs : Ton
    private val fileName = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD/r.ton"

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_edit_row_keyboard)

        rs = Ton.parsefromFile(fileName) //Rows

        val id = intent.getStringExtra("id")
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