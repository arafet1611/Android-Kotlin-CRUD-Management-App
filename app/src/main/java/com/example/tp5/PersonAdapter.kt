package com.example.tp5

import android.view.View
import androidx.recyclerview.widget.RecyclerView

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView


class PersonAdapter(private var dataList: MutableList<MyDataModel>) :
    RecyclerView.Adapter<PersonAdapter.PersonViewHolder>() {
private var onClickItems : ((MyDataModel) -> Unit)? =null
    class PersonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.textViewName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_person, parent, false)
        return PersonViewHolder(view)
    }
fun setOnclickItem(callback : (MyDataModel) -> Unit) {
    this.onClickItems =callback
}
    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val currentItem = dataList[position]

        holder.nameTextView.text = currentItem.name
        holder.itemView.setOnClickListener { onClickItems?.invoke(currentItem    )}

    }

    fun updateData(newDataList: MutableList<MyDataModel>) {
        dataList.clear()
        dataList.addAll(newDataList)
        notifyDataSetChanged()
    }
    fun updateItem(updatedItem: MyDataModel) {
        val position = dataList.indexOfFirst { it.id == updatedItem.id }

        if (position != -1) {
            dataList[position] = updatedItem
            notifyItemChanged(position)
            notifyDataSetChanged()

        }
    }
    fun getDataList(): List<MyDataModel> {
        return dataList
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
}

