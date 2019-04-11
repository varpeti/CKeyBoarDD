package ml.varpeti.ckeyboardd

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.Environment
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import ml.varpeti.ton.Ton


class CKBDDservice : InputMethodService()
{

    override fun onCreateInputView(): View
    {
        return layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{

            val ex = Environment.getExternalStorageDirectory()
            val keyboard = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/b.ton")

            for (k in keyboard.keySet())
            {
                val b = TextView(this@CKBDDservice) //TODO custom view
                b.text=keyboard.get(k).get("show").get("primary").first()
                b.setTextColor(Color.parseColor("#ffffff"))
                b.setBackgroundColor(Color.parseColor("#000000"))
                b.setOnClickListener{onClick(keyboard.get(k).get("cmd").get("normal"))}
                b.setOnLongClickListener{onClick(keyboard.get(k).get("cmd").get("long"))}
                b.textSize=28F
                b.width=32
                row0.addView(b)
            }

            //TODO SwitchingToNextInputMethod button
        }
    }

    fun onClick(cmd : Ton) : Boolean
    {
        if (currentInputConnection == null) return false

        for (c in cmd.keySet())
        {
            when (cmd.get(c).first())
            {
                "print" -> currentInputConnection.commitText(cmd.get(c).get("print").first(),1)
            }
        }

        return true
    }

}