package ml.varpeti.ckeyboardd

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.inputmethodservice.InputMethodService
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import ml.varpeti.ton.Ton
import java.text.ParseException


class CKBDDservice : InputMethodService()
{
    private val layouts = HashMap<String,View>()

    //Global Sizes
    private var buttonsHeight = 100
    private var buttonsHorizontalMargin = 2
    private var buttonsVerticalMargin = 2
    private var buttonsPrimaryTextSize = 25F
    private var buttonsSecondaryTextSize = 18F
    //TODO Global colors

    private fun checkPermission() : Boolean
    {
        //Jogok meglétének ellenőrzése
        return when
        {
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> false
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED -> false
            else -> true
        }

    }

    override fun onCreateInputView(): View
    {
        if (!checkPermission())
        {
            Log.e("|||","Missing external storage permission!") //TODO
            return View(this)
        }

        //TODO Ton files error handling

        val ex = Environment.getExternalStorageDirectory()
        val bs = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/b.ton") //Buttons
        val rs = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/r.ton") //Rows
        val ks = Ton.parsefromFile("${ex.absolutePath}/CKeyBoarDD/k.ton") //Keyboards

        for (kkey in ks.keySet()) // Keyboards
        {
            if (ks.get(kkey).containsKey("settings"))
            {
                val settings = ks.get(kkey).get("settings")
                for (key in settings.keySet())
                {
                    if (!settings.get(key).isEmpty) when (key)
                    {
                        "buttonsHeight" -> buttonsHeight = settings.get(key).first().toInt()
                        "buttonsHorizontalMargin" -> buttonsHorizontalMargin = settings.get(key).first().toInt()
                        "buttonsVerticalMargin" -> buttonsVerticalMargin = settings.get(key).first().toInt()
                        "buttonsPrimaryTextSize" -> buttonsPrimaryTextSize = settings.get(key).first().toFloat()
                        "buttonsSecondaryTextSize" -> buttonsSecondaryTextSize = settings.get(key).first().toFloat()
                    }
                }
            }

            if (ks.get(kkey).containsKey("rows"))
            {
                val layout = layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
                    val keyboard = keyboard
                    rows(rs,bs,ks.get(kkey).get("rows").keyArrayList,keyboard)
                }
                layouts[kkey] = layout
            }
        }

        if (!layouts.containsKey("main")) throw Exception("The 'main' keyboard not found")

        return layouts["main"]!!
    }

    private fun rows(rs : Ton, bs : Ton, rowkeys : ArrayList<String>, keyboard: LinearLayout)
    {
        for (rkey in rowkeys)
        {
            val rowLinearLayout = LinearLayout(this@CKBDDservice)
            rowLinearLayout.orientation = HORIZONTAL

            val row = rs.get(rkey)

            buttons(bs,row.keyArrayList,rowLinearLayout)

            rowLinearLayout.setBackgroundColor(Color.parseColor("#404040"))
            keyboard.addView(rowLinearLayout)
        }
    }


    private fun buttons(bs : Ton, buttonskeys : ArrayList<String>, rowLinearLayout : LinearLayout)
    {
        for (bkey in buttonskeys) if (bs.containsKey(bkey))
        {
            val b = bs.get(bkey) //button

            val key = layoutInflater.inflate(R.layout.ckbdd_key, null).apply {
                var buttonSize = 1F

                //Show
                if (b.containsKey("show"))
                {
                    val show = b.get("show")
                    if (show.containsKey("primary") && !show.get("primary").isEmpty)
                    {
                        primary.text = b.get("show").get("primary").first()
                        primary.textSize=buttonsPrimaryTextSize
                    }
                    if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
                    {
                        secondary.text = b.get("show").get("secondary").first()
                        secondary.textSize=buttonsSecondaryTextSize
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
                val layoutparams = LinearLayout.LayoutParams(0,buttonsHeight)
                layoutparams.weight=buttonSize

                //Margin
                layoutparams.setMargins(buttonsVerticalMargin, buttonsHorizontalMargin, buttonsVerticalMargin, buttonsHorizontalMargin)

                key.layoutParams = layoutparams
            }

            rowLinearLayout.addView(key)
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
                "print" -> //Print out the characters.
                {
                    val charArray = c.get("print").first().toCharArray()
                    if (ctrl && charArray.size==1) // if only one char and ctrl is down
                    {
                        val keyCode = charArray[0].toInt()
                        if (keyCode>= 32 && keyCode < 127)
                        {
                            // https://en.wikipedia.org/wiki/Control_character#How_control_characters_map_to_keyboards
                            currentInputConnection.commitText((keyCode and 31).toChar().toString(), 1)
                        }
                    }
                    else
                    {
                        currentInputConnection.commitText(c.get("print").first(), 1)
                    }
                    ctrl=false
                }
                "hit" -> //Trying to hit the characters as keyEvents
                {

                    /*
                    // It wont work other input fields but android.
                    currentInputConnection.sendKeyEvent(KeyEvent(100,c.get("print").first(), -1,FLAG_SOFT_KEYBOARD))
                    */

                    val charArray = c.get("print").first().toCharArray()

                    // If ctrl is toggled, only 1 char is coming, and it can be turned into a control char then do it.
                    if (ctrl && charArray.size==1)
                    {
                        val keyCode = charArray[0].toInt()
                        if (keyCode>= 32 && keyCode < 127)
                        {
                            // https://en.wikipedia.org/wiki/Control_character#How_control_characters_map_to_keyboards
                            currentInputConnection.commitText((keyCode and 31).toChar().toString(), 1)
                        }
                    }
                    else
                    {
                        // Map the characters with KeyCharacterMap
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
                "keycode" -> //This'll hit the keycode KeyEvent
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
                "delete" ->
                {
                    if (!c.get("delete").isEmpty)
                    {
                        when (c.get("delete").first())
                        {
                            "word" ->
                            {
                                // it will delete a word the cursor currently in
                                val btext = currentInputConnection.getTextBeforeCursor(30,0) // max 30 char before
                                val atext = currentInputConnection.getTextAfterCursor(30,0) // and after
                                var bi = 0
                                var ai = 0
                                btext.findLast{ ch -> bi++; ch == ' ' || ch == '\n'}
                                atext.find    { ch -> ai++; ch == ' ' || ch == '\n'}
                                currentInputConnection.deleteSurroundingText(bi,ai)
                            }
                            "all" -> currentInputConnection.deleteSurroundingText(Int.MAX_VALUE,Int.MAX_VALUE)
                            else -> Log.e("|||","Cannot delete (${c.get("delete").first()})")
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