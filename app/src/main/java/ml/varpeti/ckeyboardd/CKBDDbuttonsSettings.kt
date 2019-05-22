package ml.varpeti.ckeyboardd

import android.graphics.Color
import android.util.Log
import ml.varpeti.ton.Ton

const val LVL_K = 0
const val LVL_R = 1
const val LVL_B = 2

class CKBDDbuttonsSettings
{
    class Tway<T>(private var def : T, private var k : T?, private var r : T?, private var b : T?)
    {

        constructor (def : T) : this(def,null,null,null) {}

        fun get() : T
        {
            if (b!=null) return b!!
            if (r!=null) return r!!
            if (k!=null) return k!!
            return def
        }

        fun set(v : T, lvl : Int)
        {
            when (lvl)
            {
                LVL_K -> k=v
                LVL_R -> r=v
                LVL_B -> b=v
            }
        }

        fun reset(lvl : Int)
        {
            when (lvl)
            {
                LVL_K -> k=null
                LVL_R -> r=null
                LVL_B -> b=null
            }
        }
    }

    //TODO settings documentation

    // The default values are configured in Samsung Galaxy Note 9, so it will be ugly in other devices. But the user have the option of defining by itself the values.
    var height = Tway(100) // In the ton files it is 125 in all keyboards, and 120 in "*num" rows.
    var width = Tway(1F)
    var horizontalMargin  = Tway(2)
    var verticalMargin = Tway(2)
    var primaryTextSize = Tway(25F)
    var secondaryTextSize = Tway(18F)
    var primaryTextColor = Tway(Color.parseColor("#ffffff"))
    var secondaryTextColor = Tway(Color.parseColor("#b0b0b0"))
    var primaryBackgroundColor = Tway(Color.parseColor("#000000"))
    var secondaryBackgroundColor = Tway(Color.parseColor("#404040"))
    var repeatInitialInterval = Tway(400)
    var repeatInterval = Tway(100)

    fun change(settings : Ton,lvl : Int)
    {
        for (key in settings.keySet())
        {
            try
            {
                if (!settings.get(key).isEmpty) when (key)
                {
                    "Height"                    -> height.set(settings.get(key).first().toInt(),lvl)
                    "Width"                     -> width.set(settings.get(key).first().toFloat(),lvl)
                    "HorizontalMargin"          -> horizontalMargin.set(settings.get(key).first().toInt(),lvl)
                    "VerticalMargin"            -> verticalMargin.set(settings.get(key).first().toInt(),lvl)
                    "PrimaryTextSize"           -> primaryTextSize.set(settings.get(key).first().toFloat(),lvl)
                    "SecondaryTextSize"         -> secondaryTextSize.set(settings.get(key).first().toFloat(),lvl)
                    "PrimaryTextColor"          -> primaryTextColor.set(Color.parseColor(settings.get(key).first()),lvl)
                    "SecondaryTextColor"        -> secondaryTextColor.set(Color.parseColor(settings.get(key).first()),lvl)
                    "PrimaryBackgroundColor"    -> primaryBackgroundColor.set(Color.parseColor(settings.get(key).first()),lvl)
                    "SecondaryBackgroundColor"  -> secondaryBackgroundColor.set(Color.parseColor(settings.get(key).first()),lvl)
                    "RepeatInitialInterval"     -> repeatInitialInterval.set(settings.get(key).first().toInt(),lvl)
                    "RepeatInterval"            -> repeatInterval.set(settings.get(key).first().toInt(),lvl)
                }
            }
            catch (ex : Exception)
            {
                when (ex.message) //https://developer.android.com/reference/android/graphics/Color.html#parseColor%28java.lang.String%29
                {
                    "Unknown color" -> Log.e("|||","Unknown color, pls use '#RRGGBB' format.")
                    else -> Log.e("|||","${ex.message}") //TODO better
                }

            }
        }
    }

    fun reset(lvl : Int)
    {
        height.reset(lvl)
        width.reset(lvl)
        horizontalMargin .reset(lvl)
        verticalMargin.reset(lvl)
        primaryTextSize.reset(lvl)
        secondaryTextSize.reset(lvl)
        primaryTextColor.reset(lvl)
        secondaryTextColor.reset(lvl)
        primaryBackgroundColor.reset(lvl)
        secondaryBackgroundColor.reset(lvl)
        repeatInitialInterval.reset(lvl)
        repeatInterval.reset(lvl)
    }
}