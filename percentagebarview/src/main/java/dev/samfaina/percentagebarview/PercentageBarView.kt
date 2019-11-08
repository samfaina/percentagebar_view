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

class PercentageBarView : ConstraintLayout {

    // aux attribs
    private var thresholdPosition: Float = 0f
    private var maxWith: Float = 0f
    private var initialProgress: Int = 0
    private var initialPercProgress: Float = 0f
    private val defaultDelay: Long = 600
    private val defaultDuration: Int = 800

    // views
    lateinit var progressView: View
    lateinit var thresholdView: View
    lateinit var backgroundView: View
    lateinit var progressTextView: TextView
    lateinit var labelTextView: TextView
    lateinit var thresholdTextView: TextView
    lateinit var constrained: ConstraintLayout
    lateinit var wrapper: RelativeLayout

    // progress attribs
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

    // threshold attribs
    private var thresholdColor: Int = 0
    private var thresholdDrawable: Drawable? = null
    private var thresholdHeight: Float = 0f
    private var thresholdHWidth: Float = 0f
    private var thresholdVisible: Boolean = true
    private var thresholdTextVisible: Boolean = true
    private var thresholdTextColor: Int = 0
    private var thresholdTextPosition: Int = 0
    private var thresholdText: String? = null

    // background attribs
    private var backgroundBarColor: Int = 0
    private var backgroundBarDrawable: Drawable? = null
    private var backgroundBarHeight: Float = 0f

    // label attribs
    private var labelVisible: Boolean = false
    private var labelText: String? = null
    private var labelColor: Int = 0


