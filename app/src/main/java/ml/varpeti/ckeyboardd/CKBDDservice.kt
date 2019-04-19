package ml.varpeti.ckeyboardd

import android.content.res.Resources
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.Environment
import android.util.Log
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.KeyEvent.*
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import ml.varpeti.ton.Ton
import java.lang.Exception
import java.text.ParseException


class CKBDDservice : InputMethodService()
{
    private val layouts = HashMap<String,View>()

    override fun onCreateInputView(): View
    {
        val ex = Environment.getExternalStorageDirectory()
        val bs = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/b.ton") //Buttons
        val rs = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/r.ton") //Rows
        val ks = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/k.ton") //Keyboards

        for (k in ks.keySet()) // Keyboards
        {
            val layout = layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
                val keyboard = keyboard
                rows(rs,bs,ks.get(k),keyboard)
            }
            layouts[k] = layout
        }

        if (!layouts.containsKey("main")) throw Exception("The 'main' keyboard not found")

        return layouts["main"]!!
    }


    private fun rows(rs : Ton, bs : Ton, k : Ton, keyboard: LinearLayout)
    {
        for (ki in k.values())
        {
            //Log.i("|||", "${ki.first()} ${rs.containsKey(ki.first())}")
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
                var buttonSize = 1F

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
                    if (show.containsKey("size")  && !show.get("size").isEmpty)
                    {
                        buttonSize = (show.get("size").first()).toFloat()
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
                val size = ( Resources.getSystem().displayMetrics.widthPixels/buttonsInOneRow - (verticalMargin*2) )
                val layoutparams = LinearLayout.LayoutParams(Math.round(size * buttonSize), Math.round(size * 1.2F))

                //Margin
                layoutparams.setMargins(verticalMargin, horizontalMargin, verticalMargin, horizontalMargin)

                key.layoutParams = layoutparams
            }

            row.addView(key)
        }
    }

    /*
    //NEVER EVER USE THIS METHOD!!!
    private fun getCursorPosition(): Int
    {
        val extracted = currentInputConnection.getExtractedText(ExtractedTextRequest(), 0) ?: return 0
        return extracted.startOffset + extracted.selectionStart
    }
    */

    private var ctrl = false

    private fun onClick(cmd : Ton) : Boolean
    {
        if (currentInputConnection == null) return false

        for (c in cmd.values())
        {
            if (c.isEmpty) continue
            when (c.first())
            {
                "print" ->
                {
                    /*
                    // Just print the charaters
                    currentInputConnection.commitText(c.get("print").first(), 1)
                    */

                    /*
                    // It wont work other input fields but android.
                    currentInputConnection.sendKeyEvent(KeyEvent(100,c.get("print").first(), -1,FLAG_SOFT_KEYBOARD))
                    */

                    val charArray = c.get("print").first().toCharArray()

                    // If ctrl is toggled, only 1 char is coming, and it can be turned into a control char then do it.
                    if (ctrl && charArray.size==1)
                    {
                        var keyCode = charArray[0].toInt()
                        if (keyCode>= 32 && keyCode < 127)
                        {
                            // https://en.wikipedia.org/wiki/Control_character#How_control_characters_map_to_keyboards
                            currentInputConnection.commitText((keyCode and 31).toChar().toString(), 1)
                        }
                    }
                    else
                    {
                        //
                        val charMap = KeyCharacterMap.load(KeyCharacterMap.FULL)
                        val events = charMap.getEvents(charArray)

                        if (events != null && events.size==(charArray.size*2) ) // If every char can be converted into an keyEvent
                        {
                            for (i in events.indices)
                            {
                                currentInputConnection.sendKeyEvent(events[i]) //send keyEvent(s)
                            }
                        }
                        else // otherwise just print out
                        {
                            currentInputConnection.commitText(c.get("print").first(), 1)
                        }
                    }
                    ctrl=false
                }
                "keycode" ->
                {
                    if (!c.get("keycode").isEmpty)
                    {
                        try
                        {
                            val key = Integer.parseInt(c.get("keycode").first())
                            currentInputConnection.sendKeyEvent(KeyEvent(ACTION_DOWN, key))
                            currentInputConnection.sendKeyEvent(KeyEvent(ACTION_UP, key))
                        }
                        catch (ex : ParseException)
                        {
                            Log.e("|||","Can't find key with this number: (${c.get("keycode").first()})")
                        }
                    }
                }
                "ctrl" ->
                {
                    ctrl=!ctrl
                }
                "switch" ->
                {
                    if (!c.get("switch").isEmpty)
                    {
                        val kb = c.get("switch").first()
                        if (layouts.containsKey(kb))
                        {
                            setInputView(layouts[kb])
                            Log.i("|||","$kb")
                        }
                        else
                        {
                            Log.e("|||","The $kb keyboard not found!")
                        }

                    }
                }
                "settings" ->
                {
                    //TODO
                }
                "voice" ->
                {
                    //TODO
                }
            }
        }

        return true
    }

}