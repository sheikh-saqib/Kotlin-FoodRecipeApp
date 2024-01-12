package com.cookbook.app.ui.home.filterMenu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.cookbook.app.R
import com.cookbook.app.ui.home.HomeFragment
import com.cookbook.app.utilities.ProjectConstants.Companion.diet_type_default
import com.cookbook.app.utilities.ProjectConstants.Companion.meal_type_default
import com.cookbook.app.viewModels.ViewModelRecipe
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import java.util.Locale

/**
 * A simple [Fragment] subclass.
 * Use the [RecipesFilter.newInstance] factory method to
 * create an instance of this fragment.
 */
class RecipesFilter : BottomSheetDialogFragment() {

    private lateinit var recipeVM: ViewModelRecipe

    private var mTChip = meal_type_default
    private var mTChipId = 0
    private var dTChip = diet_type_default
    private var dTChipId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        recipeVM = ViewModelProvider(requireActivity()).get(ViewModelRecipe::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val mView = inflater.inflate(R.layout.fragment_recipes_filter, container, false)
        recipeVM.getDietTypeAndMeal.asLiveData().observe(viewLifecycleOwner) { vals ->
            mTChip = vals.checkedMealType
            dTChip = vals.checkedDietType
            updateList(
                vals.checkedMealTypeId,
                mView.findViewById(R.id.MealType_ChipGroup)
            )
            updateList(
                vals.checkedDietTypeId,
                mView.findViewById(R.id.DietType_ChipGroup)
            )

        }
        mView.findViewById<ChipGroup>(R.id.MealType_ChipGroup)
            .setOnCheckedStateChangeListener { grp, checkedChipId ->
                val chip = grp.findViewById<Chip>(checkedChipId[0])
                Log.d("checkedChipId", checkedChipId[0].toString())
                val checkedMT = chip.text.toString().lowercase(Locale.ROOT)
                Log.d("checkedChipId", checkedMT.toString())
                mTChip = checkedMT
                mTChipId = checkedChipId[0]
            }

        mView.findViewById<ChipGroup>(R.id.DietType_ChipGroup)
            .setOnCheckedStateChangeListener { grp, checkedChipId ->
                val chip = grp.findViewById<Chip>(checkedChipId[0])
                val checkedDT = chip.text.toString().lowercase(Locale.ROOT)
                dTChip = checkedDT
                dTChipId = checkedChipId[0]
            }

        mView.findViewById<Button>(R.id.filterApply_btn).setOnClickListener {
            recipeVM.dietTypeAndMealSave(
                mTChip, mTChipId, dTChip, dTChipId
            )
            dismiss()
            findNavController().navigate(R.id.action_recipesFilter_to_navigation_home)

            // Call the onFilterApplied function in HomeFragment
            val homeFragment = parentFragmentManager.findFragmentById(R.id.fragment_home) as? HomeFragment
//            homeFragment?.onFilterApplied()
        }

        return mView
    }

    private fun updateList(chipId: Int, chipGroup: ChipGroup) {
        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (ex: Exception) {
                Log.d("FilterRecipes", ex.message.toString())
            }
        }
    }
}