package com.dagf.uweyt3.iptv

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dagf.uweyt3.R
import com.dagf.uweyt3.utils.UtilsIPTV
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Response

class ChannelAdapter(context: Context, channelsList: List<M3UItem>, defaultIconDisplay: Boolean = true,
                     openPlayer: Boolean = false) : RecyclerView.Adapter<ChannelAdapter.ViewHolder>() {

    private var mContext: Context
    private var mChannelsList: List<M3UItem>
    private val apiInterface: ApiInterface?
    private var mDefaultIconDisplay: Boolean
    private var mOpenPlayer: Boolean
    companion object{
        public var listenerclik: UtilsIPTV.onClickChannel? = null

    }

    init {
        listenerclik = UtilsIPTV.clickChannel;
        mContext = context
        mOpenPlayer = openPlayer
        mDefaultIconDisplay = defaultIconDisplay
        mChannelsList = channelsList
        apiInterface = ApiClient.getCountryAPI()?.create(ApiInterface::class.java)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(mContext).inflate(R.layout.channel_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val channel = mChannelsList.get(position)

        holder.mChannelName.text = channel.tvName
      //  Log.e("MAIN", "lol chamo si "+channel.tvName );

        if(mDefaultIconDisplay)
            holder.mChannelLogo.setImageResource(R.drawable.ic_tv)
        else {
            if(channel.tvIcon.isNullOrEmpty())
                getCountryFlag(position, holder)
            else
                Picasso.get()
                    .load(channel.tvIcon)
                    .memoryPolicy(MemoryPolicy.NO_STORE, MemoryPolicy.NO_CACHE)
                    .placeholder(R.drawable.ic_tv)
                    .resize(74, 64)
                    .error(R.drawable.ic_tv)
                    .into(holder.mChannelLogo)
        }

        if (mOpenPlayer){
            holder.itemView.setOnClickListener {


                val intent = Bundle()
                intent.putParcelable("CHANNEL_DETAILS", channel)
                intent.putString(ExoPlayerActivity.key_data, UtilsIPTV.ad_banner)
if(listenerclik != null)
                listenerclik!!.onCliked(intent)
            }
        }
        else {
            holder.itemView.setOnClickListener {
                val intent = Intent(mContext, ChannelsActivity::class.java)
                intent.putExtra("CHANNELS", channel)
                mContext.startActivity(intent)
                listenerclik!!.clickCountry()
            }
        }
    }

    override fun getItemCount(): Int {
        return mChannelsList.size
    }

    fun filterChannels(filterChannels: List<M3UItem>) {
        mChannelsList = filterChannels
        notifyDataSetChanged()
    }

    private fun getCountryFlag(position: Int, holder: ViewHolder) {
        val getCountry: Call<Array<Country>> = apiInterface!!.getCountryDetails(mChannelsList.get(position).tvName)
        getCountry.enqueue(object : retrofit2.Callback<Array<Country>> {
            override fun onResponse(call: Call<Array<Country>>, response: Response<Array<Country>>) {
                if(response.isSuccessful) {
                    try {
                        val tvIcon = response.body()?.get(0)?.flag
                        mChannelsList.get(position).tvIcon = tvIcon
                        Picasso.get()
                            .load(tvIcon)
                            .placeholder(R.drawable.ic_tv)
                            .error(R.drawable.ic_tv)
                            .resize(74, 64)
                            .into(holder.mChannelLogo)
                    } catch (ignore: IndexOutOfBoundsException) { }
                }
            }

            override fun onFailure(call: Call<Array<Country>>, t: Throwable) {

            }
        })
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        internal val mView: View
        internal val mChannelName: TextView
        internal val mChannelLogo: ImageView

        init {
            mView = itemView
            mChannelName = itemView.findViewById(R.id.channelName)
            mChannelLogo = itemView.findViewById(R.id.channelLogo)
            mChannelName.setSingleLine(true)
            mChannelName.isFocusable = true
            mChannelName.isSelected = true
            mChannelName.ellipsize = TextUtils.TruncateAt.MARQUEE
        }
    }
}