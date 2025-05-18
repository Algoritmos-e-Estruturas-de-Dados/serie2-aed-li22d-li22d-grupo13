package serie2.part4

/**
 * Estrutura de dicionário baseada numa hash table com resolução de colisões por encadeamento.
 */
class HashMap<K, V>(
    capacidadeInicial: Int = 16,
    val fatorCarga: Float = 0.75f
) : MutableMap<K, V> {

    /**
     * Elemento que representa um par chave-valor na lista ligada usada para colisões.
     */
    private class EntradaHash<K, V>(
        override val key: K,
        override var value: V,
        var proxima: EntradaHash<K, V>? = null
    ) : MutableMap.MutableEntry<K, V> {
        override fun setValue(novoValor: V): V {
            val anterior = value
            value = novoValor
            return anterior
        }
    }

    private var baldes: Array<EntradaHash<K, V>?> = arrayOfNulls(capacidadeInicial)
    override var size = 0
    override val capacity: Int
        get() = baldes.size

    private fun calcularIndice(chave: K): Int {
        val hash = chave.hashCode() % capacity
        return if (hash >= 0) hash else hash + capacity
    }

    override fun get(key: K): V? {
        val pos = calcularIndice(key)
        var atual = baldes[pos]
        while (atual != null) {
            if (atual.key == key) return atual.value
            atual = atual.proxima
        }
        return null
    }

    override fun put(key: K, value: V): V? {
        if (size.toFloat() / capacity >= fatorCarga) expandir()

        val pos = calcularIndice(key)
        var cursor = baldes[pos]

        while (cursor != null) {
            if (cursor.key == key) {
                val anterior = cursor.value
                cursor.value = value
                return anterior
            }
            cursor = cursor.proxima
        }

        baldes[pos] = EntradaHash(key, value, baldes[pos])
        size++
        return null
    }

    private fun expandir() {
        val novaCapacidade = capacity * 2
        val novosBaldes = arrayOfNulls<EntradaHash<K, V>?>(novaCapacidade)

        for (i in baldes.indices) {
            var atual = baldes[i]
            while (atual != null) {
                val seguinte = atual.proxima
                var novoIndice = atual.key.hashCode() % novaCapacidade
                if (novoIndice < 0) novoIndice += novaCapacidade

                atual.proxima = novosBaldes[novoIndice]
                novosBaldes[novoIndice] = atual
                atual = seguinte
            }
        }
        baldes = novosBaldes
    }

    override fun iterator(): Iterator<MutableMap.MutableEntry<K, V>> {
        return object : Iterator<MutableMap.MutableEntry<K, V>> {
            private var posicao = 0
            private var atual: EntradaHash<K, V>? = null

            init {
                avancar()
            }

            private fun avancar() {
                if (atual?.proxima != null) {
                    atual = atual!!.proxima
                    return
                }
                while (++posicao < capacity) {
                    if (baldes[posicao] != null) {
                        atual = baldes[posicao]
                        return
                    }
                }
                atual = null
            }

            override fun hasNext(): Boolean = atual != null

            override fun next(): MutableMap.MutableEntry<K, V> {
                if (!hasNext()) throw NoSuchElementException()
                val retorno = atual!!
                avancar()
                return retorno
            }
        }
    }
}
