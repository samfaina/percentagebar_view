package dev.samfaina.percentagebarview

import android.animation.ValueAnimator
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import dev.samfaina.percentagebarview.utils.AnimationConstants.Companion.DEFAULT_DELAY
import dev.samfaina.percentagebarview.utils.AttrHelper
import dev.samfaina.percentagebarview.utils.ViewHolder

/**
 * View to render a percentage bar
 * @param context [Context]
 * @param attrs [AttributeSet]
 */
class PercentageBarView(context: Context, attrs: AttributeSet) : ConstraintLayout(context, attrs) {

    // aux attrs
    private var thresholdPosition: Float = 0f
    private var maxWith: Float = 0f
    private var initialProgress: Int = 0
    private var initialPercProgress: Float = 0f

    // attrs helper class
    private var attrHelper = AttrHelper()

    // percentbar model
    private var model: PercentBarModel

    // percentbar viewholder
    private var viewHolder: ViewHolder


    /**
     * Initializes view
     */
    init {

        inflate(context, R.layout.percentage_bar, this)


        thresholdPosition = resources.getDimensionPixelSize(R.dimen.threshold_position).toFloat()


        attrs.let {
            model = attrHelper.getModel(it, context)
            viewHolder = ViewHolder(this).applyOptions(model)
        }


        // progress
        if (model.progressValue != -1) {
            setProgress(model.progressValue.toFloat(), true)
        }
    }


    /**
     * Animates percentage text
     * @param progress [Float] progress
     */
    private fun animateText(progress: Float) {
        ValueAnimator.ofFloat(initialPercProgress, progress).apply {
            duration = model.progressTextAnimDuration.toLong()
            startDelay = DEFAULT_DELAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                interpolator = model.progressTextInterpolator
            }
            addUpdateListener { updateAnimation ->
                val value = updateAnimation.animatedValue as Float
                if (value == progress) {
                    initialPercProgress = value
                }
                viewHolder.progressTextView.text = context.getString(
                    R.string.percentage,
                    value
                )
            }
            start()
        }
    }

    /**
     * Animates percentage bar
     * @param prog [Float] progress
     */
    private fun animateBar(prog: Float) {
        ValueAnimator.ofInt(initialProgress, prog.toInt()).apply {
            duration = model.progressAnimDuration.toLong()
            startDelay = DEFAULT_DELAY
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                interpolator = model.progressInterpolator
            }
            addUpdateListener { updateAnimation ->
                val value = updateAnimation.animatedValue as Int
                updateWidth(value)

                if (value == prog.toInt()) {
                    initialProgress = value
                }
            }

            start()
        }
    }


    private fun updateWidth(width: Int) {
        val progressLayoutParams = viewHolder.progressView.layoutParams
        progressLayoutParams.width = width
        viewHolder.progressView.layoutParams = progressLayoutParams

    }


    /**
     * Set percentage bar progress
     * @param progress Float
     * @param animate Boolean
     */
    fun setProgress(progress: Float, animate: Boolean) {
        viewHolder.wrapper.postDelayed({
            maxWith = viewHolder.wrapper.measuredWidth - thresholdPosition
            val prog = progress * maxWith / 100

            if (animate && maxWith > 0) {
                animateBar(prog)
                if (model.progressVisible) {
                    animateText(progress)
                }
            } else {
                updateWidth(prog.toInt())
                initialProgress = prog.toInt()
                if (model.progressVisible) {
                    viewHolder.progressTextView.text = context.getString(R.string.percentage, prog)
                }
            }
        }, 200)

    }

    /**
     * Set threshold text programatically
     * @param text [String]
     */
    fun setThresholdText(text: String) {
        viewHolder.thresholdTextView.visibility = View.VISIBLE
        viewHolder.thresholdTextView.text = text
    }


}

