package de.tp.hillforts.views.settings

import android.os.Bundle
import android.widget.SeekBar
import de.tp.hillforts.R
import de.tp.hillforts.views.BaseView
import kotlinx.android.synthetic.main.settings_view.*

class SettingsView : BaseView(), SeekBar.OnSeekBarChangeListener {

    lateinit var presenter: SettingsPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_view)

        // initialize presenter
        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter

        // initialize view
        init(toolbar, true)
        presenter.doLoadUser()
        presenter.doLoadStatistics()
        sbNumImgVal.setOnSeekBarChangeListener(this)
        sbNumImgColVal.setOnSeekBarChangeListener(this)
        initSeekBars()
    }

    fun initSeekBars(){
        // Number of image columns
        var prog = resources.getInteger(R.integer.image_columns)
        tvImgColThumbVal.text = prog.toString()
        sbNumImgColVal.progress = --prog    // required as 0 is always included in seekBar but not needed here

        // Number of images
        sbNumImgVal.progress = resources.getInteger(R.integer.max_images)
        tvNumImgThumbVal.text = sbNumImgVal.progress.toString()
    }

    /**
     * Show a user's information on screen.
     * Updated for Firebase as there is no password available.
     * Instead display UID and email
     * @param email email address
     * @param uid user ID
     * @author Thomas Pilz
     */
    fun showUserInfo(email: String, uid: String){
        tvUserEmailVal.text = email
        tvUserUidVal.text = uid
    }

    /**
     * Show a user's statistics on screen.
     * @param total total number of hillforts
     * @param visited number of visited hillforts
     * @author Thomas Pilz
     */
    fun showStatistics(total: Int, visited: Int){
        tvTotalSitesVal.text = total.toString()
        tvVisitedSitesVal.text = visited.toString()
        var progress = 0
        if (total != 0){
            val pct = (visited.toDouble() / total.toDouble()) * 100
            progress = pct.toInt() // will autom. be ceiled
        }
        pbVisitdProgress.setProgress(progress, true)
        tvPctVisitedVal.text = "$progress%"
    }

    /**
     * Notification that the progress level has changed. Clients can use the fromUser parameter
     * to distinguish user-initiated changes from those that occurred programmatically.
     *
     * @param seekBar The SeekBar whose progress has changed
     * @param progress The current progress level. This will be in the range min..max where min
     * and max were set by [ProgressBar.setMin] and
     * [ProgressBar.setMax], respectively. (The default values for
     * min is 0 and max is 100.)
     * @param fromUser True if the progress change was initiated by the user.
     */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if(seekBar != null && fromUser){
            if(seekBar.id == R.id.sbNumImgColVal){
                // there is no min. at used API level so 0 is always included but it must start from 1!
                var progTxt = seekBar.progress
                progTxt++
                tvImgColThumbVal.text = progTxt.toString()
            }
            else{
                if(progress == 0){
                    tvNumImgThumbVal.text = "\u221E"
                }
                else{
                    tvNumImgThumbVal.text = progress.toString()
                }
            }
        }
    }

    /**
     * Notification that the user has started a touch gesture. Clients may want to use this
     * to disable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    /**
     * Notification that the user has finished a touch gesture. Clients may want to use this
     * to re-enable advancing the seekbar.
     * @param seekBar The SeekBar in which the touch gesture began
     */
    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }
}