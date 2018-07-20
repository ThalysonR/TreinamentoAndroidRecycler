package br.com.alura.ceep.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.widget.Toast
import br.com.alura.ceep.R.layout.activity_lista_notas
import br.com.alura.ceep.dao.NotaDAO
import br.com.alura.ceep.model.Nota
import br.com.alura.ceep.ui.recyclerview.adapter.ListaNotasAdapter
import br.com.alura.ceep.ui.recyclerview.helper.callback.NotaItemTouchHelperCallback
import kotlinx.android.synthetic.main.activity_lista_notas.*

class ListaNotasActivity : AppCompatActivity() {

    private lateinit var adapter: ListaNotasAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_lista_notas)

        val todasNotas = pegaTodasNotas()
        configuraRecyclerView(todasNotas)
        configuraBotaoInsereNota()
    }

    private fun configuraBotaoInsereNota() {
        val botaoInsereNota = lista_notas_insere_nota
        botaoInsereNota.setOnClickListener { vaiParaFormularioNotaActivityInsere() }
    }

    private fun vaiParaFormularioNotaActivityInsere() {
        val iniciaFormularioNota = Intent(this@ListaNotasActivity,
                FormularioNotaActivity::class.java)
        startActivityForResult(iniciaFormularioNota,
                CODIGO_REQUISICAO_INSERE_NOTA)
    }

    private fun pegaTodasNotas(): MutableList<Nota> {
        val dao = NotaDAO()
        for(i in 1..10) {
            dao.insere(
                    Nota("Titulo $i", "Descricao $i")
            )
        }
        return dao.todos()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        data?.let {
            if (ehResultadoInsereNota(requestCode,  data)) {
                if(resultadoOk(resultCode)) {
                    val notaRecebida = data.getSerializableExtra(CHAVE_NOTA) as Nota
                    adiciona(notaRecebida)
                }
            }
            if (ehResultadoAlteraNota(requestCode, data)) {
                if(resultadoOk(resultCode)) {
                    val notaRecebida = data.getSerializableExtra(CHAVE_NOTA) as Nota
                    val posicaoRecebida = data.getIntExtra(CHAVE_POSICAO, POSICAO_INVALIDA)
                    if(ehPosicaoValida(posicaoRecebida)) {
                        altera(notaRecebida, posicaoRecebida)
                    } else {
                        Toast.makeText(this, "Ocorreu um problema na alteração da nota", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun altera(nota: Nota, posicao: Int) {
        NotaDAO().altera(posicao, nota)
        adapter.altera(posicao, nota)
    }

    private fun ehPosicaoValida(posicaoRecebida: Int) = posicaoRecebida > POSICAO_INVALIDA

    private fun ehResultadoAlteraNota(requestCode: Int, data: Intent): Boolean {
        return ehCodigoRequisicaoAlteraNota(requestCode) &&
                temNota(data)
    }

    private fun ehCodigoRequisicaoAlteraNota(requestCode: Int) =
            requestCode == CODIGO_REQUISICAO_ALTERA_NOTA

    private fun adiciona(nota: Nota) {
        NotaDAO().insere(nota)
        adapter.adiciona(nota)
    }

    private fun ehResultadoInsereNota(requestCode: Int, data: Intent): Boolean {
        return ehCodigoRequisicaoInsereNota(requestCode) &&
                temNota(data)
    }

    private fun temNota(data: Intent): Boolean {
        return data.hasExtra(CHAVE_NOTA)
    }

    private fun resultadoOk(resultCode: Int): Boolean {
        return resultCode == Activity.RESULT_OK
    }

    private fun ehCodigoRequisicaoInsereNota(requestCode: Int): Boolean {
        return requestCode == CODIGO_REQUISICAO_INSERE_NOTA
    }

    private fun configuraRecyclerView(todasNotas: MutableList<Nota>) {
        val listaNotas = lista_notas_recyclerview
        configuraAdapter(todasNotas, listaNotas)
        val itemTouchHelper = ItemTouchHelper(NotaItemTouchHelperCallback(adapter))
        itemTouchHelper.attachToRecyclerView(listaNotas)
    }

    private fun configuraAdapter(todasNotas: MutableList<Nota>, listaNotas: RecyclerView) {
        adapter = ListaNotasAdapter(this, todasNotas)
        listaNotas.adapter = adapter
        adapter.onItemClick = {nota, posicao ->
            vaiParaformularioNotaActivityAltera(nota, posicao)
        }
    }

    private fun vaiParaformularioNotaActivityAltera(nota: Nota, posicao: Int) {
        val abreFormularioComNota = Intent(this, FormularioNotaActivity::class.java)
        abreFormularioComNota.putExtra(CHAVE_NOTA, nota)
        abreFormularioComNota.putExtra(CHAVE_POSICAO, posicao)
        startActivityForResult(abreFormularioComNota, CODIGO_REQUISICAO_ALTERA_NOTA)
    }

}
