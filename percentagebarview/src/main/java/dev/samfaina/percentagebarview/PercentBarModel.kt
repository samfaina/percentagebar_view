package dev.samfaina.percentagebarview

import android.graphics.drawable.Drawable
import android.view.animation.BaseInterpolator
import dev.samfaina.percentagebarview.utils.AnimationConstants.Companion.DEFAULT_DURATION
import dev.samfaina.percentagebarview.utils.InterpolatorHelper

data class PercentBarModel(
    // progress attrs
    var progressColor: Int = 0,
    var progressDrawable: Drawable? = null,
    var progressHeight: Float = 0f,
    var progressValue: Int = -1,
    var progressVisible: Boolean = false,
    var progressTextColor: Int = 0,


    // progress anims
    var progressAnimDuration: Int = DEFAULT_DURATION,
    var progressTextAnimDuration: Int = DEFAULT_DURATION,
    var progressInterpolator: BaseInterpolator? =
        InterpolatorHelper.interpolatorMap[InterpolatorHelper.LINEAR_INTERPOLATOR],
    var progressTextInterpolator: BaseInterpolator? =
        InterpolatorHelper.interpolatorMap[InterpolatorHelper.LINEAR_INTERPOLATOR],

    // threshold attrs
    var thresholdColor: Int = 0,
    var thresholdDrawable: Drawable? = null,
    var thresholdHeight: Float = 0f,
    var thresholdHWidth: Float = 0f,
    var thresholdVisible: Boolean = true,
    var thresholdTextColor: Int = 0,
    var thresholdTextPosition: Int = 0,
    var thresholdText: String? = null,

    // background attrs
    var backgroundBarColor: Int = 0,
    var backgroundBarDrawable: Drawable? = null,
    var backgroundBarHeight: Float = 0f,

    // label attrs
    var labelText: String? = null,
    var labelColor: Int = 0
)