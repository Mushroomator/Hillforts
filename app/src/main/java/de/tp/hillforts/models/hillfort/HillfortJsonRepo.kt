package de.tp.hillforts.models.hillfort

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.tp.hillforts.helpers.exists
import de.tp.hillforts.helpers.generateRandomId
import de.tp.hillforts.helpers.read
import de.tp.hillforts.helpers.write
import org.jetbrains.anko.info
import java.util.concurrent.atomic.AtomicLong

val JSON_FILE = ".json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillfortModel>>() {}.type

class HillfortJsonRepo: IHillfortRepo {

    val context: Context
    var userId: Long
    var hillforts = mutableListOf<HillfortModel>()
    var jsonFile: String

    constructor(context: Context, userId: Long){
        this.context = context
        this.userId = userId
        this.jsonFile = "hillforts_$userId$JSON_FILE"
        if(exists(context, jsonFile)){
            deserialize()
        }
    }

    /**
     * Create a hillfort in the repo.
     * @param hillfort hillford to be created within the repo
     * @author Thomas Pilz
     * @return created hillford
     */
    override fun create(hillfort: HillfortModel): HillfortModel {
        hillfort.id = generateRandomId()
        hillforts.add(hillfort.copy())
        serialize()
        return hillfort
    }

    /**
     * Update a hillford in the repo. Hillford to be updated is automatically determined by ID of provided hillfort.
     * @author Thomas Pilz
     * @return updated hillford
     */
    override fun update(hillfort: HillfortModel): HillfortModel? {
        var found = findById(hillfort.id)
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

                if(!found.notes.equals(hillfort.notes)){
                    found.notes = hillfort.notes
                }

                if(found.rating != hillfort.rating){
                    found.rating = hillfort.rating
                }

                if(found.isFavourite != hillfort.isFavourite){
                    found.isFavourite = hillfort.isFavourite
                }
            }
        }
        serialize()
        return found
    }

    /**
     * Delete Hillford from repo.
     * @param hillfort hillford to be deleted
     * @author Thomas Pilz
     */
    override fun delete(hillfort: HillfortModel) {
        hillforts.remove(hillfort)
        serialize()
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
     * @author Thomas Pilz
     */
    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    /**
     * Clear hillforts.
     */
    override fun clear() {
        hillforts.clear()
    }

    /**
     * Serialize hillforts.
     */
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillforts, listType)
        write(context, jsonFile, jsonString)
    }

    /**
     * Deserialize hillforts.
     */
    private fun deserialize() {
        val jsonString = read(context, jsonFile)
        hillforts = Gson().fromJson(jsonString, listType)
    }
}