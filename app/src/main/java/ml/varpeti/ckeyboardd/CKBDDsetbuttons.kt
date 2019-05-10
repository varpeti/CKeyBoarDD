package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_list.*
import ml.varpeti.ton.Ton
import java.lang.Math.round

class CKBDDsetbuttons : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)


        val ton2view = CKBDDton2view()

        val arrayList = ArrayList(ton2view.bs.keySet())
        ton2view.buttons(this,arrayList,list,::onClick)

        val layoutparams = LinearLayout.LayoutParams(round(ton2view.buttonsSettings.height.get()*ton2view.buttonsSettings.width.get()),ton2view.buttonsSettings.height.get())
        layoutparams.setMargins(0,ton2view.buttonsSettings.horizontalMargin.get(),0,ton2view.buttonsSettings.horizontalMargin.get())

        for (i in 0 until list.childCount)
        {
            list.getChildAt(i).layoutParams=layoutparams
        }
    }

    private fun onClick(cmd : Ton) : Boolean
    {
        Log.i("|||","$cmd")
        return true
    }
}