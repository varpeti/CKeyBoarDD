package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_keyboard.view.*
import kotlinx.android.synthetic.main.ckbdd_list.*
import ml.varpeti.ton.Ton
import java.lang.Math.round

class CKBDDsetkeyboards : AppCompatActivity()
{

    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    private val buttonsSettings = CKBDDbuttonsSettings()
    private lateinit var ks : Ton
    private lateinit var rs : Ton
    private lateinit var bs : Ton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)
        ks = Ton.parsefromFile("$ex/k.ton") //Keyboards
        rs = Ton.parsefromFile("$ex/r.ton") //Rows
        bs = Ton.parsefromFile("$ex/b.ton") //Buttons
        keyboards()
    }

    fun keyboards()
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
                val layout = layoutInflater.inflate(R.layout.ckbdd_keyboard, null).apply{
                    val keyboard = keyboard
                    rows(ks.get(kkey).get("rows").keyArrayList,keyboard)
                }
                layout.setBackgroundColor(buttonsSettings.secondaryBackgroundColor.get())
                val layoutparams = LinearLayout.LayoutParams(-1,-2)
                layoutparams.setMargins(0, buttonsSettings.horizontalMargin.get(), 0, buttonsSettings.horizontalMargin.get())
                layout.layoutParams = layoutparams
                list.addView(layout)
            }
        }
    }

    fun rows(rowkeys : ArrayList<String>, keyboard: LinearLayout)
    {
        for (rkey in rowkeys)
        {
            val rowLinearLayout = LinearLayout(this)
            rowLinearLayout.orientation = HORIZONTAL

            val row = rs.get(rkey)

            buttonsSettings.reset(LVL_R)
            if (row.containsKey("settings"))
            {
                val settings = row.get("settings")
                buttonsSettings.change(settings,LVL_R)
            }

            if (row.containsKey("buttons"))
            {
                buttons(row.get("buttons").keyArrayList,rowLinearLayout)
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

    fun buttons(buttonskeys : ArrayList<String>, rowLinearLayout : LinearLayout)
    {
        for (bkey in buttonskeys) if (bs.containsKey(bkey))
        {
            val b = bs.get(bkey) //button

            val key = layoutInflater.inflate(R.layout.ckbdd_key, null).apply {

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
                        //key.setOnClickListener { onClick(cmd.get("normal")) }
                    }
                    if (cmd.containsKey("long") && !cmd.get("long").isEmpty)
                    {
                        //key.setOnLongClickListener { onClick(cmd.get("long")) }
                    }
                }

                //Size
                val layoutparams = LinearLayout.LayoutParams(0,-1)
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