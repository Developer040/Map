package com.developer.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.developer.map.Cash.MySharedPreferences
import com.developer.map.databinding.ActivityListBinding
import com.developer.map.databinding.ItemRvBinding
import com.google.android.gms.maps.model.LatLng

class ListActivity : AppCompatActivity() {
    lateinit var binding: ActivityListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MySharedPreferences.init(this)
        var list = MySharedPreferences.obektString

        binding.rv.adapter = RvListAdapter(list)

    }

    class RvListAdapter(var list: ArrayList<LatLng>) :
        RecyclerView.Adapter<RvListAdapter.Vh>() {
        inner class Vh(var itemRv: ItemRvBinding) : RecyclerView.ViewHolder(itemRv.root) {
            fun onBind(latLng: LatLng, position: Int) {
                itemRv.tvLatLng.text =
                    "latitude - ${latLng.latitude}\nlongitude - ${latLng.longitude}"
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
            return Vh(ItemRvBinding.inflate(LayoutInflater.from(parent?.context), parent, false))
        }


        override fun onBindViewHolder(holder: Vh, position: Int) {
            holder.onBind(list[position], position)
        }

        override fun getItemCount(): Int = list.size
    }
}