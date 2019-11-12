package dev.samfaina.percentagebarviewsample


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import dev.samfaina.percentagebarview.PercentageBarView

/**
 * A simple [Fragment] subclass.
 */
class MainFragment : Fragment() {

    lateinit var percentageBarView: PercentageBarView
    lateinit var percentageBarView2: PercentageBarView
    lateinit var prgButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        percentageBarView = view.findViewById(R.id.percent_bar)
        percentageBarView2 = view.findViewById(R.id.percent_bar2)
        prgButton = view.findViewById(R.id.progress_btn)

        percentageBarView.setProgress(80f, true)



        prgButton.setOnClickListener {
            percentageBarView.setProgress((1..100).random().toFloat(), true)
            percentageBarView2.setProgress((1..100).random().toFloat(), true)
        }


        return view
    }


}
