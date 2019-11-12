package dev.samfaina.percentagebarview

import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.BaseInterpolator
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat

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
    private val defaultDelay: Long = 600
    private val defaultDuration: Int = 800

    // views
    var progressView: View
    var thresholdView: View
    var backgroundView: View
    var progressTextView: TextView
    var labelTextView: TextView
    var thresholdTextView: TextView
    var constrained: ConstraintLayout
    var wrapper: RelativeLayout

    // progress attrs
    private var progressColor: Int = 0
    private var progressDrawable: Drawable? = null
    private var progressHeight: Float = 0f
    private var progressValue: Int = -1
    private var progressVisible: Boolean = false
    private var progressTextColor: Int = 0

    // progress anims
    private var progressAnimDuration: Int = defaultDuration
    private var progressTextAnimDuration: Int = defaultDuration
    private var progressInterpolator: BaseInterpolator? =
        InterpolatorHelper.interpolatorMap[InterpolatorHelper.LINEAR_INTERPOLATOR]
    private var progressTextInterpolator: BaseInterpolator? =
        InterpolatorHelper.interpolatorMap[InterpolatorHelper.LINEAR_INTERPOLATOR]

    // threshold attrs
    private var thresholdColor: Int = 0
    private var thresholdDrawable: Drawable? = null
    private var thresholdHeight: Float = 0f
    private var thresholdHWidth: Float = 0f
    private var thresholdVisible: Boolean = true
    private var thresholdTextColor: Int = 0
    private var thresholdTextPosition: Int = 0
    private var thresholdText: String? = null

    // background attrs
    private var backgroundBarColor: Int = 0
    private var backgroundBarDrawable: Drawable? = null
    private var backgroundBarHeight: Float = 0f

    // label attrs
    private var labelText: String? = null
    private var labelColor: Int = 0


    /**
     * Initializes view
     */
    init {

        inflate(context, R.layout.percentage_bar, this)

        progressView = findViewById(R.id.progress)
        thresholdView = findViewById(R.id.threshold)
        backgroundView = findViewById(R.id.background)
        wrapper = findViewById(R.id.wrapper)
        labelTextView = findViewById(R.id.label)
        progressTextView = findViewById(R.id.progress_text)
        thresholdTextView = findViewById(R.id.threshold_text)
        constrained = findViewById(R.id.constrained)

        thresholdPosition = resources.getDimensionPixelSize(R.dimen.threshold_position).toFloat()


        attrs.let {
            getOptions(context, it)
            applyOptions()
        }


        // progress
        if (progressValue != -1) {
            setProgress(progressValue.toFloat(), true)
        }
    }


    /**
     * Fetch view setup from attributeset
     * @param attrs [TypedArray]
     * @param context [Context]
     */
    private fun getOptions(context: Context, attributeSet: AttributeSet) {
        val attrs: TypedArray =
            context.theme.obtainStyledAttributes(attributeSet, R.styleable.PercentageBarView, 0, 0)

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
        backgroundBarDrawable =
            attrs.getDrawable(R.styleable.PercentageBarView_background_bar_color)
        if (backgroundBarDrawable == null) {
            backgroundBarColor = attrs.getColor(
                R.styleable.PercentageBarView_background_bar_color,
                ContextCompat.getColor(context, R.color.default_background_color)
            )
        }


        backgroundBarHeight =
            when (attrs.hasValue(R.styleable.PercentageBarView_background_bar_height)) {
                true -> attrs.getDimension(
                    R.styleable.PercentageBarView_background_bar_height,
                    context.resources.getDimension(R.dimen.default_bar_height)
                )
                false -> progressHeight
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
        labelColor = attrs.getColor(
            R.styleable.PercentageBarView_label_color,
            ContextCompat.getColor(context, R.color.default_text_color)
        )
        labelText = attrs.getString(R.styleable.PercentageBarView_label_text)
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
        progressDrawable = attrs.getDrawable(R.styleable.PercentageBarView_progress_color)
        if (progressDrawable == null) {
            progressColor = attrs.getColor(
                R.styleable.PercentageBarView_progress_color,
                ContextCompat.getColor(context, R.color.default_progress_color)
            )
        }

        progressHeight = attrs.getDimension(
            R.styleable.PercentageBarView_progress_height,
            context.resources.getDimension(R.dimen.default_bar_height)
        )

        progressValue = attrs.getInt(R.styleable.PercentageBarView_progress, -1)

        progressAnimDuration =
            attrs.getInt(R.styleable.PercentageBarView_progress_anim_duration, defaultDuration)
        val progressInt = attrs.getInt(R.styleable.PercentageBarView_progress_interpolator, -1)
        if (progressInt != -1) {
            progressInterpolator = InterpolatorHelper.interpolatorMap[progressInt]
        }


        // progress text
        progressVisible =
            attrs.getBoolean(R.styleable.PercentageBarView_progress_text_visible, false)
        progressTextColor = attrs.getColor(
            R.styleable.PercentageBarView_progress_text_color,
            ContextCompat.getColor(context, R.color.default_text_color)
        )

        progressTextAnimDuration = attrs.getInt(
            R.styleable.PercentageBarView_progress_text_anim_duration,
            defaultDuration
        )

        val progressTextInt =
            attrs.getInt(R.styleable.PercentageBarView_progress_text_interpolator, -1)
        if (progressTextInt != -1) {
            progressTextInterpolator = InterpolatorHelper.interpolatorMap[progressTextInt]
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
        thresholdDrawable = attrs.getDrawable(R.styleable.PercentageBarView_threshold_color)
        if (thresholdDrawable == null) {
            thresholdColor = attrs.getColor(
                R.styleable.PercentageBarView_threshold_color,
                ContextCompat.getColor(context, R.color.default_threshold_color)
            )
        }

        thresholdHeight = attrs.getDimension(
            R.styleable.PercentageBarView_threshold_height,
            context.resources.getDimension(R.dimen.default_threshold_height)
        )

        thresholdHWidth = attrs.getDimension(
            R.styleable.PercentageBarView_threshold_width,
            context.resources.getDimension(R.dimen.default_threshold_width)
        )

        thresholdVisible = attrs.getBoolean(R.styleable.PercentageBarView_threshold_visible, true)

        // threshold text
        if (thresholdVisible) {

            thresholdText = attrs.getString(R.styleable.PercentageBarView_threshold_text)
            thresholdTextColor = attrs.getColor(
                R.styleable.PercentageBarView_threshold_text_color,
                ContextCompat.getColor(context, R.color.default_text_color)
            )
            thresholdTextPosition =
                attrs.getInt(R.styleable.PercentageBarView_threshold_text_position, TOP)

        }
    }

    /**
     * Applies Attribute set options
     */
    private fun applyOptions() {
        // progress bar
        setupProgressbar()

        // progress text
        setupProgressText()

        // label text
        setupLabelText()

        // background bar
        setupBackgroundBar()

        // threshold bar
        setupThresholdBar()

        // threshold text
        setupThresholdText()


    }


    private fun setupThresholdText() {
        thresholdTextView.visibility = when (thresholdText != null) {
            true -> View.VISIBLE
            false -> View.GONE
        }


        if (thresholdText != null) {
            setText(thresholdTextView, thresholdText)
            thresholdTextView.setTextColor(thresholdTextColor)
            val constraintSet = ConstraintSet()
            constraintSet.clone(constrained)
            if (thresholdTextPosition == BOTTOM) {
                constraintSet.connect(
                    R.id.threshold_text,
                    ConstraintSet.TOP,
                    R.id.threshold,
                    ConstraintSet.BOTTOM
                )
                constraintSet.clear(R.id.threshold_text, ConstraintSet.BOTTOM)

            } else {
                constraintSet.connect(
                    R.id.threshold_text,
                    ConstraintSet.BOTTOM,
                    R.id.threshold,
                    ConstraintSet.TOP
                )
                constraintSet.clear(R.id.threshold_text, ConstraintSet.TOP)
            }
            constraintSet.applyTo(constrained)

        }
    }


    private fun setupThresholdBar() {
        thresholdView.visibility = when (thresholdVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
        if (thresholdVisible) {
            thresholdView.layoutParams.height = thresholdHeight.toInt()
            thresholdView.layoutParams.width = thresholdHWidth.toInt()
            if (thresholdDrawable != null) {
                thresholdView.background = thresholdDrawable
            } else {
                thresholdView.setBackgroundColor(thresholdColor)
            }
        }
    }


    private fun setupBackgroundBar() {
        backgroundView.layoutParams.height = backgroundBarHeight.toInt()
        if (backgroundBarDrawable != null) {
            backgroundView.background = backgroundBarDrawable
        } else {
            backgroundView.setBackgroundColor(backgroundBarColor)
        }
    }


    private fun setupLabelText() {
        setText(labelTextView, labelText)
        labelTextView.setTextColor(labelColor)
        labelTextView.visibility = when (labelText != null) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }


    private fun setupProgressText() {
        progressTextView.setTextColor(progressTextColor)
        progressTextView.visibility = when (progressVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }


    private fun setupProgressbar() {
        progressView.layoutParams.height = progressHeight.toInt()
        if (progressDrawable != null) {
            progressView.background = progressDrawable
        } else {
            progressView.setBackgroundColor(progressColor)
        }
    }


    /**
     * Set text to textview and handles @Null strings
     * @param view [TextView] view to set text
     * @param text [String?] text to set
     */
    private fun setText(view: TextView, text: String?) {
        text?.let {
            view.text = text
        }
    }


    /**
     * Animates percentage text
     * @param progress [Float] progress
     */
    private fun animateText(progress: Float) {
        ValueAnimator.ofFloat(initialPercProgress, progress).apply {
            duration = progressTextAnimDuration.toLong()
            startDelay = defaultDelay
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                interpolator = progressTextInterpolator
            }
            addUpdateListener { updateAnimation ->
                val value = updateAnimation.animatedValue as Float
                if (value == progress) {
                    initialPercProgress = value
                }
                progressTextView.text = context.getString(
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
            duration = progressAnimDuration.toLong()
            startDelay = defaultDelay
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                interpolator = progressInterpolator
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
        val progressLayoutParams = progressView.layoutParams
        progressLayoutParams.width = width
        progressView.layoutParams = progressLayoutParams

    }


    /**
     * Set percentage bar progress
     * @param progress Float
     * @param animate Boolean
     */
    fun setProgress(progress: Float, animate: Boolean) {
        wrapper.postDelayed({
            maxWith = wrapper.measuredWidth - thresholdPosition
            val prog = progress * maxWith / 100

            if (animate && maxWith > 0) {
                animateBar(prog)
                if (progressVisible) {
                    animateText(progress)
                }
            } else {
                updateWidth(prog.toInt())
                initialProgress = prog.toInt()
                if (progressVisible) {
                    progressTextView.text = context.getString(R.string.percentage, prog)
                }
            }
        },200)

    }

    /**
     * Set threshold text programatically
     * @param text [String]
     */
    fun setThresholdText(text: String) {
        thresholdTextView.text = text
    }


    /**
     * Constants to define threshold text position
     */
    companion object {
        const val TOP = 0
        const val BOTTOM = 1
    }


}

