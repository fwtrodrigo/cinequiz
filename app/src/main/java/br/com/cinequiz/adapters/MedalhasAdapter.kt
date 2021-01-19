package br.com.cinequiz.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import br.com.cinequiz.R
import br.com.cinequiz.domain.Medalha
import br.com.cinequiz.domain.UsuarioMedalhaJoin

class MedalhasAdapter() : RecyclerView.Adapter<MedalhasAdapter.MedalhaViewHolder>() {
    val listaMedalhas = ArrayList<UsuarioMedalhaJoin>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MedalhasAdapter.MedalhaViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_medalha, parent, false)
        return MedalhaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MedalhasAdapter.MedalhaViewHolder, position: Int) {
        var medalha = listaMedalhas[position]
        holder.tvMedalhaTit.text = medalha.titulo
        holder.tvMedalhaDesc.text = medalha.descricao
//        holder.ivMedalhaIcone.setImageDrawable()
    }

    override fun getItemCount() = listaMedalhas.size

    class MedalhaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMedalhaTit: TextView = itemView.findViewById(R.id.tvMedalhaTit)
        var tvMedalhaDesc: TextView = itemView.findViewById(R.id.tvMedalhaDesc)
//        var ivMedalhaIcone: ImageView = itemView.findViewById(R.id.ivMedalhaIcone)
    }
}
