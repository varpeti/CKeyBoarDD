package ml.varpeti.ckeyboardd

import android.graphics.Color
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

     var height = Tway(100)
     var width = Tway(1F)
     var horizontalMargin  = Tway(2)
     var verticalMargin = Tway(2)
     var primaryTextSize = Tway(25F)
     var secondaryTextSize = Tway(18F)
     var primaryTextColor = Tway(Color.parseColor("#ffffff"))
     var secondaryTextColor = Tway(Color.parseColor("#b0b0b0"))
     var primaryBackgroundColor = Tway(Color.parseColor("#000000"))
     var secondaryBackgroundColor = Tway(Color.parseColor("#404040"))

    fun change(settings : Ton,lvl : Int)
    {
        for (key in settings.keySet())
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
    }
}