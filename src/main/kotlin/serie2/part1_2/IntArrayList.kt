package serie2.part1_2

// Estrutura circular de inteiros com capacidade fixa k.
// Implementa disciplina FIFO e suporta deslocamento global (delta) eficiente via addToAll.

class IntArrayList(private val k: Int) {
    private val dataArray = IntArray(k)
    private var start = 0
    private var end = 0
    private var size = 0
    private var delta = 0

    // Insere um valor no fim da lista, ajustando pelo deslocamento atual.
    fun append(x: Int): Boolean {
        if (size >= k) return false
        dataArray[end] = x - delta
        end = (end + 1) % k
        size++
        return true
    }

    // Devolve o elemento na posição lógica n (0-base), se existir.
    fun get(n: Int): Int? {
        if (n !in 0 until size) return null
        val idx = (start + n) % k
        return dataArray[idx] + delta
    }

    // Aplica incremento global a todos os elementos da lista.
    fun addToAll(x: Int) {
        delta += x
    }

    // Remove o elemento mais antigo (o que está no início da fila).
    fun remove(): Boolean {
        if (size == 0) return false
        start = (start + 1) % k
        size--
        return true
    }

    // Iterador ainda não implementado
    fun iterator(): Iterator<Int> {
        TODO("Not yet implemented")
    }
}
