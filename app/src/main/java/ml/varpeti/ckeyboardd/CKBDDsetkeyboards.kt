package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import kotlinx.android.synthetic.main.ckbdd_list.*
import ml.varpeti.ton.Ton
import java.lang.Math.round

class CKBDDsetkeyboards : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)

        val ton2view = CKBDDton2view()

        val layouts = HashMap<String,View>()
        ton2view.keyboards(this,layouts,::onClick)

        val layoutparams = LinearLayout.LayoutParams(-1,-2)
        layoutparams.setMargins(0,ton2view.buttonsSettings.horizontalMargin.get(),0,ton2view.buttonsSettings.horizontalMargin.get())

        for (view in layouts.values)
        {
            view.layoutParams = layoutparams
            list.addView(view)
        }
    }

    private fun onClick(cmd : Ton) : Boolean
    {
        Log.i("|||","$cmd")
        return true
    }
}