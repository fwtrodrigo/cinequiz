package br.com.cinequiz.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.recyclerview.widget.RecyclerView
import br.com.cinequiz.R
import br.com.cinequiz.domain.UsuarioMedalhaJoin

class MedalhasAdapter(
    private val context: Context,
    var listaMedalhas: ArrayList<UsuarioMedalhaJoin>
) : RecyclerView.Adapter<MedalhasAdapter.MedalhaViewHolder>() {

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

        Log.d("MEDALHAS ADAPTER", medalha.toString())

        if (!medalha.flag) {
            Log.d("MEDALHAS ADAPTER", "ENTROU AQUI . ${medalha.titulo} . ${medalha.flag}")
            holder.constraintLayoutItemMedalha.setBackgroundResource(R.drawable.shape_fundo_medalha_desativado)
            holder.btShare.setImageResource(R.drawable.ic_btn_share_desativado)
            holder.btShare.setBackgroundResource(R.drawable.shape_circulo_desativado)
            holder.ivMedalhaFundo.setImageResource(R.drawable.img_fundo_medalha_desativado)
            return
        } else {
            Log.d("MEDALHAS ADAPTER", "ENTROU AQUI . ${medalha.titulo} . ${medalha.flag}")
            holder.constraintLayoutItemMedalha.setBackgroundResource(R.drawable.shape_fundo_medalha)
            holder.btShare.setBackgroundResource(R.drawable.shape_circulo_laranja)
            holder.btShare.setImageResource(R.drawable.ic_compartilhar_temp)
            val drawable = AppCompatResources.getDrawable(context, R.drawable.ic_btn_share)
            holder.btShare.setImageDrawable(drawable)
            holder.ivMedalhaFundo.setImageResource(R.drawable.img_fundo_medalha)
        }


        holder.btShare.setOnClickListener {
            compartilhar(medalha.titulo)
        }
    }

    override fun getItemCount() = listaMedalhas.size

    class MedalhaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvMedalhaTit: TextView = itemView.findViewById(R.id.tvMedalhaTit)
        var tvMedalhaDesc: TextView = itemView.findViewById(R.id.tvMedalhaDesc)
        var ivMedalhaFundo: ImageView = itemView.findViewById(R.id.ivMedalhaFundo)
        var btShare: ImageView = itemView.findViewById(R.id.btShare)
        var constraintLayoutItemMedalha: androidx.constraintlayout.widget.ConstraintLayout =
            itemView.findViewById(R.id.constraintLayoutItemMedalha)
    }

    private fun compartilhar(tituloMedalha: String) {
        val titulo = "Compartilhe sua conquista com os seus amigos!"
        val mensagem =
            "Eu conquistei a medalha $tituloMedalha do jogo CineQuiz!\nBaixe agora o app na PlayStore e teste seus conhecimentos do mundo cinematográfico!"
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(android.content.Intent.EXTRA_TEXT, mensagem)
        context.startActivity(Intent.createChooser(intent, titulo))
    }
}
