package br.com.alura.ceep.ui.recyclerview.helper.callback

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import br.com.alura.ceep.dao.NotaDAO
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter

class NotaItemTouchHelperCallback(private val adapter: ListaNotasAdapter) : ItemTouchHelper.Callback() {
    private var dragFrom = -1
    private var dragTo = -1

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val marcacoesDeDeslize = ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val marcacoesDeArrastar = ItemTouchHelper.DOWN or ItemTouchHelper.UP or ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        return makeMovementFlags(marcacoesDeArrastar, marcacoesDeDeslize)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        val posicaoInicial = viewHolder?.adapterPosition
        val posicaoFinal = target?.adapterPosition
        posicaoInicial?.let {
            posicaoFinal?.let {
                dragFrom = posicaoInicial
                dragTo = posicaoFinal
                if (dragFrom != dragTo) {
                    NotaDAO().troca(dragFrom, dragTo)
                    adapter.troca(posicaoInicial, posicaoFinal)
                }
            }
        }
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {
        val posicaoDaNotaDeslizada = viewHolder?.adapterPosition
        posicaoDaNotaDeslizada?.let {
            NotaDAO().remove(posicaoDaNotaDeslizada)
            adapter.remove(posicaoDaNotaDeslizada)
        }
    }
}
