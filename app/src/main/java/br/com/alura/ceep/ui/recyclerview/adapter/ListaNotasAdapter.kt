package br.com.alura.ceep.ui.recyclerview.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.alura.ceep.R.layout.item_nota
import br.com.alura.ceep.model.Nota
import kotlinx.android.synthetic.main.item_nota.view.*

class ListaNotasAdapter(private val context: Context,
                        private val notas: MutableList<Nota>) : RecyclerView.Adapter<ListaNotasAdapter.NotaViewHolder>() {

    var onItemClick: (Nota, Int) -> Unit = {_,_ ->}
    private lateinit var mRecyclerView: RecyclerView

    inner class NotaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val titulo = itemView.item_nota_titulo
        private val descricao = itemView.item_nota_descricao

        private lateinit var nota: Nota

        init {
            itemView.setOnClickListener {
                onItemClick(nota, adapterPosition)
            }
        }

        fun vincula(nota: Nota) {
            this.nota = nota
            titulo.text = nota.titulo
            descricao.text = nota.descricao
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        mRecyclerView = recyclerView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotaViewHolder {
        val view = LayoutInflater.from(context).inflate(item_nota, parent, false)
        return NotaViewHolder(view)
    }

    override fun getItemCount(): Int {
        return notas.size
    }

    override fun onBindViewHolder(holder: NotaViewHolder, position: Int) {
        holder.vincula(notas[position])
    }

    fun adiciona(nota: Nota) {
        notas.add(nota)
        notifyDataSetChanged()
    }

    fun altera(posicao: Int, nota: Nota) {
        notas[posicao] = nota
        notifyItemChanged(posicao)
    }

    fun remove(posicao: Int) {
        notas.removeAt(posicao)
        notifyItemRemoved(posicao)
    }

    fun troca(posicaoInicial: Int, posicaoFinal: Int) {
        notas[posicaoInicial] = notas[posicaoFinal].also { notas[posicaoFinal] = notas[posicaoInicial] }
        notifyItemMoved(posicaoInicial, posicaoFinal)
        if(posicaoInicial == 0 || posicaoFinal == 0) mRecyclerView.scrollToPosition(0)
    }
}
