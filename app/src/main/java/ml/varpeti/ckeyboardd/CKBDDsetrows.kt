package ml.varpeti.ckeyboardd

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import kotlinx.android.synthetic.main.ckbdd_list.*

class CKBDDsetrows : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)

        val ton2view = CKBDDton2view()

        val arrayList = ArrayList(ton2view.rs.keySet())
        ton2view.rows(this,arrayList,list) {true}

        val max = list.childCount

        for (i in 0 until max)
        {
            val rowID = arrayList[i]
            val rowButton = Button(this)
            rowButton.text=rowID
            rowButton.setOnClickListener { onClick(rowID) }
            list.addView(rowButton,i*2)
        }
    }

    private fun onClick(id : String)
    {
        val intent = Intent(this,CKBDDsetrow::class.java)
        intent.putExtra("id",id)
        startActivity(intent)
    }

}