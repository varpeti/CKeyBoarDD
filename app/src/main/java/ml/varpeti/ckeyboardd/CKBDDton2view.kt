package ml.varpeti.ckeyboardd

import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import ml.varpeti.ton.Ton

//TODO more/better Ton files error handling
//TODO Ton files documentation

class CKBDDton2view
{
    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    val buttonsSettings = CKBDDbuttonsSettings()
    var ks : Ton
    var rs : Ton
    var bs : Ton
    // What encapsulation?

    init
    {
        ks = Ton.parsefromFile("$ex/k.ton") //Keyboards
        rs = Ton.parsefromFile("$ex/r.ton") //Rows
        bs = Ton.parsefromFile("$ex/b.ton") //Buttons
    }

    fun keyboards(context : Context, layouts : HashMap<String, View>, onClick : (ton : Ton) -> Boolean)
    {
        for (kkey in ks.keySet()) // Keyboards
        {
            buttonsSettings.reset(LVL_K)
            if (ks.get(kkey).containsKey("settings"))
            {
                val settings = ks.get(kkey).get("settings")
                buttonsSettings.change(settings,LVL_K)
            }

            if (ks.get(kkey).containsKey("rows"))
            {
                val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val layout = mInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
                    val keyboard = keyboard
                    rows(context, ks.get(kkey).get("rows").keyArrayList,keyboard,onClick)
                }
                layout.setBackgroundColor(buttonsSettings.secondaryBackgroundColor.get())
                layouts[kkey] = layout
            }
        }
    }

    fun rows(context : Context, rowkeys : ArrayList<String>, keyboard: LinearLayout, onClick : (ton : Ton) -> Boolean)
    {
        for (rkey in rowkeys)if (rs.containsKey(rkey))
        {
            val row = rs.get(rkey)

            val rowLinearLayout = LinearLayout(context)
            rowLinearLayout.orientation = LinearLayout.HORIZONTAL

            buttonsSettings.reset(LVL_R)
            if (row.containsKey("settings"))
            {
                val settings = row.get("settings")
                buttonsSettings.change(settings,LVL_R)
            }

            if (row.containsKey("buttons"))
            {
                buttons(context, row.get("buttons").keyArrayList,rowLinearLayout,onClick)
            }

            //Size
            val layoutparams = LinearLayout.LayoutParams(-1,buttonsSettings.height.get())

            //Margin
            layoutparams.setMargins(0, buttonsSettings.horizontalMargin.get(), 0, buttonsSettings.horizontalMargin.get())

            //Background color (secondary)
            rowLinearLayout.setBackgroundColor(buttonsSettings.secondaryBackgroundColor.get())

            rowLinearLayout.layoutParams = layoutparams
            keyboard.addView(rowLinearLayout)
        }
    }

    fun buttons(context : Context, buttonskeys : ArrayList<String>, rowLinearLayout : LinearLayout, onClick : (ton : Ton) -> Boolean)
    {
        for (bkey in buttonskeys) if (bs.containsKey(bkey))
        {
            val b = bs.get(bkey) //button

            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val key = mInflater.inflate(R.layout.ckbdd_key, null).apply {

                buttonsSettings.reset(LVL_B)
                if (b.containsKey("settings"))
                {
                    val settings = b.get("settings")
                    buttonsSettings.change(settings,LVL_B)
                }

                //Show
                if (b.containsKey("show"))
                {
                    val show = b.get("show")
                    if (show.containsKey("primary") && !show.get("primary").isEmpty)
                    {
                        primary.text = b.get("show").get("primary").first()
                        primary.textSize=buttonsSettings.primaryTextSize.get()
                    }
                    if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
                    {
                        secondary.text = b.get("show").get("secondary").first()
                        secondary.textSize=buttonsSettings.secondaryTextSize.get()
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

                //Size
                val layoutparams = LinearLayout.LayoutParams(0,buttonsSettings.height.get())
                layoutparams.weight=buttonsSettings.width.get()

                //Margin
                layoutparams.setMargins(buttonsSettings.verticalMargin.get(), 0, buttonsSettings.verticalMargin.get(), 0)

                //Colors
                key.setBackgroundColor(buttonsSettings.primaryBackgroundColor.get())
                key.primary.setTextColor(buttonsSettings.primaryTextColor.get())
                key.secondary.setTextColor(buttonsSettings.secondaryTextColor.get())

                key.layoutParams = layoutparams
            }

            rowLinearLayout.addView(key)
        }
    }
}

