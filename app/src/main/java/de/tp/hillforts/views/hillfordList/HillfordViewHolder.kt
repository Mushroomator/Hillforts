package de.tp.hillforts.views.hillfordList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.tp.hillforts.models.hillfort.HillfortModel
import kotlinx.android.synthetic.main.card_hillfort.view.*

class HillfordViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    /**
     * Bind data to fields on card
     * @param hillford hillford to be displayed in card
     * @param listener listener
     */
    fun bind(hillford: HillfortModel, listener: HillfordListener){
        // bind data to UI fields
        itemView.tvName.text = hillford.name
        itemView.tvLatVal.text = "%.6f".format(hillford.loc.lat)
        itemView.tvLngVal.text = "%.6f".format(hillford.loc.lng)
        itemView.cbVisitedList.isChecked = hillford.dateVisited != null
        // display first available picture
        if (hillford.images.isNotEmpty()){
            val image = hillford.images.first{ it.isNotEmpty() }
            Glide.with(itemView.context).load(image).into(itemView.ivImage);
        }

        // call listener
        itemView.setOnClickListener{ listener.onHillfordClick(hillford) }
    }
}