package de.tp.hillforts.models.hillfort

import java.util.concurrent.atomic.AtomicLong

interface IHillfortRepo {

    /**
     * Create a hillfort in the repo.
     * @param hillfort hillford to be created within the repo
     * @author Thomas Pilz
     * @return created hillford
     */
    fun create(hillfort: HillfortModel): HillfortModel

    /**
     * Update a hillford in the repo. Hillford to be updated is automatically determined by ID of provided hillfort.
     * @author Thomas Pilz
     * @return updated hillford or null if there is no such hillfort
     */
    fun update(hillfort: HillfortModel): HillfortModel?

    /**
     * Delete Hillford from repo.
     * @param hillfort hillford to be deleted
     */
    fun delete(hillfort: HillfortModel): Unit

    /**
     * Find a hillfort by its ID.
     * @param id ID of a hillfort
     * @author Thomas Pilz
     * @return found hillford or null of not found
     */
    fun findById(id: Long): HillfortModel?

    /**
     * Get all hillforts stored in the repo.
     * @return all hillforts
     */
    fun findAll(): List<HillfortModel>
}