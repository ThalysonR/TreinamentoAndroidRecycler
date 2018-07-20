package br.com.alura.ceep.dao

import java.util.ArrayList
import java.util.Arrays
import java.util.Collections

import br.com.alura.ceep.model.Nota

class NotaDAO {

    fun todos(): MutableList<Nota> {
        return notas.toMutableList()
    }

    fun insere(vararg notas: Nota) {
        NotaDAO.notas.addAll(listOf(*notas))
    }

    fun altera(posicao: Int, nota: Nota) {
        notas[posicao] = nota
    }

    fun remove(posicao: Int) {
        notas.removeAt(posicao)
    }

    fun troca(posicaoInicio: Int, posicaoFim: Int) {
        Collections.swap(notas, posicaoInicio, posicaoFim)
    }

    fun removeTodos() {
        notas.clear()
    }

    companion object {

        private val notas = mutableListOf<Nota>()
    }
}
