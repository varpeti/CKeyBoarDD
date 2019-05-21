package ml.varpeti.ckeyboardd

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_list.*
import java.lang.Math.round

class CKBDDsetbuttons : AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)


        val ton2view = CKBDDton2view()

        val arrayList = ArrayList(ton2view.bs.keySet())
        ton2view.buttons(this,arrayList,list) {true}

        val layoutparams = LinearLayout.LayoutParams(round(ton2view.buttonsSettings.height.get()*ton2view.buttonsSettings.width.get()),ton2view.buttonsSettings.height.get())
        layoutparams.setMargins(0,ton2view.buttonsSettings.horizontalMargin.get(),0,ton2view.buttonsSettings.horizontalMargin.get())

        val max = list.childCount

        for (i in 0 until max)
        {
            val buttonID = arrayList[i]
            val buttonButton = Button(this)
            buttonButton.text=buttonID
            buttonButton.setOnClickListener { onClick(buttonID) }
            list.addView(buttonButton,i*2)

            list.getChildAt(i*2+1).layoutParams=layoutparams
        }

        val buttonID = "New"
        val buttonButton = Button(this)
        buttonButton.text=buttonID
        buttonButton.setOnClickListener { onClick("") }
        list.addView(buttonButton)
    }

    private fun onClick(id : String)
    {
        val intent = Intent(this,CKBDDsetbutton::class.java)
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