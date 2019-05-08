package ml.varpeti.ckeyboardd

import android.os.Bundle
import android.os.Environment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.LinearLayout
import android.widget.LinearLayout.HORIZONTAL
import kotlinx.android.synthetic.main.ckbdd_key.view.*
import kotlinx.android.synthetic.main.ckbdd_list.*
import ml.varpeti.ton.Ton
import java.lang.Math.round

class CKBDDsetbuttons : AppCompatActivity()
{

    private val ex = "${Environment.getExternalStorageDirectory().absolutePath}/CKeyBoarDD"
    private val buttonsSettings = CKBDDbuttonsSettings()
    private lateinit var bs : Ton

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.ckbdd_list)

        bs = Ton.parsefromFile("$ex/b.ton") //Buttons
        //list.orientation=HORIZONTAL
        buttons(bs,list)
    }

    fun buttons(bs : Ton, rowLinearLayout : LinearLayout)
    {
        for (b in bs.values())
        {
            Log.i("|||",b.first())

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
                val layoutparams = LinearLayout.LayoutParams( round(buttonsSettings.height.get()*buttonsSettings.width.get()) ,buttonsSettings.height.get())
                //layoutparams.weight=buttonsSettings.width.get()

                //Margin
                layoutparams.setMargins(buttonsSettings.verticalMargin.get(), buttonsSettings.horizontalMargin.get(), buttonsSettings.verticalMargin.get(), buttonsSettings.horizontalMargin.get())

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