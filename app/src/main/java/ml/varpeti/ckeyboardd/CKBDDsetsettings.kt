package ml.varpeti.ckeyboardd

import android.content.Context
import android.text.InputType
import android.text.method.DigitsKeyListener
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import ml.varpeti.ton.Ton

class CKBDDsetsettings
{

    inner class Options(val option: String, val type: String)

    private val options = arrayOf(
        Options("Height","Int"),
        Options("Width","Float"),
        Options("HorizontalMargin","Int"),
        Options("VerticalMargin","Int"),
        Options("PrimaryTextSize","Float"),
        Options("SecondaryTextSize","Float"),
        Options("PrimaryTextColor","Color"),
        Options("SecondaryTextColor","Color"),
        Options("ButtonBackgroundColor","Color"),
        Options("RowBackgroundColor","Color"),
        Options("KeyboardBackgroundColor","Color"),
        Options("RepeatInitialInterval","Int"),
        Options("RepeatInterval","Int")
    )


    fun show(settings : Ton, context : Context) : LinearLayout?
    {

        val ll = LinearLayout(context)
        ll.orientation = LinearLayout.VERTICAL

        // List every option
        for (o in options)
        {
            val div = LinearLayout(context)
            div.orientation = LinearLayout.HORIZONTAL
            val label = TextView(context)
            label.text=o.option
            val value = EditText(context)

            when (o.type)
            {
                "Int" ->
                {
                    value.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL
                }
                "Float" ->
                {
                    value.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_VARIATION_NORMAL or InputType.TYPE_NUMBER_FLAG_DECIMAL
                }
                "Color" ->
                {
                    value.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_FILTER
                    value.keyListener = DigitsKeyListener.getInstance("#0123456789abcdefABCDEF")
                }
            }

            //If option already has value show it.
            if (settings.containsKey(o.option))
            {
                value.setText(settings.get(o.option).first())
            }

            val layoutparams = LinearLayout.LayoutParams(-1,-2)
            value.layoutParams = layoutparams

            div.addView(label)
            div.addView(value)
            ll.addView(div)
        }

        return ll
    }

    fun save(settings : Ton,ll : LinearLayout?)
    {
        if (ll==null) return

        for (i in 0 until ll.childCount)
        {
            val div = ll.getChildAt(i) as LinearLayout
            val label = div.getChildAt(0) as TextView
            val value = div.getChildAt(1) as EditText
            val id = label.text as String
            val v = value.text.toString()

            settings.remove(id)
            if (v!="")
            {
                settings.put(id,v)
            }
        }
    }
}