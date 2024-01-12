package com.cookbook.app.ui.overview

import android.content.Intent
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import coil.load
import com.cookbook.app.R
import com.cookbook.app.models.Result
import org.jsoup.Jsoup

class RecipeOverviewFragment : Fragment() {

    inline fun <reified T : Parcelable> Intent.parcelable(key: String): T? = when {
        SDK_INT >= 33 -> getParcelableExtra(key, T::class.java)
        else -> @Suppress("DEPRECATION") getParcelableExtra(key) as? T
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_recipe_overview, container, false)

        // Retrieve the Result object from the Intent
        val myBundle: Result? = activity?.intent?.parcelable("result")

        view.findViewById<ImageView?>(R.id.imageView_main).load(myBundle?.image)
        val titleTextView: TextView = view.findViewById(R.id.textView_title)
        // Set the title using the title from myBundle
        titleTextView.text = myBundle?.title
        val likesTextView: TextView = view.findViewById(R.id.textView_likes)
        // Set the likes using the likes from myBundle
        likesTextView.text = myBundle?.aggregateLikes.toString()
        val timeTextView: TextView = view.findViewById(R.id.textView_time)
        // Set the likes using the likes from myBundle
        likesTextView.text = myBundle?.readyInMinutes.toString()


        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            val summaryTextView: TextView = view.findViewById(R.id.textView_summary)
            // Set the summary using the summary from myBundle
            summaryTextView.text = summary
        }



        if (myBundle?.vegetarian == true){
            view.findViewById<ImageView?>(R.id.imageView_Vegetarian).setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            val vegTextView: TextView = view.findViewById(R.id.textView_Vegetarian)
            vegTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.vegan == true){
            view.findViewById<ImageView?>(R.id.imageView_Vegan).setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            val veganTextView: TextView = view.findViewById(R.id.textView_Vegan)
            veganTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.glutenFree == true){
            view.findViewById<ImageView?>(R.id.imageView_GlutenFree).setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            val glutenFreeTextView: TextView = view.findViewById(R.id.textView_GlutenFree)
            glutenFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.dairyFree == true){
            view.findViewById<ImageView?>(R.id.imageView_DiaryFree).setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            val diaryFreeTextView: TextView = view.findViewById(R.id.textView_DiaryFree)
            diaryFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }


        if (myBundle?.veryHealthy == true){
            view.findViewById<ImageView?>(R.id.imageView_Healthy).setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            val healthyFreeTextView: TextView = view.findViewById(R.id.textView_Healthy)
            healthyFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.cheap == true){
            view.findViewById<ImageView?>(R.id.imageView_Cheap).setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            val cheapFreeTextView: TextView = view.findViewById(R.id.textView_Cheap)
            cheapFreeTextView.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        return view
    }
}