    constructor(context: Context) : super(context, null) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs, 0) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    /**
     * Initializes view
     */
    private fun initView(context: Context, attributeSet: AttributeSet?) {

        View.inflate(context, R.layout.percentage_bar, this)

        progressView = findViewById(R.id.progress)
        thresholdView = findViewById(R.id.threshold)
        backgroundView = findViewById(R.id.background)
        wrapper = findViewById(R.id.wrapper)
        labelTextView = findViewById(R.id.label)
        progressTextView = findViewById(R.id.progress_text)
        thresholdTextView = findViewById(R.id.threshold_text)
        constrained = findViewById(R.id.constrained)

        thresholdPosition = resources.getDimensionPixelSize(R.dimen.threshold_position).toFloat()


        attributeSet?.let {
            getOptions(context, it)
            applyOptions()
        }


        // progress
        if (progressValue != -1) {
            wrapper.post {
                setProgress(progressValue.toFloat(), true)
            }
        }
    }


    /**
     * Fetch view setup from attributeset
     */
    private fun getOptions(context: Context, it: AttributeSet) {
        val attribs: TypedArray =
            context.theme.obtainStyledAttributes(it, R.styleable.PercentageBarView, 0, 0)

        // progress bar
        progressDrawable = attribs.getDrawable(R.styleable.PercentageBarView_progress_color)
        if (progressDrawable == null) {
            progressColor = attribs.getColor(
                R.styleable.PercentageBarView_progress_color,
                ContextCompat.getColor(context, R.color.default_progress_color)
            )
        }

        progressHeight = attribs.getDimension(
            R.styleable.PercentageBarView_progress_height,
            context.resources.getDimension(R.dimen.default_bar_height)
        )

        progressValue = attribs.getInt(R.styleable.PercentageBarView_progress, -1)

        progressAnimDuration =
            attribs.getInt(R.styleable.PercentageBarView_progress_anim_duration, defaultDuration)
        val progressInt = attribs.getInt(R.styleable.PercentageBarView_progress_interpolator, -1)
        if (progressInt != -1) {
            progressInterpolator = InterpolatorHelper.interpolatorMap[progressInt]
        }


        // progress text
        progressVisible =
            attribs.getBoolean(R.styleable.PercentageBarView_progress_text_visible, false)
        progressTextColor = attribs.getColor(
            R.styleable.PercentageBarView_progress_text_color,
            ContextCompat.getColor(context, R.color.default_text_color)
        )

        progressTextAnimDuration = attribs.getInt(
            R.styleable.PercentageBarView_progress_text_anim_duration,
            defaultDuration
        )

        val progressTextInt =
            attribs.getInt(R.styleable.PercentageBarView_progress_text_interpolator, -1)
        if (progressTextInt != -1) {
            progressTextInterpolator = InterpolatorHelper.interpolatorMap[progressTextInt]
        }

        // label text
        labelVisible = attribs.getBoolean(R.styleable.PercentageBarView_label_visible, false)
        labelColor = attribs.getColor(
            R.styleable.PercentageBarView_label_color,
            ContextCompat.getColor(context, R.color.default_text_color)
        )
        labelText = attribs.getString(R.styleable.PercentageBarView_label_text)


        // threshold
        thresholdDrawable = attribs.getDrawable(R.styleable.PercentageBarView_threshold_color)
        if (thresholdDrawable == null) {
            thresholdColor = attribs.getColor(
                R.styleable.PercentageBarView_threshold_color,
                ContextCompat.getColor(context, R.color.default_threshold_color)
            )
        }

        thresholdHeight = attribs.getDimension(
            R.styleable.PercentageBarView_threshold_height,
            context.resources.getDimension(R.dimen.default_threshold_height)
        )

        thresholdHWidth = attribs.getDimension(
            R.styleable.PercentageBarView_threshold_width,
            context.resources.getDimension(R.dimen.default_threshold_width)
        )

        thresholdVisible = attribs.getBoolean(R.styleable.PercentageBarView_threshold_visible, true)

        // threshold text
        if (thresholdVisible) {
            thresholdTextVisible =
                attribs.getBoolean(R.styleable.PercentageBarView_threshold_text_visible, true)

            if (thresholdTextVisible) {
                thresholdText = attribs.getString(R.styleable.PercentageBarView_threshold_text)
                thresholdTextColor = attribs.getColor(
                    R.styleable.PercentageBarView_threshold_text_color,
                    ContextCompat.getColor(context, R.color.default_text_color)
                )
                thresholdTextPosition =
                    attribs.getInt(R.styleable.PercentageBarView_threshold_text_position, TOP)
            }
        } else {
            thresholdTextVisible = false
        }


        // background bar
        backgroundBarDrawable =
            attribs.getDrawable(R.styleable.PercentageBarView_background_bar_color)
        if (backgroundBarDrawable == null) {
            backgroundBarColor = attribs.getColor(
                R.styleable.PercentageBarView_background_bar_color,
                ContextCompat.getColor(context, R.color.default_background_color)
            )
        }


        backgroundBarHeight =
            when (attribs.hasValue(R.styleable.PercentageBarView_background_bar_height)) {
                true -> attribs.getDimension(
                    R.styleable.PercentageBarView_background_bar_height,
                    context.resources.getDimension(R.dimen.default_bar_height)
                )
                false -> progressHeight
            }

        attribs.recycle()
    }

    /**
     * Applies Attribute set options
     */
    private fun applyOptions() {
        // progress bar
        progressView.layoutParams.height = progressHeight.toInt()
        if (progressDrawable != null) {
            progressView.background = progressDrawable
        } else {
            progressView.setBackgroundColor(progressColor)
        }

        // progress text
        progressTextView.setTextColor(progressTextColor)
        progressTextView.visibility = when (progressVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        // label text
        setText(labelTextView, labelText)
        labelTextView.setTextColor(labelColor)
        labelTextView.visibility = when (labelVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }

        // background bar
        backgroundView.layoutParams.height = backgroundBarHeight.toInt()
        if (backgroundBarDrawable != null) {
            backgroundView.background = backgroundBarDrawable
        } else {
            backgroundView.setBackgroundColor(backgroundBarColor)
        }

        // threshold bar
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

        // threshold text
        thresholdTextView.visibility = when (thresholdTextVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }


        if (thresholdTextVisible) {
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


    /**
     * Set text to textview and handles @Null strings
     */
    private fun setText(view: TextView, text: String?) {
        text?.let {
            view.text = text
        }
    }


    /**
     * Animates percentage text
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

    /**
     * Changes percentage bar with
     */
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
    }

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

