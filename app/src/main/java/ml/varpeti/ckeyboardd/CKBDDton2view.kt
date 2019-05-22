package ml.varpeti.ckeyboardd

import android.content.Context
import android.os.Environment
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.ckbdd_button.view.*
import ml.varpeti.ton.Ton
import java.lang.Math.round

//TODO more/better Ton files error handling
//TODO Ton files documentation

class CKBDDton2view
{
    // What encapsulation?
    val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    val buttonsSettings = CKBDDbuttonsSettings()
    var ks : Ton
    var rs : Ton
    var bs : Ton
    val kton = "$ex/k.ton"
    val rton = "$ex/r.ton"
    val bton = "$ex/b.ton"

    init
    {
        ks = Ton.parsefromFile(kton) //Keyboards
        rs = Ton.parsefromFile(rton) //Rows
        bs = Ton.parsefromFile(bton) //Buttons
    }

    fun keyboards(context : Context, layouts : HashMap<String, View>, onClick : (cmd : Ton) -> Boolean)
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
                val keyboardLinearLayout = LinearLayout(context)
                keyboardLinearLayout.orientation = LinearLayout.VERTICAL
                rows(context, ks.get(kkey).get("rows").keyArrayList,keyboardLinearLayout,onClick)
                keyboardLinearLayout.setBackgroundColor(buttonsSettings.keyboardBackgroundColor.get())
                layouts[kkey] = keyboardLinearLayout
            }
        }
    }

    fun rows(context : Context, rowkeys : ArrayList<String>, keyboard: LinearLayout, onClick : (cmd : Ton) -> Boolean)
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
            val layoutparams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)

            //Margin
            layoutparams.setMargins(0, buttonsSettings.horizontalMargin.get(), 0, buttonsSettings.horizontalMargin.get())

            //Background color (secondary)
            rowLinearLayout.setBackgroundColor(buttonsSettings.rowBackgroundColor.get())

            rowLinearLayout.layoutParams = layoutparams

            keyboard.addView(rowLinearLayout)
        }
    }

    fun buttons(context : Context, buttonskeys : ArrayList<String>, rowLinearLayout : LinearLayout, onClick : (cmd : Ton) -> Boolean)
    {
        for (bkey in buttonskeys) if (bs.containsKey(bkey))
        {
            val b = bs.get(bkey) //button

            val mInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val key = mInflater.inflate(R.layout.ckbdd_button, null).apply {

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
                        //primary.textSize=buttonsSettings.primaryTextSize.get()
                        primary.setTextSize(TypedValue.COMPLEX_UNIT_SP,buttonsSettings.primaryTextSize.get())
                    }
                    if (show.containsKey("secondary") && !show.get("secondary").isEmpty)
                    {
                        secondary.text = b.get("show").get("secondary").first()
                        //secondary.textSize=buttonsSettings.secondaryTextSize.get()
                        secondary.setTextSize(TypedValue.COMPLEX_UNIT_SP,buttonsSettings.secondaryTextSize.get())
                    }
                }

                //Onclick
                if (b.containsKey("cmd"))
                {
                    val cmd = b.get("cmd")
                    if (cmd.containsKey("normal") && !cmd.get("normal").isEmpty)
                    {
                        key.setOnClickListener { onClick(cmd.get("normal")) }

                        if (cmd.containsKey("long") && !cmd.get("long").isEmpty)
                        {
                            key.setOnLongClickListener { onClick(cmd.get("long")) }
                        }
                        else // If no Long Click is defined then repeat the key
                        {
                            key.setOnTouchListener(CKBDDrepeatListener(buttonsSettings.repeatInitialInterval.get(), buttonsSettings.repeatInterval.get(), { onClick(cmd.get("normal")) } ))
                        }
                    }
                }

                //Size
                val height = round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, buttonsSettings.height.get(), resources.displayMetrics))

                val layoutparams = LinearLayout.LayoutParams(0,height)
                layoutparams.weight=buttonsSettings.width.get()

                //Margin
                layoutparams.setMargins(buttonsSettings.verticalMargin.get(), 0, buttonsSettings.verticalMargin.get(), 0)

                //Colors
                key.setBackgroundColor(buttonsSettings.buttonBackgroundColor.get())
                key.primary.setTextColor(buttonsSettings.primaryTextColor.get())
                key.secondary.setTextColor(buttonsSettings.secondaryTextColor.get())

                key.layoutParams = layoutparams
            }

            rowLinearLayout.addView(key)
        }
    }
}

