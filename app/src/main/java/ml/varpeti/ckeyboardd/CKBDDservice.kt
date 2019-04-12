package ml.varpeti.ckeyboardd

import android.content.res.Resources
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import ml.varpeti.ton.Ton


class CKBDDservice : InputMethodService()
{

    override fun onCreateInputView(): View
    {
        val ex = Environment.getExternalStorageDirectory()
        val bs = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/b.ton") //Buttons
        val rs = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/r.ton") //Rows
        val ks = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/k.ton") //Keyboards

        for (k in ks.values()) // Keyboards
        {
            Log.i("|||",k.toString())
            //TODO
            return layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
                val keyboard = keyboard
                rows(rs,bs,k,keyboard)
            }
        }

        return layoutInflater.inflate(R.layout.ckbdd_keyboard, null)
    }


    fun rows(rs : Ton, bs : Ton, k : Ton, keyboard: LinearLayout)
    {
        for (ki in k.values())
        {
            Log.i("|||", "${ki.first()} ${rs.containsKey(ki.first())}")
            if (!ki.isEmpty && rs.containsKey(ki.first()))
            {
                val row = LinearLayout(this@CKBDDservice)
                row.orientation = HORIZONTAL

                val r = rs.get(ki.first())

                buttons(bs,r,row)

                keyboard.addView(row)

            }
        }
    }


    fun buttons(bs : Ton, r : Ton, row : LinearLayout)
    {
        for (r in r.values()) if (!r.isEmpty && bs.containsKey(r.first()))
        {
            val b = bs.get(r.first()) //button

            val key = layoutInflater.inflate(R.layout.ckbdd_key, null).apply {

                //Show
                if (b.containsKey("show"))
                {
                    val show = b.get("show")
                    if (show.containsKey("primary") && !show.get("primary").isEmpty)
                    {
                        primary.text = b.get("show").get("primary").first()
                    }
                    if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
                    {
                        secondary.text = b.get("show").get("secondary").first()
                    }
                }

                //Onclick
                key.setOnClickListener { onClick(b.get("cmd").get("normal")) }
                key.setOnLongClickListener { onClick(b.get("cmd").get("long")) }
                //TODO colors

                //Margin
                val layoutparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                layoutparams.setMargins(2, 2, 2, 2)
                key.layoutParams = layoutparams

                //Size
                val size = Resources.getSystem().displayMetrics.widthPixels / 9 - (10 * 2)
                key.maxWidth = size
                key.minWidth = size
                key.maxHeight = Math.round(size * 1.5F)
                key.minHeight = Math.round(size * 1.5F)
            }

            row.setBackgroundColor(Color.parseColor("#00ff00"))

            row.addView(key)
        }
    }

    fun onClick(cmd : Ton) : Boolean
    {
        if (currentInputConnection == null) return false

        for (c in cmd.values())
        {
            when (c.first())
            {
                "print" -> currentInputConnection.commitText(c.get("print").first(),1)
            }
        }

        return true
    }

}