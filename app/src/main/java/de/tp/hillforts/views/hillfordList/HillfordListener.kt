package de.tp.hillforts.views.hillfordList

import de.tp.hillforts.models.HillfortModel

interface HillfordListener {

    /**
     * Executed when a hillford is clicked on in recycler view.
     * @param hillford hillford which was clicked on
     */
    fun onHillfordClick(hillford: HillfortModel)
}