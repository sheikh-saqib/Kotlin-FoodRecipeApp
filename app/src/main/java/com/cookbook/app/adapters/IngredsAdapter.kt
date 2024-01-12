package com.cookbook.app.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.compose.ui.text.capitalize
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.cookbook.app.R
import com.cookbook.app.models.ExtendedIngredient
import com.cookbook.app.utilities.ProjectConstants.Companion.baseImgUrl
import com.cookbook.app.utilities.recipeDiffUtility

class IngredsAdapter:RecyclerView.Adapter<IngredsAdapter.MyRecyclerViewHolder>() {

    private var recipeIngredList = emptyList<ExtendedIngredient>()
    class MyRecyclerViewHolder(viewItem: View):RecyclerView.ViewHolder(viewItem)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecyclerViewHolder {
        return MyRecyclerViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.ingred_row_layout, parent, false))
    }


    override fun getItemCount(): Int {
        return recipeIngredList.size
    }

    override fun onBindViewHolder(holder: MyRecyclerViewHolder, position: Int) {
        holder.itemView.findViewById<ImageView>(R.id.imageView_ingredient).load(baseImgUrl + recipeIngredList[position].image){
            crossfade(500)
            error(R.drawable.icon_error)
        }
        holder.itemView.findViewById<TextView>(R.id.textView_IngredientName).text = recipeIngredList[position].name.replaceFirstChar { it.uppercaseChar() }
        holder.itemView.findViewById<TextView>(R.id.textView_AmountofIngredient).text = recipeIngredList[position].amount.toString()
        holder.itemView.findViewById<TextView>(R.id.textView_UnitofIngredient).text = recipeIngredList[position].unit
        holder.itemView.findViewById<TextView>(R.id.textView_consistencyofIngredient).text = recipeIngredList[position].consistency
        holder.itemView.findViewById<TextView>(R.id.textView_OriginalIngredient).text = recipeIngredList[position].original

    }

    fun setData(ingredsNew:List<ExtendedIngredient>){
        val ingredDiffUtil = recipeDiffUtility(recipeIngredList, ingredsNew)
        val difUtilRes = DiffUtil.calculateDiff(ingredDiffUtil)
        recipeIngredList = ingredsNew
        difUtilRes.dispatchUpdatesTo(this)
    }
}