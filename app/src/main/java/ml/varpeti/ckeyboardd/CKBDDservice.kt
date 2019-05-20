package ml.varpeti.ckeyboardd

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.inputmethodservice.InputMethodService
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.KeyEvent.ACTION_DOWN
import android.view.KeyEvent.ACTION_UP
import android.view.View
import android.view.inputmethod.EditorInfo
import ml.varpeti.ton.Ton
import java.io.File
import java.text.ParseException


class CKBDDservice : InputMethodService()
{
    private val layouts = HashMap<String,View>()
    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"

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

    override fun onStartInputView(info: EditorInfo?, restarting: Boolean)
    {
        super.onStartInputView(info, restarting)

        // If "ch" file is exist, (some changes had happened) delete "ch" and reload everything
        val ch = File("$ex/ch")
        if (ch.exists())
        {
            ch.delete()
            setInputView(onCreateInputView())
        }
    }

    override fun onCreateInputView(): View
    {
        if (!checkPermission())
        {
            Log.e("|||","Missing external storage permission!")

            //In case of missing external storage permission, open the settings, it'll ask 4 it
            val intent = Intent(this,CKBDDsettings::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

            return View(this)
        }

        val ton2view = CKBDDton2view()
        ton2view.keyboards(this,layouts,::onClick)

        if (!layouts.containsKey("main")) return layouts.values.first()

        return layouts["main"]!!
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
                        if (keyCode in 32..126)
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
                        if (keyCode in 32..126)
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
                    val intent = Intent(this,CKBDDsettings::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                "voice" ->
                {
                    /* TODO
                    private static final int RECOGNIZER_REQ_CODE = 1234;

                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    startActivityForResult(intent, RECOGNIZER_REQ_CODE);
                    */
                }
            }
        }

        return true
    }

}