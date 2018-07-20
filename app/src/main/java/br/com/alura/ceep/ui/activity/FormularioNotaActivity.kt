package br.com.alura.ceep.ui.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import br.com.alura.ceep.R.id.menu_formulario_nota_ic_salva
import br.com.alura.ceep.R.layout.activity_formulario_nota
import br.com.alura.ceep.R.menu.menu_formulario_nota_salva
import br.com.alura.ceep.model.Nota
import kotlinx.android.synthetic.main.activity_formulario_nota.*

const val TITULO_APPBAR_INSERE = "Insere Nota"
const val TITULO_APPBAR_ALTERA = "Altera Nota"

class FormularioNotaActivity : AppCompatActivity() {
    private var posicaoRecebida = POSICAO_INVALIDA
    private val titulo by lazy {
        formulario_nota_titulo as TextView
    }
    private val descricao by lazy {
        formulario_nota_descricao as TextView
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activity_formulario_nota)

        title = TITULO_APPBAR_INSERE

        if(intent.hasExtra(CHAVE_NOTA)) {
            title = TITULO_APPBAR_ALTERA
            val notaRecebida = intent.getSerializableExtra(CHAVE_NOTA) as Nota
            posicaoRecebida = intent.getIntExtra(CHAVE_POSICAO, -1)
            preencheCampos(notaRecebida)
        }
    }

    private fun preencheCampos(notaRecebida: Nota) {
        titulo.text = notaRecebida.titulo
        descricao.text = notaRecebida.descricao
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(menu_formulario_nota_salva, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (ehMenuSalvaNota(item)) {
            val notaCriada = criaNota()
            retornaNota(notaCriada)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun retornaNota(nota: Nota) {
        val resultadoInsercao = Intent()
        resultadoInsercao.putExtra(CHAVE_NOTA, nota)
        resultadoInsercao.putExtra(CHAVE_POSICAO, posicaoRecebida)
        setResult(Activity.RESULT_OK, resultadoInsercao)
    }

    private fun criaNota(): Nota {
        return Nota(titulo.text.toString(),
                descricao.text.toString())
    }

    private fun ehMenuSalvaNota(item: MenuItem): Boolean {
        return item.itemId == menu_formulario_nota_ic_salva
    }
}
