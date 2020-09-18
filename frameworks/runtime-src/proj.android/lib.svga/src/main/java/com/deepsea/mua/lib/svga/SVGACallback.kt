package com.deepsea.mua.lib.svga

/**
 * Created by cuiminghui on 2017/3/30.
 */
interface SVGACallback {

    fun onPause()
    fun onFinished()
    fun onRepeat()
    fun onStep(frame: Int, percentage: Double)

}