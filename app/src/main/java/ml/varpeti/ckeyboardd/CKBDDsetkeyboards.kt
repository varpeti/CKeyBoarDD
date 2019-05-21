package ml.varpeti.ckeyboardd

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_list.*

class CKBDDsetkeyboards : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)

        val ton2view = CKBDDton2view()

        val layouts = HashMap<String,View>()
        ton2view.keyboards(this,layouts) {true}

        val layoutparams = LinearLayout.LayoutParams(-1,-2)
        layoutparams.setMargins(0,ton2view.buttonsSettings.horizontalMargin.get(),0,ton2view.buttonsSettings.horizontalMargin.get())

        for (keyboardID in layouts.keys)
        {
            val keyboardButton = Button(this)
            keyboardButton.text=keyboardID
            keyboardButton.setOnClickListener { onClick(keyboardID) }
            list.addView(keyboardButton)

            val view = layouts[keyboardID]
            view!!.layoutParams = layoutparams
            list.addView(view)
        }

        val keyboardID = "New"
        val keyboardButton = Button(this)
        keyboardButton.text=keyboardID
        keyboardButton.setOnClickListener { onClick("") }
        list.addView(keyboardButton)
    }

    private fun onClick(id : String)
    {
        val intent = Intent(this,CKBDDsetkeyboard::class.java)
        intent.putExtra("id",id)
        startActivityForResult(intent,3373)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)
    {
        when (requestCode)
        {
            3373 -> recreate() //If we arrive here after setting
        }
    }
}