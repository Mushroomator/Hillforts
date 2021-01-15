package de.tp.hillforts.views.hillfordList

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import de.tp.hillforts.models.HillfortModel
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
        itemView.tvLatVal.text = hillford.loc.lat.toString()
        itemView.tvLngVal.text = hillford.loc.lng.toString()
        itemView.cbVisitedList.isChecked = hillford.dateVisited != null
        // display first available picture
        if (hillford.images.isNotEmpty()){
            val image = hillford.images.first{ it.isNotEmpty() }
            Glide.with(itemView.context).load(image).into(itemView.ivImage);
            itemView.tvLatVal.text = "%.6f".format(hillford.loc.lat)
            itemView.tvLngVal.text = "%.6f".format(hillford.loc.lng)
        }

        // call listener
        itemView.setOnClickListener{ listener.onHillfordClick(hillford) }
    }
}