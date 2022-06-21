package com.eachilin.codechallenge

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.eachilin.codechallenge.R.drawable.*
import com.eachilin.codechallenge.databinding.ActivityEventDetailBinding
import com.eachilin.database.EventLike
import com.eachilin.database.EventLikeModal
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private const val TAG ="EventDetailActivity"
class EventDetailActivity : AppCompatActivity() {

    private lateinit var binding : ActivityEventDetailBinding
    private lateinit var eventInfo :Event
    private lateinit var eventLike : EventLike

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()


        eventLike = EventLike(this)

        eventInfo = intent.getSerializableExtra("eventInfo") as Event
        assignValue()

        binding.ivDetailHeart.setOnClickListener {
            val filledHeart = this.getDrawable(filled_heart)
            val unfilledHeart = this.getDrawable(unfilled_heart)
            val getImageViewDrawable = binding.ivDetailHeart.drawable


           // get constantState from image to verify images are the same
            //image is unfilled then it will be set to filled
            if(unfilledHeart!!.constantState == getImageViewDrawable.constantState){
                val event = EventLikeModal( eventInfo.id, 0)
                eventLike.addNewLike(event)
                binding.ivDetailHeart.setImageDrawable(filledHeart)

            }else{
                eventLike.deleteLike(eventInfo.id)
                binding.ivDetailHeart.setImageDrawable(unfilledHeart)

            }

        }

        binding.ivBackIcon.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }


    }

    @SuppressLint("NewApi", "UseCompatLoadingForDrawables")
    private fun assignValue() {
        val date = eventInfo.datetime_local
        val localDateFormat = LocalDateTime.parse(date)
        val dateFormatter = DateTimeFormatter.ofPattern("E, dd MMM yyyy h:mm a")
        val outputDate = localDateFormat.format(dateFormatter).uppercase()

        binding.tvDetailTitle.text = eventInfo.title
        binding.tvEventDetailLocation.text = eventInfo.venue.display_location
        binding.tvEventDetailTime.text = outputDate

        Glide.with(this)
            .load(eventInfo.performers[0].image)
            .into(binding.ivDetailEvent)

        // check if image is liked in database by checking to see if the id exist
        val isLike = eventLike.checkLikeExist(eventInfo.id.toString())
        if(isLike){
            val filledHeart = getDrawable(filled_heart)
            binding.ivDetailHeart.setImageDrawable(filledHeart)
        }
    }
}