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
    var layouts = ArrayList<View>()

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
            val layout = layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
                val keyboard = keyboard
                rows(rs,bs,k,keyboard)
            }
            layouts.add(layout)
        }

        return layouts[1] //TODO
    }


    private fun rows(rs : Ton, bs : Ton, k : Ton, keyboard: LinearLayout)
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

                row.setBackgroundColor(Color.parseColor("#404040"))
                keyboard.addView(row)
            }
        }
    }


    private fun buttons(bs : Ton, r : Ton, row : LinearLayout)
    {
        for (r in r.values()) if (!r.isEmpty && bs.containsKey(r.first()))
        {
            val b = bs.get(r.first()) //button

            val key = layoutInflater.inflate(R.layout.ckbdd_key, null).apply {

                // TODO
                val buttonsInOneRow = 10
                val horizontalMargin = 2
                val verticalMargin = 2
                val primaryTextSize = 25F
                val secondaryTextSize = 18F

                //Show
                if (b.containsKey("show"))
                {
                    val show = b.get("show")
                    if (show.containsKey("primary") && !show.get("primary").isEmpty)
                    {
                        primary.text = b.get("show").get("primary").first()
                        primary.textSize=primaryTextSize
                    }
                    if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
                    {
                        secondary.text = b.get("show").get("secondary").first()
                        secondary.textSize=secondaryTextSize
                    }
                }

                //Onclick
                if (b.containsKey("cmd"))
                {
                    val cmd = b.get("cmd")
                    if (cmd.containsKey("normal") && !cmd.get("normal").isEmpty)
                    {
                        key.setOnClickListener { onClick(cmd.get("normal")) }
                    }
                    if (cmd.containsKey("long") && !cmd.get("long").isEmpty)
                    {
                        key.setOnLongClickListener { onClick(cmd.get("long")) }
                    }
                }

                //TODO colors

                //Size
                val size = Resources.getSystem().displayMetrics.widthPixels/buttonsInOneRow - (verticalMargin*2)
                val layoutparams = LinearLayout.LayoutParams(size, Math.round(size * 1.2F))

                //Margin
                layoutparams.setMargins(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin)

                key.layoutParams = layoutparams
            }

            row.addView(key)
        }
    }

    private fun onClick(cmd : Ton) : Boolean
    {
        if (currentInputConnection == null) return false

        for (c in cmd.values())
        {
            if (c.isEmpty) continue
            when (c.first())
            {
                "print" -> currentInputConnection.commitText(c.get("print").first(),1)
            }
        }

        return true
    }

}