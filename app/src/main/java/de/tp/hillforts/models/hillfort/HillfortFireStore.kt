package de.tp.hillforts.models.hillfort

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import org.jetbrains.anko.AnkoLogger

class HillfortFireStore(val context: Context) : IHillfortRepo, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun findById(id: Long): HillfortModel? {
        val found: HillfortModel? = hillforts.find { p -> p.id == id }
        return found
    }

    override fun create(hillfort: HillfortModel): HillfortModel {
        val key = db.child("users").child(userId).child("hillforts").push().key
        key?.let {
            hillfort.fbId = key
            hillforts.add(hillfort)
            db.child("users").child(userId).child("hillforts").child(key).setValue(hillfort)
        }
        return hillfort
    }

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

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(found)
        return found
    }

    override fun delete(hillfort: HillfortModel) {
        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).removeValue()
        hillforts.remove(hillfort)
    }

    override fun clear() {
        hillforts.clear()
    }

    fun fetchHillforts(hillfortsReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(hillforts) { it.getValue<HillfortModel>(HillfortModel::class.java) }
                hillfortsReady()
            }
        }
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        hillforts.clear()
        db.child("users").child(userId).child("hillforts").addListenerForSingleValueEvent(valueEventListener)
    }
}
