package com.eachilin.codechallenge

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources.getDrawable
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.eachilin.codechallenge.databinding.EventItemBinding
import com.eachilin.database.EventLike
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class EventAdapter(private val eventData: List<Event>) : RecyclerView.Adapter<EventAdapter.EventAdapterViewHolder>() {

    private var _binding: EventItemBinding? = null
    private val binding get() = _binding!!
    private lateinit var eventLike : EventLike

    class EventAdapterViewHolder(itemView: EventItemBinding) :RecyclerView.ViewHolder(itemView.root) {
        @SuppressLint("NewApi")
        fun bind(
            eventItem: Event,
            binding: EventItemBinding,
            isLike: Boolean,
            filledHeart: Drawable?
        ) {
            val date = eventItem.datetime_local
            val localDateFormat = LocalDateTime.parse(date)
            val dateFormatter = DateTimeFormatter.ofPattern("E, MMM yyyy h:mm a")
            val outputDate = localDateFormat.format(dateFormatter).uppercase()

            val image = eventItem.performers[0].image

            binding.tvEventTitle.text = eventItem.title
            binding.tvEventLocation.text = eventItem.venue.display_location
            binding.tvtimeInfo.text = outputDate

            Glide.with(itemView.context)
                .load(image)
                .transform(CenterCrop(), RoundedCorners(24))
                .into(binding.ivEventIcon)

            if(isLike){
               binding.ivHeart.setImageDrawable(filledHeart)
            }else{
                binding.ivHeart.isVisible = false
            }

            binding.cardEventHolder.setOnClickListener {
                val intent = Intent(itemView.context, EventDetailActivity::class.java)
                intent.putExtra("eventInfo", eventItem)
                itemView.context.startActivity(intent)
            }

        }


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EventAdapterViewHolder {
        _binding = EventItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return EventAdapterViewHolder(_binding!!)
    }

    override fun onBindViewHolder(holder: EventAdapterViewHolder, position: Int) {
        var context = binding.root.context


        val eventItem = eventData[position]
        eventLike = EventLike( context)
        val isLike =  eventLike.checkLikeExist(eventItem.id.toString())
        val filledHeart = getDrawable(context, R.drawable.filled_heart)


        holder.bind(eventItem, binding, isLike, filledHeart)


    }

    override fun getItemCount(): Int {
        return eventData.size
    }

// prevent item from appearing twice when scrolling
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


}
