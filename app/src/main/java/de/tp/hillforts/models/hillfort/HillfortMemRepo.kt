package de.tp.hillforts.models.hillfort

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.concurrent.atomic.AtomicLong


var nextId: AtomicLong = AtomicLong(1)
internal fun getId(): Long{
    return nextId.getAndAdd(1L)
}

class HillfortMemRepo: IHillfortRepo, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()

    /**
     * Create a hillfort in the repo.
     * @param hillfort hillford to be created within the repo
     * @author Thomas Pilz
     * @return created hillford
     */
    override fun create(hillfort: HillfortModel): HillfortModel {
        hillfort.id = getId()
        hillforts.add(hillfort.copy())
        return hillfort
    }

    /**
     * Update a hillford in the repo. Hillford to be updated is automatically determined by ID of provided hillfort.
     * @author Thomas Pilz
     * @return updated hillford
     */
    override fun update(hillfort: HillfortModel): HillfortModel? {
        var found = findById(hillfort.id)
        info("Found hillford: $found\nUpdate with: $hillfort")
        if(found != null){
            if(!found.equals(hillfort)){
                if(!found.name.equals(hillfort.name)){
                    found.name = hillfort.name
                }
                if(!found.desc.equals(hillfort.desc)){
                    found.desc = hillfort.desc
                }
                // do not compare images paths, just change them
                found.images = hillfort.images

                if(!found.loc.equals(hillfort.loc)){
                    found.loc = hillfort.loc
                }

                found.dateVisited = hillfort.dateVisited

                if(!found.notes.equals(found.notes)){
                    found.notes = hillfort.notes
                }
            }
        }
        return found
    }

    /**
     * Delete Hillford from repo.
     * @param hillfort hillford to be deleted
     */
    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
    }

    /**
     * Find a hillfort by its ID.
     * @param id ID of a hillfort
     * @author Thomas Pilz
     * @return found hillford or null of not found
     */
    override fun findById(id: Long): HillfortModel? {
        return hillforts.find { it.id == id }
    }

    /**
     * Get all hillforts stored in the repo.
     * @return all hillforts
     */
    override fun findAll(): List<HillfortModel> {
        return hillforts
    }


}