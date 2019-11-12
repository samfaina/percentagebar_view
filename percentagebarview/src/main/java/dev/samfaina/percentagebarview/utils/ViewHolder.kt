package dev.samfaina.percentagebarview.utils

import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import dev.samfaina.percentagebarview.PercentBarModel
import dev.samfaina.percentagebarview.R

/**
 * Holds all nested views
 */
class ViewHolder(view: View) {

    var progressView: View = view.findViewById(R.id.progress)
    var thresholdView: View = view.findViewById(R.id.threshold)
    var backgroundView: View = view.findViewById(R.id.background)
    var progressTextView: TextView = view.findViewById(R.id.progress_text)
    var labelTextView: TextView = view.findViewById(R.id.label)
    var thresholdTextView: TextView = view.findViewById(R.id.threshold_text)
    var constrained: ConstraintLayout = view.findViewById(R.id.constrained)
    var wrapper: RelativeLayout = view.findViewById(R.id.wrapper)


    /**
     * Applies Attribute set options
     */
    fun applyOptions(model: PercentBarModel): ViewHolder {
        // progress bar
        setupProgressbar(model)

        // progress text
        setupProgressText(model)

        // label text
        setupLabelText(model)

        // background bar
        setupBackgroundBar(model)

        // threshold bar
        setupThresholdBar(model)

        // threshold text
        setupThresholdText(model)

        return this
    }

    private fun setupThresholdText(model: PercentBarModel) {
        when (model.thresholdText != null) {
            true -> {
                thresholdTextView.visibility =View.VISIBLE
                setText(thresholdTextView, model.thresholdText)
                thresholdTextView.setTextColor(model.thresholdTextColor)
                val constraintSet = ConstraintSet()
                constraintSet.clone(constrained)
                if (model.thresholdTextPosition == PositionConstants.BOTTOM) {
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
            false ->  thresholdTextView.visibility =View.GONE
        }
    }

    private fun setupThresholdBar(model: PercentBarModel) {
        when (model.thresholdVisible) {
            true -> {
                thresholdView.visibility = View.VISIBLE
                thresholdView.layoutParams.height = model.thresholdHeight.toInt()
                thresholdView.layoutParams.width = model.thresholdHWidth.toInt()
                if (model.thresholdDrawable != null) {
                    thresholdView.background = model.thresholdDrawable
                } else {
                    thresholdView.setBackgroundColor(model.thresholdColor)
                }
            }
            false -> thresholdView.visibility = View.GONE

        }
    }

    private fun setupBackgroundBar(model: PercentBarModel) {
        backgroundView.layoutParams.height = model.backgroundBarHeight.toInt()
        when {
            model.backgroundBarDrawable != null -> backgroundView.background = model.backgroundBarDrawable
            else -> backgroundView.setBackgroundColor(model.backgroundBarColor)
        }
    }

    private fun setupLabelText(model: PercentBarModel) {
        setText(labelTextView, model.labelText)
        labelTextView.setTextColor(model.labelColor)
        labelTextView.visibility = when (model.labelText != null) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }


    private fun setupProgressText(model: PercentBarModel) {
        progressTextView.setTextColor(model.progressTextColor)
        progressTextView.visibility = when (model.progressVisible) {
            true -> View.VISIBLE
            false -> View.GONE
        }
    }

    private fun setupProgressbar(model: PercentBarModel) {
        progressView.layoutParams.height = model.progressHeight.toInt()
        when {
            model.progressDrawable != null -> progressView.background = model.progressDrawable
            else -> progressView.setBackgroundColor(model.progressColor)
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

}