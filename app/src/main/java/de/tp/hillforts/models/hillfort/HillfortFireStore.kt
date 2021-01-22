package de.tp.hillforts.models.hillfort

import android.content.Context
import android.graphics.Bitmap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.tp.hillforts.helpers.generateRandomId
import de.tp.hillforts.helpers.readImageFromPath
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Future
import java.util.concurrent.FutureTask

class HillfortFireStore(val context: Context) : IHillfortRepo, AnkoLogger {

    val hillforts = ArrayList<HillfortModel>()
    lateinit var userId: String
    lateinit var db: DatabaseReference
    lateinit var st: StorageReference

    override fun findAll(): List<HillfortModel> {
        return hillforts
    }

    override fun findById(id: Long): HillfortModel? {
        // not useful with FireStore as the Long ID has no relevance! Just there for compatability.
        //val found: HillfortModel? = hillforts.find { p -> p.id == id }
        //return found
        return null
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
        var found: HillfortModel? = null
        try {
            found = hillforts.find { hillfort.fbId.equals(it.fbId) }
        } catch (e: NoSuchElementException) {
        }
        if (found != null) {
            if (!found.equals(hillfort)) {
                if (!found.name.equals(hillfort.name)) {
                    found.name = hillfort.name
                }
                if (!found.desc.equals(hillfort.desc)) {
                    found.desc = hillfort.desc
                }

                // if anything changes in list update whole list
                if(found.images.size != hillfort.images.size){
                    found.images = hillfort.images
                    updateImages(found)
                }
                else{
                    for(i in found.images.indices){
                        if(!found.images[i].equals(hillfort.images[i])){
                            found.images = hillfort.images
                            updateImages(found)
                            break
                        }
                    }
                }

                if (!found.loc.equals(hillfort.loc)) {
                    found.loc = hillfort.loc
                }

                found.dateVisited = hillfort.dateVisited

                if (!found.notes.equals(hillfort.notes)) {
                    found.notes = hillfort.notes
                }

                if (found.rating != hillfort.rating) {
                    found.rating = hillfort.rating
                }

                if (found.isFavourite != hillfort.isFavourite) {
                    found.isFavourite = hillfort.isFavourite
                }
            }
        }

        db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(found)
        return found
    }

    fun updateImages(hillfort: HillfortModel) {
        if (hillfort.images.size > 0) {
            for (i in hillfort.images.indices){
                var curImage = hillfort.images[i]   // use "old school" iteration to get mutability

                val fileName = File(curImage)
                val imageName = fileName.getName()

                var imageRef = st.child(userId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, curImage)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    uploadTask
                        .addOnFailureListener {
                            info("Image upload for file $curImage failed: {it.message}")
                        }
                        .addOnSuccessListener { taskSnapshot ->
                            taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                                curImage = it.toString()
                                hillfort.images[i] = curImage
                                if(i == hillfort.images.size){
                                    // could be done smarter with a Future that resolved when all uploads are done but will do for now
                                    db.child("users").child(userId).child("hillforts").child(hillfort.fbId).setValue(hillfort)
                                }
                            }
                        }
                }
            }

        }
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
                dataSnapshot!!.children.mapNotNullTo(hillforts) {
                    it.getValue<HillfortModel>(
                        HillfortModel::class.java
                    )
                }
                hillfortsReady()
            }
        }
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        userId = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.keepSynced(true)
        hillforts.clear()
        db.child("users").child(userId).child("hillforts")
            .addListenerForSingleValueEvent(valueEventListener)
    }
}
