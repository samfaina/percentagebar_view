package dev.samfaina.percentagebarview

import android.view.animation.*

/**
 * Helper class that holds a HashMap
 * Match attrs enum with their corresponding interpolator
 */
class InterpolatorHelper {

    companion object {
        const val LINEAR_INTERPOLATOR = 0
        const val ACCELERATE_DECELERATE_INTERPOLATOR = 1
        const val ACCELERATE_INTERPOLATOR = 2
        const val ANTICIPATE_INTERPOLATOR = 3
        const val ANTICIPATE_OVERSHOOT_INTERPOLATOR = 4
        const val BOUNCE_INTERPOLATOR = 5
        const val DECELERATE_INTERPOLATOR = 6
        const val OVERSHOOT_INTERPOLATOR = 7

        val interpolatorMap: HashMap<Int, BaseInterpolator> = hashMapOf(
            LINEAR_INTERPOLATOR to LinearInterpolator(),
            ACCELERATE_DECELERATE_INTERPOLATOR to AccelerateDecelerateInterpolator(),
            ACCELERATE_INTERPOLATOR to AccelerateInterpolator(),
            ANTICIPATE_INTERPOLATOR to AnticipateInterpolator(),
            ANTICIPATE_OVERSHOOT_INTERPOLATOR to AnticipateOvershootInterpolator(),
            BOUNCE_INTERPOLATOR to BounceInterpolator(),
            DECELERATE_INTERPOLATOR to DecelerateInterpolator(),
            OVERSHOOT_INTERPOLATOR to OvershootInterpolator()
        )
    }




}