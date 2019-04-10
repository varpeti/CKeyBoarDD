package ml.varpeti.ckeyboardd

import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.view.View
import android.widget.TextView
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*



class CKBDDservice : InputMethodService()
{

    override fun onCreateInputView(): View
    {
        return layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
            for (i in 0..10)
            {
                val b = TextView(this@CKBDDservice) //TODO custom view
                b.id=i
                b.text="$i"
                b.setTextColor(Color.parseColor("#ffffff"))
                b.setOnClickListener{onClick(b)}
                b.setOnLongClickListener{onLongClick()}
                row0.addView(b)
            }

            //TODO SwitchingToNextInputMethod button
        }
    }

    fun onClick(b : TextView)
    {
        if (currentInputConnection != null)
        {
            currentInputConnection.commitText(b.text,1)
        }
    }

    fun onLongClick() : Boolean
    {
        //TODO
        return true
    }

}