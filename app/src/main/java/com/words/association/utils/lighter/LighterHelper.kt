package com.words.association.utils.lighter

import android.graphics.*
import android.view.View
import android.view.animation.*
import android.view.animation.Interpolator
import me.samlss.lighter.parameter.LighterParameter
import me.samlss.lighter.parameter.MarginOffset
import me.samlss.lighter.shape.LighterShape

class LighterHelper {

    companion object {
        private const val TAG = "LighterHelper"

        fun getDashPaint(): Paint {
            val paint =
                Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 20f
            paint.pathEffect = DashPathEffect(floatArrayOf(20f, 20f), 0.0f)
            paint.maskFilter = BlurMaskFilter(20.0f, BlurMaskFilter.Blur.SOLID)
            return paint
        }

        fun getDiscretePaint(): Paint {
            val paint =
                Paint(Paint.ANTI_ALIAS_FLAG)
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 20f
            paint.pathEffect = DiscretePathEffect(10f, 10f)
            paint.maskFilter = BlurMaskFilter(20f, BlurMaskFilter.Blur.SOLID)
            return paint
        }

        fun getScaleAnimation(
            fromX: Float = 0.0f,
            toX: Float = 0.0f,
            fromY: Float = 0.0f,
            toY: Float = 0.0f,
            pivotXType: Int = Animation.ABSOLUTE,
            pivotXValue: Float = 0.0f,
            pivotYType: Int = Animation.ABSOLUTE,
            pivotYValue: Float = 0.0f,
            duration: Long = 0,
            interpolator: Interpolator = BounceInterpolator()
        ): Animation {
            val animation =
                ScaleAnimation(
                    fromX,
                    toX,
                    fromY,
                    toY,
                    pivotXType,
                    pivotXValue,
                    pivotYType,
                    pivotYValue
                )
            animation.duration = duration
            animation.interpolator = interpolator
            return animation
        }


        fun getTranslateAnimation(
            fromXDelta: Float = 0.0f,
            toXDelta: Float = 0.0f,
            fromYDelta: Float = 0.0f,
            toYDelta: Float = 0.0f,
            duration: Long = 0,
            interpolator: Interpolator = BounceInterpolator()
        ): TranslateAnimation {
            val animation =
                TranslateAnimation(fromXDelta, toXDelta, fromYDelta, toYDelta)
            animation.duration = duration
            animation.interpolator = interpolator
            return animation
        }

        fun getRotateAnimation(
            fromDegrees: Float = 0.0f,
            toDegrees: Float = 0.0f,
            pivotXType: Int = Animation.ABSOLUTE,
            pivotXValue: Float = 0.0f,
            pivotYType: Int = Animation.ABSOLUTE,
            pivotYValue: Float = 0.0f,
            duration: Long = 0,
            interpolator: Interpolator = BounceInterpolator()
        ): RotateAnimation {
            val animation =
                RotateAnimation(
                    fromDegrees,
                    toDegrees,
                    pivotXType,
                    pivotXValue,
                    pivotYType,
                    pivotYValue
                )
            animation.duration = duration
            animation.interpolator = interpolator
            return animation
        }

        fun getLighterParameterView(
            lighterView: View,
            tipLayoutId: Int? = null,
            tipLayout: View? = null,
            lighterShape: LighterShape,
            shapeXOffset: Float = 0.0f,
            shapeYOffset: Float = 0.0f,
            viewDirection: Int,
            animation: Animation? = null,
            viewOffset: MarginOffset = MarginOffset(0, 0, 0, 0)
        ): LighterParameter {
            return LighterParameter.Builder().apply {
                setHighlightedView(lighterView)
                tipLayout?.let { setTipView(it) }
                tipLayoutId?.let{setTipLayoutId(it)}
                setLighterShape(lighterShape)
                setShapeXOffset(shapeXOffset)
                setShapeYOffset(shapeYOffset)
                setTipViewRelativeDirection(viewDirection)
                animation?.let { setTipViewDisplayAnimation(it) }
                setTipViewRelativeOffset(viewOffset)
            }.build()


        }
    }

    fun LighterShape.paint(p: Paint) = this.setPaint(p)
}






