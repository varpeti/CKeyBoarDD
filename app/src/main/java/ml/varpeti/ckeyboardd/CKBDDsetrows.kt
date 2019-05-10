package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_list.*
import ml.varpeti.ton.Ton
import java.lang.Math.round

class CKBDDsetrows : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)

        val ton2view = CKBDDton2view()

        val arrayList = ArrayList(ton2view.rs.keySet())
        ton2view.rows(this,arrayList,list,::onClick)
    }

    private fun onClick(cmd : Ton) : Boolean
    {
        Log.i("|||","$cmd")
        return true
    }

}