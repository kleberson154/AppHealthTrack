package com.kleberson.appmedical.view

import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.kleberson.appmedical.R
import com.kleberson.appmedical.controller.UserController
import com.kleberson.appmedical.model.Medicines
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.Locale

class MedicineAdapter(private val medicines: MutableList<Medicines>): RecyclerView.Adapter<MedicineAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.textViewNameMedicine)
        val date: TextView = itemView.findViewById(R.id.textViewDateMedicine)
        val time: TextView = itemView.findViewById(R.id.textViewTimeMedicine)
        val quantity: TextView = itemView.findViewById(R.id.textViewQuantityMedicine)

        fun bind(medicines: Medicines) {
            nome.text = medicines.name
            date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(medicines.dateLimit)
            time.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(medicines.time)
            quantity.text = medicines.quantity
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_medicine, parent, false)
        return ViewHolder(view)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicine = medicines[position]
        holder.nome.text = medicine.name
        holder.date.text = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(medicine.dateLimit)
        val atDateMedicine = medicine.atDate.format(DateTimeFormatter.ofPattern("HH:mm"))
        val proximaHora = atDateMedicine.substring(0, 2).toInt() + medicine.time
        holder.time.text = String.format("%02d:00", proximaHora)
        holder.quantity.text = medicine.quantity
        val userController = UserController(holder.itemView.context)
        holder.itemView.setOnLongClickListener{
            holder.bind(medicines[position])
            if (position != RecyclerView.NO_POSITION) {
                userController.deleteMedicine(medicines[position], holder.itemView.context)
                medicines.removeAt(position)
                notifyItemRemoved(position)
            }
            true
        }

    }

    override fun getItemCount() = medicines.size
}