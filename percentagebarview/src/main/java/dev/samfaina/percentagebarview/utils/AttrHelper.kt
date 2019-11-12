package dev.samfaina.percentagebarview.utils

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import dev.samfaina.percentagebarview.PercentBarModel
import dev.samfaina.percentagebarview.R
import dev.samfaina.percentagebarview.utils.AnimationConstants.Companion.DEFAULT_DURATION
import dev.samfaina.percentagebarview.utils.PositionConstants.Companion.TOP


/**
 * Fetch view setup from attributeset
 */
class AttrHelper {

    var model = PercentBarModel()

    /**
     * Build model from attribute set
     * @param attributeSet [AttributeSet]
     * @param context [Context]
     */
    fun getModel(
        attributeSet: AttributeSet,
        context: Context
    ): PercentBarModel {

        val attrs: TypedArray =
            context.theme.obtainStyledAttributes(
                attributeSet,
                R.styleable.PercentageBarView, 0, 0
            )
        // progress bar
        getProgressAttrs(attrs, context)

        // label text
        getLabelAttrs(attrs, context)


        // threshold
        getThresholdAttrs(attrs, context)


        // background bar
        getBackgroundAttrs(attrs, context)

        // recycle TypedArray
        attrs.recycle()

        return model
    }


    /**
     * Get progress settings from attrs
     * @param attrs [TypedArray]
     * @param context [Context]
     */
    private fun getProgressAttrs(
        attrs: TypedArray,
        context: Context
    ) {
        model.progressDrawable = attrs.getDrawable(R.styleable.PercentageBarView_progress_color)
        if (model.progressDrawable == null) {
            model.progressColor = attrs.getColor(
                R.styleable.PercentageBarView_progress_color,
                ContextCompat.getColor(
                    context,
                    R.color.default_progress_color
                )
            )
        }

        model.progressHeight = attrs.getDimension(
            R.styleable.PercentageBarView_progress_height,
            context.resources.getDimension(R.dimen.default_bar_height)
        )

        model.progressValue = attrs.getInt(R.styleable.PercentageBarView_progress, -1)

        model.progressAnimDuration =
            attrs.getInt(R.styleable.PercentageBarView_progress_anim_duration, DEFAULT_DURATION)
        val progressInt = attrs.getInt(R.styleable.PercentageBarView_progress_interpolator, -1)
        if (progressInt != -1) {
            model.progressInterpolator = InterpolatorHelper.interpolatorMap[progressInt]
        }


        // progress text
        model.progressVisible =
            attrs.getBoolean(R.styleable.PercentageBarView_progress_text_visible, false)
        model.progressTextColor = attrs.getColor(
            R.styleable.PercentageBarView_progress_text_color,
            ContextCompat.getColor(
                context,
                R.color.default_text_color
            )
        )

        model.progressTextAnimDuration = attrs.getInt(
            R.styleable.PercentageBarView_progress_text_anim_duration,
            DEFAULT_DURATION
        )

        val progressTextInt =
            attrs.getInt(R.styleable.PercentageBarView_progress_text_interpolator, -1)
        if (progressTextInt != -1) {
            model.progressTextInterpolator = InterpolatorHelper.interpolatorMap[progressTextInt]
        }
    }


    /**
     * Get background bar settings from attrs
     * @param attrs [TypedArray]
     * @param context [Context]
     */
    private fun getBackgroundAttrs(
        attrs: TypedArray,
        context: Context
    ) {
        model.backgroundBarDrawable =
            attrs.getDrawable(R.styleable.PercentageBarView_background_bar_color)
        if (model.backgroundBarDrawable == null) {
            model.backgroundBarColor = attrs.getColor(
                R.styleable.PercentageBarView_background_bar_color,
                ContextCompat.getColor(
                    context,
                    R.color.default_background_color
                )
            )
        }


        model.backgroundBarHeight =
            when (attrs.hasValue(R.styleable.PercentageBarView_background_bar_height)) {
                true -> attrs.getDimension(
                    R.styleable.PercentageBarView_background_bar_height,
                    context.resources.getDimension(R.dimen.default_bar_height)
                )
                false -> model.progressHeight
            }
    }

    /**
     * Get threshold settings from attrs
     * @param attrs [TypedArray]
     * @param context [Context]
     */
    private fun getThresholdAttrs(
        attrs: TypedArray,
        context: Context
    ) {
        model.thresholdDrawable = attrs.getDrawable(R.styleable.PercentageBarView_threshold_color)
        if (model.thresholdDrawable == null) {
            model.thresholdColor = attrs.getColor(
                R.styleable.PercentageBarView_threshold_color,
                ContextCompat.getColor(
                    context,
                    R.color.default_threshold_color
                )
            )
        }

        model.thresholdHeight = attrs.getDimension(
            R.styleable.PercentageBarView_threshold_height,
            context.resources.getDimension(R.dimen.default_threshold_height)
        )

        model.thresholdHWidth = attrs.getDimension(
            R.styleable.PercentageBarView_threshold_width,
            context.resources.getDimension(R.dimen.default_threshold_width)
        )

        model.thresholdVisible =
            attrs.getBoolean(R.styleable.PercentageBarView_threshold_visible, true)

        // threshold text
        if (model.thresholdVisible) {

            model.thresholdText = attrs.getString(R.styleable.PercentageBarView_threshold_text)
            model.thresholdTextColor = attrs.getColor(
                R.styleable.PercentageBarView_threshold_text_color,
                ContextCompat.getColor(
                    context,
                    R.color.default_text_color
                )
            )
            model.thresholdTextPosition =
                attrs.getInt(
                    R.styleable.PercentageBarView_threshold_text_position,
                    TOP
                )

        }


    }


    /**
     * Get label settings from attrs
     * @param attrs [TypedArray]
     * @param context [Context]
     */
    private fun getLabelAttrs(
        attrs: TypedArray,
        context: Context
    ) {
        model.labelColor = attrs.getColor(
            R.styleable.PercentageBarView_label_color,
            ContextCompat.getColor(
                context,
                R.color.default_text_color
            )
        )
        model.labelText = attrs.getString(R.styleable.PercentageBarView_label_text)
    }


}